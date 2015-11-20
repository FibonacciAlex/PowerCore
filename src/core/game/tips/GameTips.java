package core.game.tips;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import core.game.util.StringUtil;
import core.game.util.XmlUtil;

/**
 * 系统提示信息
 * @author Alex
 * 2015年7月21日 下午5:03:33
 */
public class GameTips {

	private final static Map<String, String> tips = new HashMap<String, String>();
	private static File tipsFile;
	
	private static long lastModifyTime;
	
	public static void load(){
		String xmlFilePath = "res/config/Tips.xml";
		tipsFile = new File(xmlFilePath);
		lastModifyTime = tipsFile.lastModified();
		loadTipsData();
	}

	@SuppressWarnings("unchecked")
	private static void loadTipsData() {
		Document doc = XmlUtil.openXml(tipsFile);
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren("tips");
		for (Element e : list) {
			String key = e.getAttributeValue("key");
			String value = e.getTextTrim();
			tips.put(key, value);
		}
		
	}
	
	
	public static String getTips(String key){
		String v = tips.get(key);
		return v == null ? "" : v;
	}
	
	/**
	 * 获取一个tips，并组装对应的参数
	 * @param key
	 * @param args
	 * @return
	 */
	public static String get(String key, Object... args){
		String v = getTips(key);
		return StringUtil.format(v, args);
	}
	
	
	public static void startScanTipsFile(){
		
	}
}
