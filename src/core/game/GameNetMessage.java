package core.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

public interface GameNetMessage {
	
	public final static byte FALSE = 0;
	
	public final static byte TRUE = 1;
	
	
	//////////////////////////////////////////////////////////////
	public final static int INDEX_MSGTYPE = 0; //0
	
	public final static int INDEX_CLIENTTYPE = 1; //1
	
	public final static int INDEX_MSGID = 2;//2|3|4|5
	
	public final static int INDEX_PAYLOADLENGTH = 6; //6|7|8|9
	
	public final static int INDEX_START_CHECKSUM = 10; //10|11|12|13
	
	public final static int INDEX_START_PAYLOAD = 14; //14
	
	///////////////////////////以上为消息前置内容/////////////////////////////////////
	public final static int LENGTH_OF_HEADER = 14;

	public static final byte MTYPE_PLAFORM = 0;

	public static final byte CTYPE_ANDROID = 0;
	
	/**获取消息内容的加密方式（如base64、zip等）*/
	byte getEncrytion();
	
	/**设置消息内容的加密方式，！！注意：本值只能为小于16的正整数！！*/
	void setEncrytion(byte en);
	
	/**获取自定义的字段值*/
	byte getUndefined();
	
	/**设置自定义字段值，！！注意：本值只能为小于16的正整数！！*/
	void setUndefined(byte un);
	
	/** 获取消息类型，可重复调用 */
	byte getMsgType();
	
	/** 获取客户端类型，可重复调用 */
	byte getClientType();
	
	/** 获取消息ID，可重复调用 */
	int getMsgID();
	
	/** 获取消息内容的长度，可重复调用 */
	int getPayloadLength();
	
	/**
	 * 对当前{@link GameNetMessage}做一个副本。！！{@link #duplicate()}方法跟{@link #copy()}
	 * 起到的效果是一样，但其实内部实现不同： {@link #duplicate()}方法生成出来的新的{@link GameNetMessage}
	 * 实例跟原实例的数据内容其实是共享的，只是各自维护一套indexs。 而{@link #copy()}是对原{@link GameNetMessage}
	 * 对象的完全的复制。所以一般情况下都使用{@link #duplicate()}方法，更省内存。
	 * 
	 * @return 新的{@link GameNetMessage}副本
	 */
	GameNetMessage duplicate();

	
	/**
	 * 对当前{@link GameNetMessage}做一个副本。！！{@link #duplicate()}方法跟{@link #copy()}
	 * 起到的效果是一样，但其实内部实现不同： {@link #duplicate()}方法生成出来的新的{@link GameNetMessage}
	 * 实例跟原实例的数据内容其实是共享的，只是各自维护一套indexs。 而{@link #copy()}是对原{@link GameNetMessage}
	 * 对象的完全的复制。所以一般情况下都使用{@link #duplicate()}方法，更省内存。
	 * 
	 * @param resetReaderIndex
	 *            对新产生的{@link GameNetMessage}副本的读取index是否要重设，如果原
	 *            {@link GameNetMessage}
	 *            实例已经读取过部分数据，那么如果本值为false的话在副本中将不能直接读取到前面已经被读取过的数据。(无参数版方法
	 *            {@link #duplicate()}此参数为ture)
	 * @return 新的{@link GameNetMessage}副本
	 */
	GameNetMessage duplicate(boolean resetReadIndex);
	
	/**
	 * 对当前{@link GameNetMessage}实例做完全的克隆。！！{@link #duplicate()}方法跟{@link #copy()}
	 * 起到的效果是一样，但其实内部实现不同： {@link #duplicate()}方法生成出来的新的{@link GameNetMessage}
	 * 实例跟原实例的数据内容其实是共享的，只是各自维护一套indexs。 而{@link #copy()}是对原{@link GameNetMessage}
	 * 对象的完全的复制。所以一般情况下都使用{@link #duplicate()}方法，更省内存。
	 * 
	 * @return 新的{@link GameNetMessage}克隆
	 */
	GameNetMessage copy();
	
	/**
	 * Increases the current {@code readerIndex} by the specified {@code length}
	 * in this buffer.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if {@code length} is greater than {@code this.readableBytes}
	 */
	void skipBytes(int length);
	
	// ///////////////////////////////////Read/////////////////////////////////////////////
		/**
		 * Gets a byte at the current {@code readerIndex} and increases the
		 * {@code readerIndex} by {@code 1} in this buffer.
		 * 
		 * @throws IndexOutOfBoundsException
		 *             if {@code this.readableBytes} is less than {@code 1}
		 */
	byte readByte();
	
