/**
 * 
 */
package top.lmoon.baiducloud.vo;

import top.lmoon.baiducloud.util.DownloadUtil.downloadType;

/**
 * @author LMoon
 * @date 2017年9月30日
 * 
 */
public class ApeInfoVO {
	
	private int songId;
	private int singerId;
	private int songIdForSinger;
	private String singer;
	private String title;
	private String link;
	private String pw;
	private String album;
	private String size;
	private String language;
	private String remark;	
	private String url;
	
	private String name;
	private downloadType downType;
	private long createTime;
	private long updateTime;

	
	public ApeInfoVO(){}
	
	public ApeInfoVO(int songId, int singerId, int songIdForSinger, String singer, String title, String link,
			String pw, String album, String size, String language, String remark,String url) {
		super();
		long currentTimeMillis = System.currentTimeMillis();
		this.songId = songId;
		this.singerId = singerId;
		this.songIdForSinger = songIdForSinger;
		this.singer = singer;
		this.title = title;
		this.link = link;
		this.pw = pw;
		this.album = album;
		this.size = size;
		this.language = language;
		this.remark = remark;
		this.url = url;
		this.name = "";
		this.downType = downloadType.INITIAL;
		this.createTime = currentTimeMillis;
		this.updateTime = currentTimeMillis;
	}
	
	public ApeInfoVO(int songId, int singerId, int songIdForSinger, String singer, String title, String link, String pw,
			String album, String size, String language, String remark, String url, String name, downloadType downType,
			long createTime, long updateTime) {
		super();
		this.songId = songId;
		this.singerId = singerId;
		this.songIdForSinger = songIdForSinger;
		this.singer = singer;
		this.title = title;
		this.link = link;
		this.pw = pw;
		this.album = album;
		this.size = size;
		this.language = language;
		this.remark = remark;
		this.url = url;
		this.name = name;
		this.downType = downType;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public downloadType getDownType() {
		return downType;
	}

	public void setDownType(downloadType downType) {
		this.downType = downType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getSongId() {
		return songId;
	}
	public void setSongId(int songId) {
		this.songId = songId;
	}
	public int getSingerId() {
		return singerId;
	}
	public void setSingerId(int singerId) {
		this.singerId = singerId;
	}
	public int getSongIdForSinger() {
		return songIdForSinger;
	}
	public void setSongIdForSinger(int songIdForSinger) {
		this.songIdForSinger = songIdForSinger;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ApeInfoVO [songId=" + songId + ", singerId=" + singerId + ", songIdForSinger=" + songIdForSinger
				+ ", singer=" + singer + ", title=" + title + ", link=" + link + ", pw=" + pw + ", album=" + album
				+ ", size=" + size + ", language=" + language + ", remark=" + remark + ", url=" + url + ", name=" + name
				+ ", downType=" + downType + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
