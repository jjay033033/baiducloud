/**
 * 
 */
package top.lmoon.baiducloud.vo;

/**
 * @author LMoon
 * @date 2017年12月12日
 * 
 */
public class FileInfoVO{
	private String fileName = "";
	private String fileUrl = "";
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	@Override
	public String toString() {
		return "FileInfo [fileName=" + fileName + ", fileUrl=" + fileUrl + "]";
	}
}
