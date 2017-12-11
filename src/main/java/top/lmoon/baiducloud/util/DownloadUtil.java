package top.lmoon.baiducloud.util;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import top.lmoon.baiducloud.constant.SysConstants;

public class DownloadUtil {
	
	public static Map<Integer,ImageIcon> map = new HashMap<>();
	
	static{
		init();
	}
	
	private static void init() {
		for(int i=0;i<5;i++){
			map.put(i, new ImageIcon(new ImageIcon(SysConstants.IMG_DOWNLOAD_TYPE_PATH + i + ".png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT)));
		}
	}

	public static enum downloadType {
		INITIAL(0, "未下载"), NOTSTART(1, "未开始"), ONGOING(2, "下载中"), UNFINISHED(3, "下载失败"), FINISHED(4, "下载成功");

		private int value;
		private String name;

		downloadType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int get() {
			return value;
		}

		public String getName() {
			return name;
		}

		public ImageIcon getImg() {
			return map.get(value);
		}

	}

	public static downloadType getDownloadType(int value) {
		switch (value) {
		case 0:
			return downloadType.INITIAL;
		case 1:
			return downloadType.NOTSTART;
		case 2:
			return downloadType.ONGOING;
		case 3:
			return downloadType.UNFINISHED;
		case 4:
			return downloadType.FINISHED;
		default:
			return downloadType.INITIAL;
		}
	}

	

}