	/**
	 * Gets an unsigned byte at the current {@code readerIndex} and increases
	 * the {@code readerIndex} by {@code 1} in this buffer.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if {@code this.readableBytes} is less than {@code 1}
	 */
	short readUnsignedByte();
	
	
	short readShort();
	
	int readUnsignedShort();
	
	boolean readBoolean();
	
	int readInt();
	
	long readUnsignedInt();
	
	long readLong();
	
	char readChar();
	
	float readFloat();
	
	double readDouble();
	
	/**
	 * Transfers this buffer's data to the specified destination starting at the
	 * current {@code readerIndex} and increases the {@code readerIndex} by the
	 * number of the transferred bytes (= {@code dst.length}).
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if {@code dst.length} is greater than
	 *             {@code this.readableBytes}
	 */
	void readBytes(byte[] dst);
	
	/**
	 * Transfers this buffer's data to the specified destination starting at the
	 * current {@code readerIndex} and increases the {@code readerIndex} by the
	 * number of the transferred bytes (= {@code length}).
	 * 
	 * @param dstIndex
	 *            the first index of the destination
	 * @param length
	 *            the number of bytes to transfer
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the specified {@code dstIndex} is less than {@code 0}, if
	 *             {@code length} is greater than {@code this.readableBytes}, or
	 *             if {@code dstIndex + length} is greater than
	 *             {@code dst.length}
	 */
	void readBytes(byte[] dst, int dstIndex, int index);
	
	/**
	 * Transfers this buffer's data to the specified stream starting at the
	 * current {@code readerIndex}.
	 * 
	 * @param length
	 *            the number of bytes to transfer
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if {@code length} is greater than {@code this.readableBytes}
	 * @throws IOException
	 *             if the specified stream threw an exception during I/O
	 */
	void readBytes(OutputStream out, int length) throws IOException;
	
	/**
	 * 读取一个protobuf对象
	 * @param parser 负责转化数据的protobuf parser
	 * @return
	 */
	<T extends MessageLite> T readProtobufData(Parser<T> parser);
	
	/**
	 * 自定义的一种读取字符串封装方法:<br>
	 * {@code int len=readInt();}<br>
	 * {@code byte[] bs=new byte[len];}<br>
	 * {@code readBytes(bs);}<br>
	 * 
	 * @return UTF-8编码的字符串
	 */
	String readUTF8String();
	
	/////////////////////////////////////////////////////////////////////////////write//////////////////////////////////////////////////////////////////////////////
	
	void writeByte(int value);
	
	
	void writeShort(int value);
	
	void writeBoolean(boolean value);
	
	void writeInt(int value);
	
	void writeLong(int value);
	
	void writeChar(int value);
	
	void writeFloat(float value);
	
	void writeDouble(double value);
	
	void writeBytes(byte[] src);
	
	void writeBytes(byte[] src, int srcIndex, int length);
	
	void writeProtobuf(MessageLite protobuf);
	
	void writeProtobuf(MessageLite.Builder builder);
	
	void writeUTF8String(String utf8String);
	
	
	/////////////////////////////////////////////////////////////get & set//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	byte getByte(int index);
	
	short getUnsignedByte(int index);
	
	short getShort(int index);
	
	int getUnsignedShort(int index);
	
	int getInt(int index);
	
	long getUnsignedInt(int index);
	
	boolean getBoolean(int index);
	
	long getLong(int index);
	
	char getChar(int index);
	
	float getFloat(int index);
	
	double getDouble(int index);
	
	void getBytes(int index, byte[] dst);
	
	void getBytes(int index, byte[] dst, int dstIndex, int length);
	
	void getBytes(int index, OutputStream out, int length) throws IOException;
	
	byte[] gatProtobufData(int index);
	
	void setByte(int index, int value);
	
	void setShort(int index, int valeu);
	
	void setInt(int index, int value);
	
	void setLong(int index, long valeu);
	
	void setChar(int index, char value);
	
	void setFloat(int index, float value);
	
	void setDouble(int index, float value);
	
	void setBytes(int index, byte[] value);
	
	void setBytes(int index, byte[] value, int valueIndex, int length);
	
	int setBytes(int index, InputStream in, int length) throws IOException;
	
	int writerIndex();
	
	int readerIndex();
	
	int readableBytes();
	
	
	
	
	
	
	
	
	
}
