/**
 * 
 */
package top.lmoon.baiducloud.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 提供通过HTTP协议获取内容的方法 <br/>
 * 所有提供方法中的params参数在内部不会进行自动的url encode，如果提交参数需要进行url encode，请调用方自行处理
 * 
 * @author wuhuoxin
 */
public class HttpUtil {
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(HttpUtil.class);

	private static final int connectTimeout = 5000;
	private static final int readTimeout = 5000;
	private static final String charset = "UTF-8";

	/**
	 * 支持的Http method
	 *
	 */
	private static enum HttpMethod {
		POST, DELETE, GET, PUT, HEAD;
	};

	private static String invokeUrl(String url, Map params, Map formParams, Map<String, String> headers,
			int connectTimeout, int readTimeout, String encoding, HttpMethod method) {
		return invokeUrl(url, params, formParams, headers, connectTimeout, readTimeout, encoding, method, false);
	}

	private static String invokeUrl(String url, Map params, Map formParams, Map<String, String> headers,
			int connectTimeout, int readTimeout, String encoding, HttpMethod method, boolean returnCookies) {
		// 构造请求参数字符串
		StringBuilder paramsStr = null;
		StringBuilder formParamsStr = null;
		if (params != null) {
			paramsStr = new StringBuilder();
			Set<Map.Entry> entries = params.entrySet();
			for (Map.Entry entry : entries) {
				String value = (entry.getValue() != null) ? (String.valueOf(entry.getValue())) : "";
				paramsStr.append(entry.getKey() + "=" + value + "&");
			}
			// 只有POST方法才能通过OutputStream(即form的形式)提交参数
			// if(method != HttpMethod.POST){
			url += "?" + paramsStr.toString();
			// }
		}

		if (formParams != null) {
			formParamsStr = new StringBuilder();
			Set<Map.Entry> entries = formParams.entrySet();
			for (Map.Entry entry : entries) {
				String value = (entry.getValue() != null) ? (String.valueOf(entry.getValue())) : "";
				formParamsStr.append(entry.getKey() + "=" + value + "&");
			}
		}

		URL uUrl = null;
		HttpURLConnection conn = null;
		BufferedWriter out = null;
		BufferedReader in = null;
		try {
			// 创建和初始化连接
			uUrl = new URL(url);
			System.out.println("----------url:" + url);
			System.out.println("----------formParamsStr:" + formParamsStr);
			conn = (HttpURLConnection) uUrl.openConnection();
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			conn.setRequestMethod(method.toString());
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置连接超时时间
			conn.setConnectTimeout(connectTimeout);
			// 设置读取超时时间
			conn.setReadTimeout(readTimeout);
			

			// 指定请求header参数
			if (headers != null && headers.size() > 0) {
				Set<String> headerSet = headers.keySet();
				for (String key : headerSet) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}

			if (formParamsStr != null && method == HttpMethod.POST) {
				// 发送请求参数
				out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), encoding));
				out.write(formParamsStr.toString());
				out.flush();
			}
