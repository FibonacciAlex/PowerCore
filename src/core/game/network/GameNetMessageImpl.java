package core.game.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;
import com.google.protobuf.Parser;

import core.game.GameNetMessage;


/**
 * 协议实现类
 * @author Alex
 * 2015年6月30日 下午3:23:41
 */
public class GameNetMessageImpl implements GameNetMessage{

	private final static Logger log = LoggerFactory.getLogger(GameNetMessageImpl.class);
	
	ByteBuf buffer;
	private boolean inorout;
	private boolean isDuplicate;
	
	//发送后不能写保护
	private final AtomicBoolean cannotbewritten = new AtomicBoolean();
	
	
	public GameNetMessageImpl() {
	}

	
	/**
	 * 构造发送给客户端的消息
	 * @param msgType
	 * @param clientType
	 * @param msgID
	 */
	GameNetMessageImpl(byte msgType, byte clientType, int msgID){
		buffer = Unpooled.buffer();
		buffer.writeByte(msgType);
		buffer.writeByte(clientType);
		buffer.writeInt(msgID);
		buffer.writeInt(0);//payload的长度没定，后面再进行设置
		buffer.writeInt(0);//检验码位置  暂时未知
		inorout = false;
	}
	
	/**
	 * 构造对象表示从客户端发过来的消息，平台内部调用
	 * @param msgType
	 * @param clientType
	 * @param msgID
	 * @param payloadLength
	 * @param checksum
	 * @param srcBuf
	 */
	GameNetMessageImpl(byte msgType, byte clientType, int msgID,
			int payloadLength, int checksum, ByteBuf srcBuf){
		buffer = Unpooled.buffer(LENGTH_OF_HEADER
				+ payloadLength);
		buffer.writeByte(msgType);
		buffer.writeByte(clientType);
		buffer.writeInt(msgID);
		buffer.writeInt(payloadLength);
		buffer.writeInt(checksum);
		if(payloadLength > 0){
			buffer.writeBytes(srcBuf, payloadLength);
		}
		buffer.skipBytes(LENGTH_OF_HEADER);
		inorout = true;
	}
	
	
	private boolean isCannotBeWritten(){
		if(cannotbewritten.get()){
			log.warn("msg [{}] can not be write, it was send!");
			return true;
		}
		return false;
	}
	
	void closeWriteOp(){
		cannotbewritten.set(true);
	}
	

	
	private boolean checkifNotAllowToChangeDate(int index){
		return isDuplicate && index > INDEX_CLIENTTYPE;
	}
	
	private void writeProtoBufInternal(MessageLite protobuf){
		byte[] array = protobuf.toByteArray();
		buffer.writeInt(array.length);
		buffer.writeBytes(array);
	}
	
	boolean isDuplicate(){
		return isDuplicate;
	}
	
	
	@Override
	public byte getEncrytion() {
		return (byte) ((buffer.getByte(INDEX_MSGTYPE)>>>4)&0x0F);
	}

	@Override
	public void setEncrytion(byte en) {
		byte b = buffer.getByte(INDEX_MSGTYPE);
		byte b1 = (byte) (((en<<4)&0xFF)+(b&0xFF));
		buffer.setByte(INDEX_MSGTYPE, b1);
	}

	@Override
	public byte getUndefined() {
		return (byte) ((buffer.getByte(INDEX_CLIENTTYPE)>>>4)&0x0F);
	}

	@Override
	public void setUndefined(byte un) {
		byte b = buffer.getByte(INDEX_CLIENTTYPE);
		byte b1 = (byte) (((un<<4)&0xFF)+(b&0xFF));
		buffer.setByte(INDEX_CLIENTTYPE, b1);
		
	}

	@Override
	public byte getMsgType() {
		return (byte) ((buffer.getByte(INDEX_MSGTYPE)>>>0)&0x0F);
	}

	@Override
	public byte getClientType() {
		return (byte) ((buffer.getByte(INDEX_CLIENTTYPE)>>>0)&0x0F);
	}

	@Override
	public int getMsgID() {
		return buffer.getInt(INDEX_MSGID);
	}

	@Override
	public int getPayloadLength() {
		if(inorout){
			return buffer.getInt(INDEX_PAYLOADLENGTH);
		}else{
			return buffer.writerIndex() - LENGTH_OF_HEADER;
		}
	}

