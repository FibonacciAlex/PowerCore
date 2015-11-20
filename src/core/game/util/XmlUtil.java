package core.game.util;

import java.io.File;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * xml操作工具类，使用jdom.jar包
 * @author Alex
 * 2015年7月21日 下午5:52:04
 */
public class XmlUtil {

	
	public static Document openXml(String xml){
		return openXml(new File(xml));
	}
	
	
	
	public static Document openXml(File xml){
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			
			doc = sb.build(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
}



















