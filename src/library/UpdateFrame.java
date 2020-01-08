package library;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import library.InsertFrame.CancelListener;
import library.InsertFrame.GetbackListener;
import library.InsertFrame.SubmitListener;

public class UpdateFrame extends JFrame {
	// 录入图书信息提示语句标签
	private JLabel isbnLabel, nameLabel, authorLabel, publisherLabel;
	private JLabel priceLabel, pubDateLabel, depositLabel, quantifyLabel;
	// 录入图书信息文本框
	private JTextField isbnField, nameField, authorField, publisherField;
	private JTextField priceField, pubDateField, depositField, quantifyField;
	// 界面中的提交，取消，返回按钮
	private JButton update, cancel, getback;
	// 窗体
	private JFrame jframe;
	// 提示信息
	private JLabel showLabel;
	// 用户名（学号）
	private long userId;
	//存储要修改的格式化后的图书信息
	private Vector<String> bookInfo = new Vector<String>();
	//存储要修改的图书信息
	private String bookValue;

	// 初始化录入图书界面
	public UpdateFrame(long userId, String bookValue) {
		jframe = new JFrame("欢迎您来到修改图书信息界面");
		this.userId = userId;
		this.bookValue = bookValue;
		jframe.setLayout(null); // 设置窗体无布局
		// 界面中的录入提示信息
		showLabel = new JLabel("修改图书信息", JLabel.CENTER);
		isbnLabel = new JLabel("书        号");
		nameLabel = new JLabel("书        名");
		authorLabel = new JLabel("作        者");
		publisherLabel = new JLabel("出   版   社");
		priceLabel = new JLabel("价        格");
		pubDateLabel = new JLabel("出版时间");
		depositLabel = new JLabel("存放位置");
		quantifyLabel = new JLabel("数        量");
		// 设置界面中的文本框的内容及长度
		isbnField = new JTextField(25);
		nameField = new JTextField(25);
		authorField = new JTextField(25);
		publisherField = new JTextField(25);
		priceField = new JTextField(25);
		pubDateField = new JTextField(25);
		depositField = new JTextField(25);
		quantifyField = new JTextField(25);
		// 给按钮添加提示信息
		update = new JButton("修改");
		cancel = new JButton("取消");
		getback = new JButton("←");
		// 把以上组件添加到窗体中
		jframe.add(showLabel);
		jframe.add(isbnLabel);
		jframe.add(nameLabel);
		jframe.add(authorLabel);
		jframe.add(publisherLabel);
		jframe.add(priceLabel);
		jframe.add(pubDateLabel);
		jframe.add(depositLabel);
		jframe.add(quantifyLabel);

		jframe.add(isbnField);
		jframe.add(nameField);
		jframe.add(authorField);
		jframe.add(publisherField);
		jframe.add(priceField);
		jframe.add(pubDateField);
		jframe.add(depositField);
		jframe.add(quantifyField);

		jframe.add(update);
		jframe.add(cancel);
		jframe.add(getback);
		// 设置窗体大小
		jframe.setSize(1000, 800);
		// 设置提示信息标签位置
		showLabel.setBounds(400, 20, 280, 50);
		isbnLabel.setBounds(50, 200, 100, 50);
		nameLabel.setBounds(550, 200, 100, 50);
		authorLabel.setBounds(50, 290, 100, 50);
		publisherLabel.setBounds(550, 290, 100, 50);
		priceLabel.setBounds(50, 380, 100, 50);
		pubDateLabel.setBounds(550, 380, 100, 50);
		depositLabel.setBounds(50, 470, 100, 50);
		quantifyLabel.setBounds(550, 470, 100, 50);
		// 设置文本框位置
		isbnField.setBounds(150, 200, 300, 50);
		nameField.setBounds(650, 200, 300, 50);
		authorField.setBounds(150, 290, 300, 50);
		publisherField.setBounds(650, 290, 300, 50);
		priceField.setBounds(150, 380, 300, 50);
		pubDateField.setBounds(650, 380, 300, 50);
		depositField.setBounds(150, 470, 300, 50);
		quantifyField.setBounds(650, 470, 300, 50);
		// 设置三个按钮位置
		update.setBounds(550, 600, 100, 70);
		cancel.setBounds(350, 600, 100, 70);
		getback.setBounds(50, 50, 80, 50);
		// 设置界面中各组件的字体样式及颜色
		showLabel.setFont(new Font("", Font.BOLD, 40));
		showLabel.setForeground(Color.BLUE);
		isbnLabel.setFont(new Font("", Font.BOLD, 20));
		nameLabel.setFont(new Font("", Font.BOLD, 20));
		authorLabel.setFont(new Font("", Font.BOLD, 20));
		publisherLabel.setFont(new Font("", Font.BOLD, 20));
		priceLabel.setFont(new Font("", Font.BOLD, 20));
		pubDateLabel.setFont(new Font("", Font.BOLD, 20));
		depositLabel.setFont(new Font("", Font.BOLD, 20));
		quantifyLabel.setFont(new Font("", Font.BOLD, 20));
		isbnField.setFont(new Font("", Font.BOLD, 20));
		nameField.setFont(new Font("", Font.BOLD, 20));
		authorField.setFont(new Font("", Font.BOLD, 20));
		publisherField.setFont(new Font("", Font.BOLD, 20));
		priceField.setFont(new Font("", Font.BOLD, 20));
		pubDateField.setFont(new Font("", Font.BOLD, 20));
		depositField.setFont(new Font("", Font.BOLD, 20));
		quantifyField.setFont(new Font("", Font.BOLD, 20));
		update.setFont(new Font("", Font.BOLD, 20));
		cancel.setFont(new Font("", Font.BOLD, 20));
		getback.setFont(new Font("", Font.BOLD, 40));
		getback.setForeground(Color.BLUE);
		//得到格式化后的图书信息
		bookInfo = getBookInfo(bookValue);
		isbnField.setText(bookInfo.elementAt(1));
		nameField.setText(bookInfo.elementAt(2));
		authorField.setText(bookInfo.elementAt(3));
		publisherField.setText(bookInfo.elementAt(4));
		priceField.setText(bookInfo.elementAt(5));
		pubDateField.setText(bookInfo.elementAt(6));
		depositField.setText(bookInfo.elementAt(7));
		quantifyField.setText(bookInfo.elementAt(8));
		// 给三个按钮注册动作监听器
		update.addActionListener(new Update1Listener());
		cancel.addActionListener(new CancelListener());
		getback.addActionListener(new GetbackListener());
		// 添加录入图书界面关闭监听事件
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				UpdateFrame1 update = new UpdateFrame1(userId);// 创建主界面
				jframe.dispose();// 释放由此 Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源。
			}
		});
		jframe.setResizable(false); // 设置窗体大小不可改变
		jframe.setLocationRelativeTo(null); // 设置窗体位置居中
		jframe.setVisible(true); // 设置窗体可见
		jframe.getContentPane().setBackground(Color.PINK);
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 释放窗体
	}
	//把图书信息格式化
	public Vector<String> getBookInfo(String bookValue) {
		Vector<String> result = new Vector<String>();
		String str = bookValue;
		int index = str.indexOf(':');
		result.add(str.substring(2,index));
		int index1 = 0;
		str = str.substring(index+1);
		for(int i=1;i<=8;i++){
			index = str.indexOf(':');
			index1 = str.indexOf(';');
			String s = str.substring(index+1,index1).trim();
			result.add(s);
			str = str.substring(index1+1);
		}
		return result;
	}
	// 清空界面录入信息
	public void clearField() {
		isbnField.setText("");
		nameField.setText("");
		authorField.setText("");
		publisherField.setText("");
		priceField.setText("");
		pubDateField.setText("");
		depositField.setText("");
		quantifyField.setText("");
	}

	// 添加'提交'动作监听事件
	public class Update1Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 获取界面中的录入文本内容
			String isbn = isbnField.getText().trim();
			String name = nameField.getText().trim();
			String author = authorField.getText().trim();
			String publisher = publisherField.getText().trim();
			String price = priceField.getText().trim();
			String pubDate = pubDateField.getText().trim();
			String deposit = depositField.getText().trim();
			String quantify = quantifyField.getText().trim();
			if (isbn.equals("")) {
				JOptionPane.showMessageDialog(jframe, "'书号'项为空，请您重新填写！");
			} else if(name.equals("")) {
				JOptionPane.showMessageDialog(jframe, "'书名'项为空，请您重新填写！");
			} else if(author.equals("")) {
				JOptionPane.showMessageDialog(jframe, "'作者;项为空，请您重新填写！");
			} else if(publisher.equals("")) {
				JOptionPane.showMessageDialog(jframe, "'出版社'项为空，请您重新填写！");
			} else if(price.equals("")) {
				JOptionPane.showMessageDialog(jframe, "'价格'项为空，请您重新填写！");
			} else if(pubDate.equals("")) {
				JOptionPane.showMessageDialog(jframe, "'出版时间'项为空，请您重新填写！");
			} else if(deposit.equals("")) {
				JOptionPane.showMessageDialog(jframe, "'存放位置'为空，请您重新填写！");
			} else if(quantify.equals("")||quantify.trim().equals("0")) {
				JOptionPane.showMessageDialog(jframe, "'数量'项为空(0)，请您重新填写！");
			} else {//文本框内容都不为空时
				Vector<String> bookInfo1 = new Vector<String>(); // Vector类可以实现可增长的对象数组。
				// 将获得得各文本框的内容依次存放到bookInfo字符串数组中
				bookInfo1.add(isbn);
				bookInfo1.add(name);
				bookInfo1.add(author);
				bookInfo1.add(publisher);
				bookInfo1.add(price);
				bookInfo1.add(pubDate);
				bookInfo1.add(deposit);
				bookInfo1.add(quantify);
	
				if (Service.updateBook(bookInfo1, bookInfo.elementAt(0).trim()) == 0) {// 当录入信息成功存储到数据库中
					clearField(); // 清空界面文本框内容
					JOptionPane.showMessageDialog(jframe, "修改图书成功！");
				} else { // 录入信息添加到数据库中失败
					JOptionPane.showMessageDialog(jframe, "修改图书失败，请重新确认信息是否填写正确！");
				}
			}
		}
	}

	// 添加'取消'按钮动作监听事件
	public class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clearField(); // 当点击取消时，清空界面文本框内容
		}
	}

	// 添加'返回'按钮动作监听事件
	public class GetbackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			UpdateFrame1 update = new UpdateFrame1(userId); // 创建主界面
			jframe.dispose(); // 关闭录入图书界面
		}
	}
}
