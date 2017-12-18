/**
 * 
 */
package top.lmoon.baiducloud.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;

import top.lmoon.baiducloud.constant.SysConstants;
import top.lmoon.baiducloud.service.BaiduCloudService;
import top.lmoon.baiducloud.service.BaiduCloudService.GetVcode;
import top.lmoon.baiducloud.service.ThreadPool;
import top.lmoon.baiducloud.util.Locker;
import top.lmoon.baiducloud.vo.BaiduCloudVcodeVO;
import top.lmoon.baiducloud.vo.FileInfoVO;
import top.lmoon.baiducloud.vo.InputVcodeVO;

/**
 * @author LMoon
 * @date 2017年12月12日
 * 
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//https://pan.baidu.com/s/1qXZHS08
	private JTextField urlText = new JTextField("http://pan.baidu.com/s/1kV3fVev");

	private JTextField pwdText = new JTextField("ni1w");

	private JTextArea textArea = new JTextArea(4, 30);

	private static MainFrame frame = new MainFrame();

	public static MainFrame getInstance() {
		return frame;
	}

	private MainFrame() {
		// MainFrame mFrame = getInstance();
		this.setTitle("Cracker");
		this.setIconImage(new ImageIcon(SysConstants.TITLE_IMG).getImage());
		this.setSize(690, 300);
		// this.addWindowListener(new CloseWindowListener());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 界面关闭方式
		// this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);// 显示的界面居中

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(northPanel(), BorderLayout.NORTH);
		contentPanel.add(southPanel(), BorderLayout.CENTER);
		this.setContentPane(contentPanel);
		this.setVisible(true);
	}

	/**
	 * @return
	 */
	private JPanel northPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 5,20,0));
//		panel.setPreferredSize(new Dimension(600, 100));

		JLabel urlLabel = new JLabel("百度网盘链接:");
		JLabel pwdLabel = new JLabel("提取码:");
		
		urlText.setPreferredSize(new Dimension(20, 5));
		urlText.setMinimumSize(new Dimension(20, 5));
		
		urlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		pwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		JButton dealButton = new JButton("GO");
		dealButton.addActionListener(new dealAction());

//		dealButton.setMaximumSize(new Dimension(5, 2));

		panel.add(urlLabel);
		panel.add(urlText);
		panel.add(pwdLabel);
		panel.add(pwdText);
		panel.add(dealButton);
		panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 3, 20));
		
		return panel;
	}

	/**
	 * @return
	 */
	private JPanel southPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		// textArea = new JTextArea(10, 60);
		// textArea.setAutoscrolls(true);
		// textArea.setEditable(false);
		// JTextPane textPane = new JTextPane();

		textArea = new JTextArea(4, 30);
		// textField.setText("fdfdfdf");
		textArea.setEditable(false);
		// textArea.setColumns(30);
		// textArea.setAutoscrolls(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		// southPanel.setMinimumSize(new Dimension(500, 200));

		panel.setPreferredSize(new Dimension(550, 170));
		// southPanel.setAutoscrolls(true);
		panel.add(new JScrollPane(textArea));
		return panel;
	}

	/**
	 * @author LMoon
	 * @date 2017年12月12日
	 * 
	 */
	private class dealAction implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String url = urlText.getText();
			String pwd = pwdText.getText();
			if (StringUtils.isBlank(url) || StringUtils.isBlank(pwd)) {
				return;
			}
			
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					// "http://pan.baidu.com/s/1kV3fVev", "ni1w"
					List<FileInfoVO> fileList = BaiduCloudService.downloadAndGetFile(url, pwd, new GetVcode() {
		
						@Override
						public InputVcodeVO get(BaiduCloudVcodeVO vo) {
							VcodeFrame downloadFrame = new VcodeFrame(getInstance(), vo);
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
					});
					if (fileList != null && !fileList.isEmpty()) {
						// 展示下载链接
						StringBuilder sb = new StringBuilder();
						for (FileInfoVO fi : fileList) {
							sb.append(fi.getFileName());
							sb.append(":");
							sb.append(SysConstants.LINE_SYMBOL);
							sb.append(fi.getFileUrl());
							sb.append(SysConstants.LINE_SYMBOL).append(SysConstants.LINE_SYMBOL);
						}
						textArea.setText(sb.toString());
					}
				}
			};
			ThreadPool.commonThreadPool.submit(runnable);
			
		}

	}

	public static void main(String[] args) {
		getInstance();
	}

}
