package core.game;

public interface GameProtocol {
	
	/**
	 * 心跳消息
	 */
	public final static int MID_PING = 0;
	
	/**
	 * 握手消息
	 * 
	 * <pre>
	 * REQUEST:
	 *   String code;//密匙，如果不符合 服务器将判断为非法客户端并断开链接
	 *   String clientmodel;
	 * RESPONSE:
	 *   String serverinfo;//发回一些服务器信息（预留）
	 *   
	 * 【注：内部通信的HANDSHAKE内容是完全不同的，通过ClientType判断，游戏逻辑无须理会】
	 * </pre>
	 */
	public final static int MID_HANDSHAKE = 1;

	
	/**
	 *  版本检测（FE）- 【20130611更新了协议】【20130724更新了协议添加md5】
	 * 
	 * <pre>
	 * 请求PL:
	 *   int promoid;  //渠道ID
	 *   String appVer;//应用版本号（客户端和服务器协商自定义）
	 *   int resVer;//资源版本号（必须为整型值）
	 * 响应PL:
	 *   int responsecode = 
	 *     {@link #PL_VERCHECK_NEWEST}/ 
	 *     {@link #PL_VERCHECK_UPDATE_APK}/
	 *     {@link #PL_VERCHECK_UPDATE_APK_SYS}/
	 *     {@link #PL_VERCHECK_UPDATE_RESPACKS}/
	 *     {@link #PL_VERSIONFORMAT_ERROR}/
	 *     {@link #PL_ILLEGAL_SESSION_OR_MSG}/{@link #PL_UNKNOWNEXCEPTION_OR_SERVERBUSY}
	 *   String tips;//版本更新结果提示
	 *   if({@link #PL_VERCHECK_UPDATE_APK}){
	 *     String lastestAppVer; //当前最新版本号
	 *     long apkfilesize;     //apk文件大小
	 *     String apkdownloadurl;//下载地址
	 *     String md5;//20130724每个文件增加md5校验码，发给客户端自己对比
	 *   }
	 *   else if({@link #PL_VERCHECK_UPDATE_RESPACKS}){
	 *     int updatefileN;//需要更新的文件数量
	 *     for(updatefileN){
	 *       String filename;//文件名
	 *       int patchver;//版本号,为整型值
	 *       long filesize;//文件大小
	 *       String downloadurl;//HTTP下载地址
	 *       String md5;//20130724每个文件增加md5校验码，发给客户端自己对比
	 *     }
	 *   }
	 * </pre>
	 */
	public final static int MID_CHECKVERSION = 100;
	
	/** PL 版本检测结果-最新,无须更新 */
	public final static int PL_VERCHECK_NEWEST = 0;
	/** PL 版本检测结果-需要更新APK */
	public final static int PL_VERCHECK_UPDATE_APK = 1;
	/** PL 版本检测结果-需要更新APK，通知客户端由系统系在，而不是在应用内下载，主要是防止客户端出现问题而采取的紧急处理 办法 */
	public final static int PL_VERCHECK_UPDATE_APK_SYS = 4;
	/** PL 版本检测结果-需要更新资源包 */
	public final static int PL_VERCHECK_UPDATE_RESPACKS = 2;
	/** PL 版本检测结果-客户端版本号格式错误 */
	public final static int PL_VERSIONFORMAT_ERROR = 3;
	
	
	/**
	 * 做账号验证（FE）
	 * 
	 * <pre>
	 * 请求PL:
	 * String pName; 
	 * String pPassword
	 * 响应PL:
	 * String pName;
	 * int responsecode =
	 * {@link #PL_PASSPORT_VERIFY_SUCCEED} / 
	 * {@link #PL_PASSPORT_VERIFY_FAILED_NAMENOTEXIST} /
	 * {@link #PL_PASSPORT_VERIFY_FAILED_PASSWORDISWRONG}/
	 * {@link #PL_ILLEGAL_SESSION_OR_MSG}/{@link #PL_UNKNOWNEXCEPTION_OR_SERVERBUSY}
	 * String tips;//20130222添加的提示语，有可能是0长度客户端要注意
	 * 注意：如果responsecode==PL_PASSPORT_VERIFY_SUCCEED，后面将携带服务器列表数据
	 * </pre>
	 */
	public final static int MID_ACCOUNT_VERIFY = 200;
}
