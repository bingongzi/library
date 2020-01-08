package library;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class LoginManager{
	// 定义登录界面需要的基本组件，一个窗体三个标签，一个文本字段（用户名），一个口令字段（密码），一个按钮（登录）,一个按钮(注册)
	private JFrame jframe1;
	private JLabel showLabel;
	private JLabel userLabel;
	private JLabel passwordLabel;
	private JTextField userField;
	private JPasswordField passwordField;
	private JButton login;
	private JButton registe;
	private String str = "";
//	private ImageIcon img = new ImageIcon("C:\\Users\\DELL\\Desktop\\Java笔记\\1.jpg");//
//	private JLabel label = new JLabel(img);
	public static void main(String[] args) {
		// 执行程序
		new LoginManager();
	}
	
	// 对窗体进行初始化
	public LoginManager() {
		jframe1 = new JFrame("欢迎您来到图书管理系统");
		jframe1.setLayout(null); // 设置无布局格式
		// 设置窗体位置及大小(下面一段代码实现居中)
		jframe1.setSize(1000, 800);
		// 方法一
		// Toolkit toolKit = Toolkit.getDefaultToolkit(); // 获取默认工具包
		// Dimension dimension = toolKit.getScreenSize(); // 获取屏幕对象
		// double screenWidth = dimension.getWidth(); // 获取屏幕的宽和高
		// double screenHight = dimension.getHeight();
		// int jframeWidth = this.getWidth(); // 获取窗体的宽和高
		// int jframeHight = this.getHeight();
		// int x = (int) ((screenWidth - jframeWidth) / 2); // 得到窗体真正位置
		// int y = (int) ((screenHight - jframeHight) / 2);
		// jframe1.setLocation(x, y);
		// 方法二
		jframe1.setLocationRelativeTo(null);
		
		// 创建基本组件对象
		showLabel = new JLabel("图书管理系统登录界面");
		userLabel = new JLabel("用户名");
		passwordLabel = new JLabel("密码");
		userField = new JTextField(15);
		passwordField = new JPasswordField(15);
		passwordField.setEchoChar('*'); // 设置此 JPasswordField 的回显字符为*
		login = new JButton("登录");
		login.addActionListener(new loginListener()); // 注册：登录按钮动作事件
		registe = new JButton("注册");
		registe.addActionListener(new regisiteListener1());// 注册：注册按钮动作事件

		showLabel.setBounds(300, 50, 600, 80); // 设置showLabel显示位置
		userLabel.setBounds(210, 250, 130, 130); // 设置"用户名"显示位置
		passwordLabel.setBounds(210, 355, 130, 130); // 设置"密码"显示位置
		userField.setBounds(350, 280, 450, 60); // 设置文本字段显示位置
		passwordField.setBounds(350, 390, 450, 60); // 设置口令字段显示位置
		login.setBounds(660, 550, 140, 70); // 设置登录按钮显示位置
		registe.setBounds(200, 550, 140, 70); // 设置注册按钮显示位置
		// 把这些基本组件添加到窗体中
		jframe1.add(showLabel);
		jframe1.add(userLabel);
		jframe1.add(passwordLabel);
		jframe1.add(userField);
		jframe1.add(passwordField);
		jframe1.add(login);
		jframe1.add(registe);
		// 设置组件里的字体样式
		showLabel.setFont(new Font("", Font.BOLD, 40));
		showLabel.setForeground(Color.BLUE);
		userLabel.setFont(new Font("", Font.BOLD, 30));
		passwordLabel.setFont(new Font("", Font.BOLD, 30));
		userField.setFont(new Font("", Font.BOLD, 30));
		passwordField.setFont(new Font("", Font.BOLD, 30));
		login.setFont(new Font("", Font.BOLD, 30));
		registe.setFont(new Font("", Font.BOLD, 30));
//		setBg();
		Service.windowClose(jframe1); // 添加窗体关闭事件
		// loginManager.pack(); //调整此窗口的大小，以适合其子组件的首选大小和布局
		jframe1.setResizable(false); // 设置窗体不可放大
		jframe1.setVisible(true); // 使窗体可见
		jframe1.getContentPane().setBackground(Color.PINK);
		jframe1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 释放窗体
	}
//	public void setBg() {
//		label.setBounds(0,0, 1000,800);
//		jframe1.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
//		JPanel jp = (JPanel)jframe1.getContentPane();
//		jp.setOpaque(false);
//	}
	// 重写登录按钮动作事件监听方法
	public class loginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String userName = userField.getText().trim(); // 获取文本字段里的内容（账号）
			String password = MD5.GetMD5Code(passwordField.getText().trim());// 获取口令字段里的内容进行加密（密码）
			long id = Service.login(userName, password); // 作为用户标识号，核对登录信息
			if (id > 0) {// 在数据库中可以匹配
				BooksManager app = new BooksManager(id); // 创建主界面，返回用户标识号
				jframe1.dispose();
				; // 释放由此 Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源。
			} else if (id == 0) {// 与数据库没有完全匹配
				JOptionPane.showMessageDialog(jframe1, "您输入的用户名或密码不正确");
			} else {// 与数据库完全不匹配
				JOptionPane.showMessageDialog(jframe1, "查询用户表出错！");
			}
		}
	}

	// 重写注册按钮动作事件监听方法
	public class regisiteListener1 implements ActionListener {
		private JFrame jframe2 = new JFrame("欢迎您来到图书管理系统"); // 窗体
		private JButton backButton = new JButton("←"); // 返回按钮
		private JLabel showLabel1 = new JLabel("图书管理系统注册界面", JLabel.CENTER); // 显示提示信息1
		private JLabel userName = new JLabel("学号:"); // 显示"用户名"字体信息
		private JLabel setPassword = new JLabel("设置密码:"); // 显示"设置密码"字体信息
		private JLabel surePassword = new JLabel("确认密码:"); // 显示"确认密码"字体信息
		private JLabel name = new JLabel("姓名:"); // 显示"姓名"字体信息
		private JLabel sex = new JLabel("性别:"); // 显示"性别"字体信息
		private JLabel birthday = new JLabel("出生日期:"); // 显示"出生日期内"字体信息
		private JLabel showLabel2 = new JLabel("*密码由6-12位数字、字符组成*"); // 显示提示信息2（密码）
		private JLabel showLabel3 = new JLabel("格式：(****-**-**)"); // 显示提示信息3（出生日期）
		private JTextField text1 = new JTextField(15); // 显示用户名对应的文本框
		private JPasswordField text2 = new JPasswordField(15); // 显示设置密码对应的文本框

		private JPasswordField text3 = new JPasswordField(15); // 显示确认密码对应的文本框
		private JTextField text4 = new JTextField(15); // 显示姓名对应的文本框
		// private JTextField text5 = new JTextField(15); // 显示性别对应的文本框
		private JRadioButton radio1 = new JRadioButton("男"); // 单选框1
		private JRadioButton radio2 = new JRadioButton("女"); // 单选框2
		private ButtonGroup group = new ButtonGroup(); // 此类用于为一组按钮创建一个多斥作用域

		private JTextField text6 = new JTextField(15); // 显示出生日期对应的文本框
		private JButton regisiteButton = new JButton("注册"); // 注册按钮

		public regisiteListener1() {
			text2.setEchoChar('*'); // 设置此 text2 的回显字符为*（设置密码）
			text3.setEchoChar('*'); // 设置此 text3 的回显字符为*（确认密码）
			jframe2.setLayout(null); // 布局格式清空
			jframe2.setSize(1000, 800); // 设置窗体大小
			jframe2.setLocationRelativeTo(null); // 设置窗体居中
			backButton.setBounds(30, 30, 80, 50); // 设置返回按钮位置及大小
			// 注册返回按钮动作监听事件
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LoginManager login = new LoginManager(); // 当点击返回按钮时，登录界面显现
					jframe2.dispose(); // 释放由此
										// Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源。
					// 清空登录界面内容
					text1.setText("");
					text2.setText("");
					text3.setText("");
					text4.setText("");
					// text5.setText("");
					text6.setText("");
				}
			});
			// 注册单选框1动作监听器
			radio1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					str = "男";
				}
			});
			// 注册单选框2动作监听器
			radio2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					str = "女";
				}
			});
			// 设置基本组件的大小及位置
			showLabel1.setBounds(200, 50, 600, 80);
			userName.setBounds(250, 170, 100, 40);
			setPassword.setBounds(250, 250, 100, 40);
			surePassword.setBounds(250, 330, 100, 40);
			name.setBounds(250, 410, 100, 40);
			sex.setBounds(250, 490, 100, 40);
			birthday.setBounds(250, 570, 100, 40);
			text1.setBounds(350, 170, 400, 40);
			text2.setBounds(350, 250, 400, 40);
			text3.setBounds(350, 330, 400, 40);
			text4.setBounds(350, 410, 400, 40);
			// text5.setBounds(350, 490, 400, 40);
			radio1.setBounds(350, 490, 100, 40);
			radio2.setBounds(500, 490, 100, 40);
			text6.setBounds(350, 570, 400, 40);
			regisiteButton.setBounds(800, 650, 100, 50);
			showLabel2.setBounds(350, 295, 400, 20);
			showLabel3.setBounds(350, 610, 400, 20);
			// 把这些基本组件添加到注册界面里
			jframe2.add(backButton);
			jframe2.add(userName);
			jframe2.add(showLabel1);
			jframe2.add(setPassword);
			jframe2.add(surePassword);
			jframe2.add(name);
			jframe2.add(sex);
			jframe2.add(birthday);
			jframe2.add(text1);
			jframe2.add(text2);
			jframe2.add(text3);
			jframe2.add(text4);
			// jframe2.add(text5);
			group.add(radio1); // 归成一组才能实现单选
			group.add(radio2);
			jframe2.add(radio1);
			jframe2.add(radio2);
			jframe2.add(text6);
			jframe2.add(regisiteButton);
			jframe2.add(showLabel2);
			jframe2.add(showLabel3);
			// 设置组件内容的字体样式及颜色
			backButton.setFont(new Font("", Font.BOLD, 40));// 字体样式黑体，40
			backButton.setForeground(Color.BLUE);// 字体颜色为蓝色
			userName.setFont(new Font("", Font.BOLD, 20));
			showLabel1.setFont(new Font("", Font.BOLD, 40));
			showLabel1.setForeground(Color.BLUE);
			setPassword.setFont(new Font("", Font.BOLD, 20));
			surePassword.setFont(new Font("", Font.BOLD, 20));
			name.setFont(new Font("", Font.BOLD, 20));
			sex.setFont(new Font("", Font.BOLD, 20));
			birthday.setFont(new Font("", Font.BOLD, 20));
			text1.setFont(new Font("", Font.BOLD, 20));
			text2.setFont(new Font("", Font.BOLD, 20));
			text3.setFont(new Font("", Font.BOLD, 20));
			text4.setFont(new Font("", Font.BOLD, 20));
			// text5.setFont(new Font("", Font.BOLD, 20));
			radio1.setFont(new Font("", Font.BOLD, 20));
			radio2.setFont(new Font("", Font.BOLD, 20));
			text6.setFont(new Font("", Font.BOLD, 20));
			regisiteButton.setFont(new Font("", Font.BOLD, 20));
			regisiteButton.setForeground(Color.BLUE);
			showLabel2.setFont(new Font("", Font.BOLD, 18));
			showLabel2.setForeground(Color.RED);
			showLabel3.setFont(new Font("", Font.BOLD, 18));
			showLabel3.setForeground(Color.RED);
		}

		// 处理登录界面里的注册按钮监听事件
		@Override
		public void actionPerformed(ActionEvent e) {
			jframe2.setResizable(false); // 设置注册界面大小不可改变
			jframe2.getContentPane().setBackground(Color.PINK);
			jframe2.setVisible(true); // 当点击登录界面的注册按钮时设置注册界面可见
			jframe1.dispose();
			; // 释放由此 Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源。
				// 清空登录界面各组件内容
			userField.setText("");
			passwordField.setText("");

			Service.windowClose(jframe2);// 注册界面的窗口监听事件
			jframe2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 释放注册界面窗口
			regisiteButton.addActionListener(new regisiteListener2()); // 注册界面里的注册按钮动作监听事件
		}

		// 处理注册界面里的注册按钮动作监听事件
		public class regisiteListener2 implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (text1.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(jframe2, "学号项为空，请您重新填写！");
				} else if (text2.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(jframe2, "设置密码项为空，请您重新填写！");
				} else if (text3.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(jframe2, "确认密码项为空，请您重写填写！");
				} else if (text4.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(jframe2, "姓名项为空，请您重写填写！");
				} // else if (text5.getText().trim().equals("")) {
				else if (str.equals("")) {
					JOptionPane.showMessageDialog(jframe2, "性别项为空，请您重写填写！");
				} else if (text6.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(jframe2, "出生日期项为空，请您重写填写！");
				} else {
					// 注册用户信息录入数据库
					Service.regisiteUser(text1.getText().trim(), text2.getText().trim(), text3.getText().trim(),
							text4.getText().trim(), /* text5.getText().trim() */str, text6.getText().trim());
				}
			}
		}
	}


}
