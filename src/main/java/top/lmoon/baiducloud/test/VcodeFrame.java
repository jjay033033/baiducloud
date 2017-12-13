/**
 * 
 */
package top.lmoon.baiducloud.test;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.lmoon.baiducloud.constant.SysConstants;
import top.lmoon.baiducloud.service.BaiduCloudService;
import top.lmoon.baiducloud.service.BaiduCloudService.GetVcode;
import top.lmoon.baiducloud.util.Locker;
import top.lmoon.baiducloud.util.VcodeUtil.VcodeResult;
import top.lmoon.baiducloud.vo.BaiduCloudVcodeVO;
import top.lmoon.baiducloud.vo.InputVcodeVO;

/**
 * @author LMoon
 * @date 2017年10月25日
 * 
 */
public class VcodeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(VcodeFrame.class);
	
	private JTextField text;
	
	private JLabel vcodePic;
	
	private BaiduCloudVcodeVO baiduCloudVcodeVO;
	
	private InputVcodeVO inputVcodeVO;
	
//	public static final Object lock = new Object();

	public VcodeFrame(Component c,BaiduCloudVcodeVO vo) {
		inputVcodeVO = new InputVcodeVO();
		inputVcodeVO.setVcodeResult(VcodeResult.DEFAULT);

		this.baiduCloudVcodeVO = vo;
		this.setTitle("VCode input");
		this.setIconImage(new ImageIcon(SysConstants.TITLE_IMG).getImage());
		this.setSize(250, 150);
		// this.addWindowListener(new CloseWindowListener());
		// mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//界面关闭方式
		// this.setLocationRelativeTo(null);// 显示的界面居中
		this.setLocationRelativeTo(c);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(2, 2, 10, 10));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		// contentPanel.setPreferredSize(new Dimension(100, 200));

		text = new JTextField();
		
		vcodePic = new JLabel();
		if(!setVCodePic()){
			return;
		}

		JButton changeButton = new JButton("换个");
		changeButton.addActionListener(new changeAction());	

		JButton checkButton = new JButton("验证");
		checkButton.addActionListener(new checkAction());

		contentPanel.add(vcodePic);
		contentPanel.add(changeButton);
		contentPanel.add(text);
		contentPanel.add(checkButton);
		
		this.setContentPane(contentPanel);
		this.setVisible(true);
		
	}
	
	public InputVcodeVO getInputVcodeVO(){
		return inputVcodeVO;
	}

	/**
	 * @author LMoon
	 * @date 2017年10月25日
	 * 
	 */
	public class changeAction implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			inputVcodeVO.setVcodeResult(VcodeResult.CHANGE);
			synchronized (Locker.vcodeLock) {
				Locker.vcodeLock.notify();
			}
			setVisible(false);
		}

	}
	
	private boolean setVCodePic(){
		try {
			vcodePic.setIcon(new ImageIcon(new URL(baiduCloudVcodeVO.getVcode_url())));
			text.setText("");
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.error("",e);
			return false;
		}
	}

	/**
	 * @author LMoon
	 * @date 2017年10月25日
	 * 
	 */
	public class checkAction implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String vcode = text.getText();
			inputVcodeVO.setVcodeInput(vcode);
			inputVcodeVO.setVcodeResult(VcodeResult.FINISHED);	
			synchronized (Locker.vcodeLock) {
				Locker.vcodeLock.notify();
			}
			setVisible(false);
		}

	}
	
	public static void main(String[] args) {
		// System.out.println(getUrl("https://pan.baidu.com/s/1qXZHS08"));
		System.out.println(BaiduCloudService.downloadAndGetFile("http://pan.baidu.com/s/1kV3fVev", "ni1w",new GetVcode() {
			
			@Override
			public InputVcodeVO get(BaiduCloudVcodeVO vo) {
				VcodeFrame downloadFrame = new VcodeFrame(null, vo);
				synchronized (Locker.vcodeLock) {
					try {
						Locker.vcodeLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				InputVcodeVO iVo = downloadFrame.getInputVcodeVO();
				return iVo;
			}
		}));

	}

}