	@Override
	public GameNetMessage duplicate() {
		return duplicate(true);
	}

	@Override
	public GameNetMessage duplicate(boolean resetReadIndex) {
		GameNetMessageImpl duplication = new GameNetMessageImpl();
		duplication.inorout = this.inorout;
		duplication.isDuplicate = true;
		duplication.buffer = this.buffer.duplicate();
		if(inorout && resetReadIndex){
			duplication.buffer.readerIndex(INDEX_START_PAYLOAD);
		}
		return duplication;
	}
	
	

	@Override
	public GameNetMessage copy() {
		GameNetMessageImpl copy = new GameNetMessageImpl();
		copy.inorout = this.inorout;
		copy.buffer = Unpooled.buffer(this.buffer.capacity());
		copy.buffer.writeByte(this.getMsgType());
		copy.buffer.writeByte(this.getClientType());
		copy.buffer.writeInt(this.getMsgID());
		copy.buffer.writeInt(this.getPayloadLength());
		copy.buffer.writeInt(this.getCheckSum());
		copy.buffer.writeBytes(buffer, INDEX_START_PAYLOAD,this.getPayloadLength());
		if(inorout){
			copy.skipBytes(LENGTH_OF_HEADER);
		}
		return copy;
	}

	public int getCheckSum(){
		return buffer.getInt(INDEX_START_CHECKSUM);
	}
	
	@Override
	public void skipBytes(int length) {
		buffer.skipBytes(length);
		
	}

	@Override
	public byte readByte() {
		return buffer.readByte();
	}

	@Override
	public short readUnsignedByte() {
		return buffer.readUnsignedByte();
	}

	@Override
	public short readShort() {
		return buffer.readShort();
	}

