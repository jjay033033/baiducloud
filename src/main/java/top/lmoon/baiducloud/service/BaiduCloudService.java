/**
 * 
 */
package top.lmoon.baiducloud.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.lmoon.baiducloud.constant.SysConstants;
import top.lmoon.baiducloud.util.CommonUtil;
import top.lmoon.baiducloud.util.HttpUtil;
import top.lmoon.baiducloud.util.VcodeUtil.VcodeResult;
import top.lmoon.baiducloud.vo.BaiduCloudVcodeVO;
import top.lmoon.baiducloud.vo.FileInfoVO;
import top.lmoon.baiducloud.vo.InputVcodeVO;

/**
 * @author LMoon
 * @date 2017年10月13日
 * 
 */

public class BaiduCloudService {

	private static final Logger logger = LoggerFactory.getLogger(BaiduCloudService.class);

	private final static int CLIENTTYPE = 0;
	private final static String CHANNEL = "chunlei";
	private final static int WEB = 1;
	private final static String APP_ID = "250528";

	static {
		init();
	}

	/**
	 * 
	 */
	private static void init() {
//		File downloadPath = new File(SysConstants.FILE_PATH);
//		File downloadTmpPath = new File(SysConstants.FILE_TMP_PATH);
//		if (!downloadPath.exists() || !downloadPath.isDirectory()) {
//			downloadPath.mkdirs();
//		}
//		if (!downloadTmpPath.exists() || !downloadTmpPath.isDirectory()) {
//			downloadTmpPath.mkdirs();
//		}
	}