//			System.out.println(conn.getResponseMessage());
			if (returnCookies) {
				System.out.println(conn.getHeaderField("set-cookie"));
				return conn.getHeaderField("set-cookie");
			}
			
			// 接收返回结果
			StringBuilder result = new StringBuilder();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			if (in != null) {
				String line = "";
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
			}
			System.out.println(result.toString());
			return result.toString();
		} catch (Exception e) {
			logger.error("调用接口[" + url + "]失败！请求URL：" + url + "，参数：" + params, e);
			// 处理错误流，提高http连接被重用的几率
			try {
				byte[] buf = new byte[100];
				InputStream es = conn.getErrorStream();
				if (es != null) {
					while (es.read(buf) > 0) {
						;
					}
					es.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 关闭连接
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

	/**
	 * POST方法提交Http请求，语义为“增加” <br/>
	 * 注意：Http方法中只有POST方法才能使用body来提交内容
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String post(String url, Map params, Map formParams, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, formParams, null, connectTimeout, readTimeout, charset, HttpMethod.POST);
	}

	public static String post(String url, Map params, Map formParams) {
		return post(url, params, formParams, connectTimeout, readTimeout, charset);
	}

	public static String postForBaiduCookies(String url, Map params, Map formParams, Map<String, String> headers) {
		return invokeUrl(url, params, formParams, headers, connectTimeout, readTimeout, charset, HttpMethod.POST, true);
	}

	/**
	 * POST方法提交Http请求，语义为“增加” <br/>
	 * 注意：Http方法中只有POST方法才能使用body来提交内容
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String post(String url, Map params, Map formParams, Map<String, String> headers, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, formParams, headers, connectTimeout, readTimeout, charset, HttpMethod.POST);
	}

	/**
	 * GET方法提交Http请求，语义为“查询”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String get(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return get(url, params, null, connectTimeout, readTimeout, charset);
	}

	/**
	 * GET方法提交Http请求，语义为“查询”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String get(String url, Map params, Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, null, headers, connectTimeout, readTimeout, charset, HttpMethod.GET);
	}

	public static String get(String url) {
		return get(url, null, connectTimeout, readTimeout, charset);
	}

	/**
	 * PUT方法提交Http请求，语义为“更改” <br/>
	 * 注意：PUT方法也是使用url提交参数内容而非body，所以参数最大长度收到服务器端实现的限制，Resin大概是8K
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String put(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, null, connectTimeout, readTimeout, charset, HttpMethod.PUT);
	}

	/**
	 * PUT方法提交Http请求，语义为“更改” <br/>
	 * 注意：PUT方法也是使用url提交参数内容而非body，所以参数最大长度收到服务器端实现的限制，Resin大概是8K
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String put(String url, Map params, Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, null, headers, connectTimeout, readTimeout, charset, HttpMethod.PUT);
	}

	/**
	 * DELETE方法提交Http请求，语义为“删除”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String delete(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, null, connectTimeout, readTimeout, charset, HttpMethod.DELETE);
	}

	/**
	 * DELETE方法提交Http请求，语义为“删除”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String delete(String url, Map params, Map<String, String> headers, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, null, headers, connectTimeout, readTimeout, charset, HttpMethod.DELETE);
	}

	/**
	 * HEAD方法提交Http请求，语义同GET方法 <br/>
	 * 跟GET方法不同的是，用该方法请求，服务端不返回message body只返回头信息，能节省带宽
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String head(String url, Map params, int connectTimeout, int readTimeout, String charset) {
		return invokeUrl(url, params, null, null, connectTimeout, readTimeout, charset, HttpMethod.HEAD);
	}

	/**
	 * HEAD方法提交Http请求，语义同GET方法 <br/>
	 * 跟GET方法不同的是，用该方法请求，服务端不返回message body只返回头信息，能节省带宽
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String head(String url, Map params, Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, null, headers, connectTimeout, readTimeout, charset, HttpMethod.HEAD);
	}

	public static boolean download(String urlStr, String fileName) {
		System.out.println(urlStr);
		System.out.println(fileName);
		// 下载网络文件
		int bytesum = 0;
		int byteread = 0;
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			// conn.setConnectTimeout(100000);
			// conn.setReadTimeout(100000);
			inStream = conn.getInputStream();
			fs = new FileOutputStream(fileName);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			System.out.println(bytesum);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		} finally {
			try {
				if (fs != null) {
					fs.close();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void main(String[] args) {
		// download("https://pan.baidu.com/genimage?33324238656332346361663334656637323237633636373637643239666664336662343330393135363338303030303030303030303030303031353038393132353335E409471BF6D09C6E8853D11AFC018DC8",
		// "./res/music/a.png");
		download(
				"https://d.pcs.baidu.com/file/5c4ec96e0602ad9d4f5fe775e8f6ed89?fid=660976564-250528-677635646695251&time=1508925401&rt=sh&sign=FDTAERV-DCb740ccc5511e5e8fedcff06b081203-nS6u7fjgOaiontHvrQFmmx%2FUWKM%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=6905609891134183393&dp-callid=0&r=588511371",
				"./res/music/张赫宣-会哭的人不一定流泪2.flac");
	}

}
