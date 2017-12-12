/**
 * 
 */
package top.lmoon.baiducloud.vo;

import top.lmoon.baiducloud.util.VcodeUtil.VcodeResult;

/**
 * @author LMoon
 * @date 2017年12月12日
 * 
 */
public class InputVcodeVO {
	
	private VcodeResult vcodeResult;
	
	private String vcodeInput;

	public VcodeResult getVcodeResult() {
		return vcodeResult;
	}

	public void setVcodeResult(VcodeResult vcodeResult) {
		this.vcodeResult = vcodeResult;
	}

	public String getVcodeInput() {
		return vcodeInput;
	}

	public void setVcodeInput(String vcodeInput) {
		this.vcodeInput = vcodeInput;
	}

	@Override
	public String toString() {
		return "VcodeInfoVO [vcodeResult=" + vcodeResult + ", vcodeInput=" + vcodeInput + "]";
	}

}