	public static List<FileInfoVO> downloadAndGetFile(String url, String pwd, GetVcode gv) {
		return downloadAndGetFile(url, pwd, null, null, gv);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<FileInfoVO> downloadAndGetFile(String url, String pwd, String vcode_input, String vcode_str,
			GetVcode gv) {
		try {
			Connection connection = Jsoup.connect(url);
			connection.userAgent(
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			connection.header("Cookie",
					"PANWEB=1; BAIDUID=C45C3ACB0DAE46D34927F30EEC9A920F:FG=1; BDCLND=kR%2BvhmAQ63n%2Bps2G3R%2F6cAkJt2Pwk8NTgeQ2pzTGTyw%3D; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1507625685%2C1507625751%2C1507886431%2C1507887009; Hm_lpvt_7a3960b6f067eb0085b7f96ff5e660b0=1509099846");

			Map formParams = new HashMap<>();
			//
			if (StringUtils.isNotBlank(pwd)) {
				dealPwd(url, pwd, connection, formParams);
			}

			Document doc = connection.get();
			String html = doc.toString();

			String beginStr = "yunData.setData({";
			int a = html.indexOf(beginStr);
			int b = html.indexOf("})", a);

			String info = html.substring(a + beginStr.length() - 1, b + 1);
			System.out.println("------info:" + info);
			JSONObject jo = new JSONObject(info);
			System.out.println("------jo:" + jo.get("file_list"));
			if (jo.get("file_list") == null || jo.get("file_list").toString().equalsIgnoreCase("null")) {
				return null;
			}

			String sign = jo.getString("sign");
			long timestamp = jo.getLong("timestamp");
			// String bdstoken = jo.getString("bdstoken");
			String bdstoken = "";
			long uk = jo.getLong("uk");
			long primaryid = jo.getLong("shareid");

			JSONObject fileList = jo.getJSONObject("file_list");
			JSONArray ja = fileList.getJSONArray("list");

			List<Long> fid_list = new ArrayList<Long>();
			String app_id = "";

			for (int i = 0; i < ja.length(); i++) {
				JSONObject fileJo = ja.getJSONObject(i);
				app_id = fileJo.getString("app_id");
				fid_list.add(fileJo.getLong("fs_id"));
			}

			Map params = new HashMap<>();
			params.put("sign", sign);
			params.put("timestamp", timestamp);
			params.put("bdstoken", bdstoken);
			params.put("app_id", app_id);

			params.put("channel", CHANNEL);
			params.put("clienttype", CLIENTTYPE);
			params.put("web", WEB);

			formParams.put("uk", uk);
			formParams.put("primaryid", primaryid);
			formParams.put("fid_list", fid_list);
			formParams.put("encrypt", 0);
			formParams.put("product", "share");
			formParams.put("vcode_input", vcode_input);
			formParams.put("vcode_str", vcode_str);

			return download(bdstoken, app_id, params, formParams, gv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("", e);
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void dealPwd(String url, String pwd, Connection connection, Map finalFormParams) throws IOException {
		Map params = new HashMap<>();
		params.put("app_id", APP_ID);
		params.put("channel", CHANNEL);
		params.put("clienttype", CLIENTTYPE);
		params.put("web", WEB);
		params.put("surl", CommonUtil.getBaiduCloudSUrl(url));

		Map formParams = new HashMap<>();
		formParams.put("pwd", pwd);

		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
		headers.put("Cookie",
				"PANWEB=1; BAIDUID=C45C3ACB0DAE46D34927F30EEC9A920F:FG=1; BDCLND=kR%2BvhmAQ63n%2Bps2G3R%2F6cAkJt2Pwk8NTgeQ2pzTGTyw%3D; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1507625685%2C1507625751%2C1507886431%2C1507887009; Hm_lpvt_7a3960b6f067eb0085b7f96ff5e660b0=1509099846");
		headers.put("Host", "pan.baidu.com");
		headers.put("Origin", "https://pan.baidu.com");
		headers.put("Referer", "https://pan.baidu.com");

		String cookies = HttpUtil.postWithBaiduCookies("https://pan.baidu.com/share/verify", params, formParams,
				headers);
		if (cookies == null) {
			return;
		}
		int bdclndStart = cookies.indexOf("BDCLND=");
		int bdclndEnd = cookies.indexOf(";", bdclndStart);
		String bdclnd = cookies.substring(bdclndStart, bdclndEnd);
		String newCookies = headers.get("Cookie").replaceAll("BDCLND=[^;]*", bdclnd);
		connection.header("Cookie", newCookies);
		finalFormParams.put("extra", "{\"sekey\":\"" + bdclnd.substring(7) + "\"}");
	}

	/**
	 * @param bdstoken
	 * @param app_id
	 * @param params
	 * @param formParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<FileInfoVO> download(String bdstoken, String app_id, Map params, Map formParams, GetVcode gv) {
		String postResult = HttpUtil.post("https://pan.baidu.com/api/sharedownload", params, formParams);

		System.out.println("------postResult:" + postResult);

		JSONObject json_data = new JSONObject(postResult);
		int resultInt = json_data.getInt("errno");
		String fileUrl = "";
		String fileName = "";
		List<FileInfoVO> fileList = new ArrayList<>();
		if (resultInt == 0) {
			JSONArray jsonArray2 = json_data.getJSONArray("list");
			int size = jsonArray2.length();
			if (size == 1) {
				json_data = jsonArray2.getJSONObject(0);
				// 储存文件下载实链
				fileUrl = json_data.getString("dlink");
				fileName = json_data.getString("server_filename");
				if (StringUtils.isNotBlank(fileUrl)) {
					FileInfoVO fiVO = new FileInfoVO();
					fiVO.setFileName(fileName);
					fiVO.setFileUrl(fileUrl);
					fileList.add(fiVO);
				}
			} else if (size > 1) {
				StringBuffer fileNameSb = new StringBuffer();
				for (int i = 0; i < size; i++) {
					json_data = jsonArray2.getJSONObject(i);
					// 储存文件下载实链
					fileUrl = json_data.getString("dlink");
					fileName = json_data.getString("server_filename");
					if (StringUtils.isNotBlank(fileUrl)) {
						fileNameSb.append(fileName).append(",");
					}
				}
				for (int i = 0; i < size; i++) {
					json_data = jsonArray2.getJSONObject(i);
					// 储存文件下载实链
					fileUrl = json_data.getString("dlink");
					fileName = json_data.getString("server_filename");
					if (StringUtils.isNotBlank(fileUrl)) {
						FileInfoVO fiVO = new FileInfoVO();
						fiVO.setFileName(fileName);
						fiVO.setFileUrl(fileUrl);
						fileList.add(fiVO);
					}
				}
			}

		} else if (resultInt == -20) {
			// String getVCode();
			BaiduCloudVcodeVO vcodeInfo = null;
			InputVcodeVO inputVcodeVO = null;
			do {
				vcodeInfo = getVcodeInfo(bdstoken, app_id);
				if (vcodeInfo == null) {
					throw new NullPointerException("验证码获取失败！");
				}
				inputVcodeVO = gv.get(vcodeInfo);
			} while (inputVcodeVO.getVcodeResult() == VcodeResult.CHANGE);

			if (inputVcodeVO.getVcodeResult() == VcodeResult.FINISHED) {
				formParams.put("vcode_str", vcodeInfo.getVcode_str());
				formParams.put("vcode_input", inputVcodeVO.getVcodeInput());
				return download(bdstoken, app_id, params, formParams, gv);
			}
		}
		return fileList;
	}

	/**
	 * @param bdstoken
	 * @param app_id
	 */
	public static BaiduCloudVcodeVO getVcodeInfo(String bdstoken, String app_id) {
		BaiduCloudVcodeVO baiduCloudVcode = null;
		String vCodeGetUrl = "https://pan.baidu.com/api/getvcode?prod=pan&bdstoken=" + bdstoken + "&channel=" + CHANNEL
				+ "&clienttype=" + CLIENTTYPE + "&web=" + WEB + "&app_id=" + app_id;
		String result = HttpUtil.get(vCodeGetUrl);
		JSONObject jo = new JSONObject(result);
		if (jo.getInt("errno") == 0) {
			baiduCloudVcode = new BaiduCloudVcodeVO();
			baiduCloudVcode.setVcode_str(jo.getString("vcode"));
			baiduCloudVcode.setVcode_url(jo.getString("img"));
		}
		return baiduCloudVcode;
	}

	public interface GetVcode {
		InputVcodeVO get(BaiduCloudVcodeVO vo);
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(getUrl("https://pan.baidu.com/s/1qXZHS08"));
		System.out.println(downloadAndGetFile("http://pan.baidu.com/s/1kV3fVev", "ni1w", null));
	}
}