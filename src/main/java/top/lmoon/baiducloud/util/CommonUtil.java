/**
 * 
 */
package top.lmoon.baiducloud.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author LMoon
 * @date 2017年9月30日
 * 
 */
public class CommonUtil {
	
	private static final String BAIDUCLOUD_REGEX = "http(s)?://pan.baidu.com/s/1(.*)";
	
	public static boolean isBaiduCloudUrl(String url){
		return url.matches(BAIDUCLOUD_REGEX);
	}
	
	public static String getBaiduCloudSUrl(String url){
		Pattern p = Pattern.compile(BAIDUCLOUD_REGEX);
		Matcher m = p.matcher(url);
		if(m.find()){
			return m.group(2);
		}
		return "";
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 替换换行、换列符
	 * @param str
	 * @return
	 */
	public static String replaceJavaSpecilChar(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		char[] cs = str.toCharArray();
		for (char c : cs) {
			switch (c) {
			case '\n':
			case '\t':
				sb.append(" ");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 效果等同String.replace("/", "&").replace("\\", "*");
	 * @param str
	 * @return
	 */
	public static String replacePathSpecilChar(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		char[] cs = str.toCharArray();
		for (char c : cs) {
			switch (c) {
			case '/':
				sb.append('&');
				break;
			case '\\':
				sb.append('*');
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

}