	@Override
	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
	}

	@Override
	public boolean readBoolean() {
		return buffer.readByte() == FALSE ? false : true;
	}

	@Override
	public int readInt() {
		return buffer.readInt();
	}

	@Override
	public long readUnsignedInt() {
		return buffer.readUnsignedInt();
	}

	@Override
	public long readLong() {
		return buffer.readLong();
	}

	@Override
	public char readChar() {
		return buffer.readChar();
	}

	@Override
	public float readFloat() {
		return buffer.readFloat();
	}

	@Override
	public double readDouble() {
		return buffer.readDouble();
	}

	@Override
	public void readBytes(byte[] dst) {
		buffer.readBytes(dst);
		
	}

	@Override
	public void readBytes(byte[] dst, int dstIndex, int index) {
		buffer.readBytes(dst, dstIndex, index);
		
	}

	@Override
	public void readBytes(OutputStream out, int length) throws IOException {
		buffer.readBytes(out, length);
	}

	@Override
	public <T extends MessageLite> T readProtobufData(Parser<T> parser) {
		int length = buffer.readInt();
		if (length > 0) {
			if (buffer.readableBytes() > length) {
				byte[] array = new byte[length];
				buffer.readBytes(array);
				try {
					return parser.parseFrom(array);
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			} else {
				throw new RuntimeException("not enough bytes...wants:" + length + ", remains:" + buffer.readableBytes());
			}
		} else {
			throw new IllegalArgumentException("illegal length : " + length);
		}
	}

	@Override
	public String readUTF8String(String utf8String) {
		int len = buffer.readInt();
		if(len <= 0 ){
			return "";
		}
		byte[] bs  = new byte[len];
		buffer.readBytes(bs);
		String str = new String(bs, CharsetUtil.UTF_8);
		return str;
	}

	@Override
	public void writeByte(int value) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeByte(value);
	}

	@Override
	public void writeShort(int value) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeShort(value);
	}

	@Override
	public void writeBoolean(boolean value) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeByte(value ? TRUE : FALSE);
	}
	

	@Override
	public void writeInt(int value) {

		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeInt(value);
	}

	@Override
	public void writeLong(int value) {

		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeLong(value);
	}

	@Override
	public void writeChar(int value) {

		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeChar(value);
	}

	@Override
	public void writeFloat(float value) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeFloat(value);
	}

	@Override
	public void writeDouble(double value) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeDouble(value);
	}

	@Override
	public void writeBytes(byte[] src) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeBytes(src);
	}

	@Override
	public void writeBytes(byte[] src, int srcIndex, int length) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.writeBytes(src, srcIndex, length);
		
	}

	@Override
	public void writeProtobuf(MessageLite protobuf) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}else if(protobuf == null){
			throw new NullPointerException("protobuf data can not be null");
		}
		writeProtoBufInternal(protobuf);
	}

	@Override
	public void writeProtobuf(Builder builder) {
		if(isCannotBeWritten()){
			return;
		}else if(isDuplicate){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}else if(builder == null){
			throw new NullPointerException("rotobuf data can not be null");
		}
		writeProtoBufInternal(builder.build());
	}

	@Override
	public void writeUTF8String(String utf8String) {
		if(isCannotBeWritten()){
			return;
		}
		if(utf8String != null){
			byte[] bs = utf8String.getBytes(CharsetUtil.UTF_8);
			if(bs != null){
				buffer.writeInt(bs.length);
				buffer.writeBytes(bs);
				bs = null;
				return;
			}
		}
		buffer.writeInt(0);
	}

	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("KNetMessage[");
		sb.append(getMsgType()).append(",").append(getClientType()).append(",")
				.append(getMsgID()).append(",").append(getPayloadLength())
				.append("]");
		return sb.toString();
	}

	@Override
	public byte getByte(int index) {
		return buffer.getByte(index);
	}

	@Override
	public short getUnsignedByte(int index) {
		return buffer.getUnsignedByte(index);
	}

	@Override
	public short getShort(int index) {
		return buffer.getShort(index);
	}

	@Override
	public int getUnsignedShort(int index) {
		return buffer.getUnsignedShort(index);
	}

	@Override
	public int getInt(int index) {
		return buffer.getInt(index);
	}

	@Override
	public long getUnsignedInt(int index) {
		return buffer.getUnsignedInt(index);
	}

	@Override
	public boolean getBoolean(int index) {
		return buffer.getByte(index) == FALSE ? false : true;
	}

	@Override
	public long getLong(int index) {
		return buffer.getLong(index);
	}

	@Override
	public char getChar(int index) {
		return buffer.getChar(index);
	}

	@Override
	public float getFloat(int index) {
		return buffer.getFloat(index);
	}

	@Override
	public double getDouble(int index) {
		return buffer.getDouble(index);
	}

	@Override
	public void getBytes(int index, byte[] dst) {
		buffer.getBytes(index, dst);
	}

	@Override
	public void getBytes(int index, byte[] dst, int dstIndex, int length) {
		buffer.getBytes(index, dst, dstIndex, length);
		
	}

	@Override
	public void getBytes(int index, OutputStream out, int length)
			throws IOException {
		buffer.getBytes(index,out, length);
	}

	@Override
	public byte[] gatProtobufData(int index) {
		int length = buffer.getInt(index);
		if(length > 0){
			byte[] a = new byte[length];
			buffer.getBytes(index + 4, a);
			return a;
		}else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setByte(int index, int value) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setByte(index, value);
	}

	@Override
	public void setShort(int index, int valeu) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setShort(index, valeu);
		
	}

	@Override
	public void setInt(int index, int value) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setIndex(index, value);
		
	}

	@Override
	public void setLong(int index, long valeu) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setLong(index, valeu);
	}

	@Override
	public void setChar(int index, char value) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setChar(index, value);
	}

	@Override
	public void setFloat(int index, float value) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setFloat(index, value);
		
	}

	@Override
	public void setDouble(int index, float value) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setDouble(index, value);
	}

	@Override
	public void setBytes(int index, byte[] value) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setBytes(index, value);
		
	}

	@Override
	public void setBytes(int index, byte[] value, int valueIndex, int length) {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		buffer.setBytes(index, value,valueIndex,length);
		
	}

	@Override
	public int setBytes(int index, InputStream in, int length)
			throws IOException {
		if(checkifNotAllowToChangeDate(index)){
			throw new UnsupportedOperationException("duplicate msg is not allowed to change data");
		}
		int result = buffer.setBytes(index, in, length);
		return result;
	}

	@Override
	public int writerIndex() {
		return buffer.writerIndex();
	}

	@Override
	public int readerIndex() {
		return buffer.readerIndex();
	}

	@Override
	public int readableBytes() {
		return buffer.readableBytes();
	}

}
