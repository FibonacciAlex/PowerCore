package core.game.util;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;


/**
 * <pre>
 * Logger方式，字符串格式化
 * </pre>
 * @author Alex
 * 2015年7月21日 下午8:55:46
 */
public class StringUtil {


	/**
	 * <pre>
	 * 字符串插入
	 * 例子：
	 * 1. format("Hi {}. {}","kola",520); 返回"Hi kola. 520"
	 * 2. 如果{}中间有内容就直接输出，不算是格式中的{}。如：format("Set{1,2,3} size={}",3); return "Set{1,2,3} size=3"
	 * 3. 如果本身就想输出"{}"那可以用"\\"作为转义符。如：format("\\{} {}","kola"); return "{} kola"
	 * 4. format(File name is C:\\\\{}., "file.zip"); 返回"File name is C:\file.zip"
	 * </pre>
	 * @param pattern   原字符串，内有{}标记
	 * @param args 多个参数，顺序插入替代pattern 里的 {} 标记
	 * @return
	 */
	public static String format(String pattern, Object... args){
		FormattingTuple ft = MessageFormatter.arrayFormat(pattern, args);
		return ft.getMessage();
	}
	
	
	
	public static boolean hasNullOrLengthString(String... args){
		for (String str : args) {
			if(str == null || str.length() <= 0){
				return true;
			}
		}
		return false;
	}
}
