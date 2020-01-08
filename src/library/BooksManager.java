package library;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class BooksManager extends JFrame implements ActionListener {
	// 下面为8个功能菜单项
	private JMenuItem insertMenuItem; // 录入图书菜单项
	private JMenuItem deleteMenuItem; // 删除图书菜单项
	private JMenuItem updateMenuItem; // 修改图书信息菜单项
	private JMenuItem selectMenuItem; // 检索图书菜单项
	private JMenuItem lendMenuItem; // 开始借书菜单项
	private JMenuItem lendRecordMenuItem; // 借书记录菜单项
	private JMenuItem returnMenuItem; // 开始还书菜单项
	private JMenuItem returnRecordMenuItem; // 还书记录菜单项
	private JMenuItem exitloadMenuItem;

	private JInternalFrame titleFrame; // 提供很多本机窗体功能的轻量级对象，这些功能包括拖动、关闭、变成图标、调整大小、标题显示和支持菜单栏。
	private JDesktopPane desktop = new JDesktopPane(); // 用于创建多文档界面或虚拟桌面的容器
	private long userId;// 学号：用于用户标识
	// 开始初始化界面

	public BooksManager(long userId) {
		super("图书管理信息系统"); // 窗口标题
		this.userId = userId; // 得到传递过来的学号
		// 创建菜单条、菜单、菜单项对象
		JMenuBar menubar = new JMenuBar();
		JMenu manageMenu = new JMenu("图书管理");
		JMenu lendMenu = new JMenu("借阅图书");
		JMenu returnMenu = new JMenu("归还图书");
		JMenu exitMenu = new JMenu("退出登录");
		insertMenuItem = new JMenuItem("录入图书");
		deleteMenuItem = new JMenuItem("删除图书");
		updateMenuItem = new JMenuItem("修改图书信息");
		selectMenuItem = new JMenuItem("检索图书");
		lendMenuItem = new JMenuItem("开始借书");
		lendRecordMenuItem = new JMenuItem("借阅记录");
		returnMenuItem = new JMenuItem("开始还书");
		returnRecordMenuItem = new JMenuItem("还书记录");
		exitloadMenuItem = new JMenuItem("退出");
		// 把菜单项添加到对应exitloadMenuItem的菜单里，把菜单添加到菜单条里
		returnMenu.add(returnMenuItem);
		returnMenu.add(returnRecordMenuItem);
		lendMenu.add(lendMenuItem);
		lendMenu.add(lendRecordMenuItem);
		manageMenu.add(insertMenuItem);
		manageMenu.add(deleteMenuItem);
		manageMenu.add(updateMenuItem);
		manageMenu.add(selectMenuItem);
		exitMenu.add(exitloadMenuItem);
		menubar.add(manageMenu);
		menubar.add(lendMenu);
		menubar.add(returnMenu);
		menubar.add(exitMenu);
		// 设置三个菜单内容的字体样式及颜色，粗体，20，蓝色
		manageMenu.setFont(new Font("", Font.BOLD, 20));
		lendMenu.setFont(new Font("", Font.BOLD, 20));
		returnMenu.setFont(new Font("", Font.BOLD, 20));
		exitMenu.setFont(new Font("", Font.BOLD, 20));
		
		manageMenu.setForeground(Color.BLUE);
		lendMenu.setForeground(Color.BLUE);
		returnMenu.setForeground(Color.BLUE);
		exitMenu.setForeground(Color.BLUE);
		
		insertMenuItem.setFont(new Font("", Font.BOLD, 20));
		deleteMenuItem.setFont(new Font("", Font.BOLD, 20));
		updateMenuItem.setFont(new Font("", Font.BOLD, 20));
		selectMenuItem.setFont(new Font("", Font.BOLD, 20));
		lendMenuItem.setFont(new Font("", Font.BOLD, 20));
		lendRecordMenuItem.setFont(new Font("", Font.BOLD, 20));
		returnMenuItem.setFont(new Font("", Font.BOLD, 20));
		returnRecordMenuItem.setFont(new Font("", Font.BOLD, 20));
		exitloadMenuItem.setFont(new Font("", Font.BOLD, 20));

		this.setJMenuBar(menubar); // 把菜单条添加到窗体中
		this.setSize(1000, 800); // 设置窗体大小
		Container con = this.getContentPane(); // 返回此窗体的 contentPane 对象 。
		con.add(desktop); // 把虚拟桌面添加到此容器中
		JLabel label = new JLabel("欢迎您使用图书管理信息系统", JLabel.CENTER); // 主界面的欢迎信息
		label.setFont(new Font("隶书", Font.BOLD, 50)); // 信息的字体样式
		label.setForeground(Color.BLUE); // 信息的字体为蓝色
		titleFrame = new JInternalFrame(null, true); // 创建多窗体对象，无标题，不可关闭
		Container c = titleFrame.getContentPane(); // 得到此多窗体对象
		c.add(label, BorderLayout.CENTER); // 把欢迎内容添加到多窗体中
		titleFrame.setSize(990, 740); // 设置多窗体的大小
		desktop.add(titleFrame); // 把多窗体添加到虚拟桌面
		titleFrame.setVisible(true); // 设置多窗体可见
		titleFrame.getContentPane().setBackground(Color.PINK);
		this.setLocationRelativeTo(null);// 主界面居中
		this.setResizable(false); // 主界面大小不可改变
		this.setVisible(true); // 主界面可见
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 释放主界面窗口

		Service.windowClose(this);// 主界面关闭窗口事件
		// 为各个菜单项添加动作监听事件
		insertMenuItem.addActionListener(this);
		deleteMenuItem.addActionListener(this);
		updateMenuItem.addActionListener(this);
		selectMenuItem.addActionListener(this);
		lendMenuItem.addActionListener(this);
		lendRecordMenuItem.addActionListener(this);
		returnMenuItem.addActionListener(this);
		returnRecordMenuItem.addActionListener(this);
		exitloadMenuItem.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource(); // 最初发生的Event对象，返回Object
		if (source == insertMenuItem) {// 如果点击的是录入图书菜单项
			InsertFrame insertFrame = new InsertFrame(userId);// 创建录入图书界面
			this.dispose();// 释放由此 Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源。
		}
		if (source == deleteMenuItem) {// 如果点击的是删除图书菜单项
			DeleteFrame deleteFrame = new DeleteFrame(userId); // 删除图书
			this.dispose();
			// 添加删除图书界面关闭监听事件
		}
		if (source == updateMenuItem) {// 如果点击的是修改图书信息菜单项
			UpdateFrame1 updateFrame = new UpdateFrame1(userId); // 修改图书信息
			this.dispose(); // 释放由此 Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源。
			// 添加修改图书信息界面关闭监听事件

		}
		if (source == selectMenuItem) {// 如果点击的是检索图书菜单项
			SelectFrame selectFrame = new SelectFrame(userId);// 查询图书
			this.dispose();
			// 添加检索图书界面关闭监听事件
		}
		if (source == lendMenuItem) {// 如果点击的是开始借书菜单项
			LendFrame lendFrame = new LendFrame(userId);// 开始借书
			this.dispose();
		}
		if (source == lendRecordMenuItem) {// 如果点击的是借书记录菜单项
			LendRecordFrame lendRecordFrame = new LendRecordFrame(userId); // 借书记录
			this.dispose();
			// 添加借书记录界面关闭监听事件
		}
		if (source == returnMenuItem) {// 如果点击的开始还书菜单项
			ReturnFrame returnFrame = new ReturnFrame(userId); // 开始还书
			this.dispose();
			// 添加开始还书界面关闭监听事件
		}
		if (source == returnRecordMenuItem) {// 如果点击的是还书记录菜单项
			ReturnRecordFrame returnRecordFrame = new ReturnRecordFrame(userId);
			// 还书记录
			this.dispose();
			// 添加还书记录界面关闭监听事件
		}
		if(source == exitloadMenuItem) {
			LoginManager lm = new LoginManager();
			this.dispose();
			// 添加退出登录界面,关闭监听事件
		}
	}
}
