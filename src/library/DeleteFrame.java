package library;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import library.SelectFrame.GetbackListener;
import library.SelectFrame.SelectListener;

public class DeleteFrame extends JFrame {
	private JLabel showLabel, nameLabel, authorLabel, publisherLabel, hintLabel1, hintLabel2;
	private JTextField nameField, authorField;
	private JComboBox publisherChoice;
	private JRadioButton radio1, radio2;
	private ButtonGroup group;
	private JButton selectButton, getbackButton, deleteButton;
	private JList list;
	private long userId;
	private JScrollPane scroll;

	public DeleteFrame(long userId) {
		super("欢迎您来到删除图书界面");
		this.userId = userId;
		this.setLayout(null);
		Vector<String> publisherInfo = Service.getPublisher();
		showLabel = new JLabel("删除图书");
		nameLabel = new JLabel("书  名：");
		authorLabel = new JLabel("作  者：");
		publisherLabel = new JLabel("出版社：");
		hintLabel1 = new JLabel("检索条件：");
		hintLabel2 = new JLabel("检索结果（图书信息）：");

		nameField = new JTextField(30);
		authorField = new JTextField(20);
		publisherChoice = new JComboBox(publisherInfo);
		radio1 = new JRadioButton("完全一致", true);
		radio2 = new JRadioButton("模糊查询");
		group = new ButtonGroup();
		group.add(radio1);
		group.add(radio2);

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("", Font.BOLD, 20));
		scroll = new JScrollPane();
		scroll.getViewport().setView(list);

		selectButton = new JButton("开始检索");
		getbackButton = new JButton("←");
		deleteButton = new JButton("删除");

		this.add(showLabel);
		this.add(nameLabel);
		this.add(authorLabel);
		this.add(publisherLabel);
		this.add(hintLabel1);
		this.add(hintLabel2);
		this.add(nameField);
		this.add(authorField);
		this.add(publisherChoice);
		this.add(radio1);
		this.add(radio2);
		this.add(selectButton);
		this.add(getbackButton);
		this.add(deleteButton);
		this.add(scroll);

		this.setSize(1000, 800);
		showLabel.setBounds(400, 20, 200, 50);
		nameLabel.setBounds(160, 100, 80, 50);
		authorLabel.setBounds(160, 170, 80, 50);
		publisherLabel.setBounds(160, 240, 100, 50);
		nameField.setBounds(260, 100, 600, 50);
		authorField.setBounds(260, 170, 600, 50);
		publisherChoice.setBounds(260, 240, 600, 50);
		hintLabel1.setBounds(250, 300, 110, 50);
		radio1.setBounds(360, 300, 110, 50);
		radio2.setBounds(470, 300, 110, 50);
		selectButton.setBounds(630, 300, 130, 50);
		hintLabel2.setBounds(30, 350, 240, 50);
		scroll.setBounds(30, 400, 940, 320);
		getbackButton.setBounds(25, 25, 80, 50);
		deleteButton.setBounds(450, 720, 100, 40);

		showLabel.setFont(new Font("", Font.BOLD, 40));
		showLabel.setForeground(Color.BLUE);
		nameLabel.setFont(new Font("", Font.BOLD, 20));
		authorLabel.setFont(new Font("", Font.BOLD, 20));
		publisherLabel.setFont(new Font("", Font.BOLD, 20));
		nameField.setFont(new Font("", Font.BOLD, 20));
		authorField.setFont(new Font("", Font.BOLD, 20));
		publisherChoice.setFont(new Font("", Font.BOLD, 20));
		hintLabel1.setFont(new Font("", Font.BOLD, 20));
		radio1.setFont(new Font("", Font.BOLD, 20));
		radio2.setFont(new Font("", Font.BOLD, 20));
		selectButton.setFont(new Font("", Font.BOLD, 20));
		hintLabel2.setFont(new Font("", Font.BOLD, 20));
		scroll.setFont(new Font("", Font.BOLD, 20));
		getbackButton.setFont(new Font("", Font.BOLD, 40));
		getbackButton.setForeground(Color.BLUE);
		deleteButton.setFont(new Font("", Font.BOLD, 20));

		getbackButton.addActionListener(new GetbackListener());
		selectButton.addActionListener(new SelectListener());
		deleteButton.addActionListener(new DeleteListener());

		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				BooksManager app = new BooksManager(userId);
				dispose();
			}
		});
		this.setVisible(true);
		this.getContentPane().setBackground(Color.PINK);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	public class GetbackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			BooksManager app = new BooksManager(userId);
			dispose();
		}
	}

	public class SelectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Vector<String> selectResult = new Vector<String>();
			list.setListData(selectResult);// 根据一个 Vector 构造只读
											// ListModel，清除显示结果查询的列表
			String name = nameField.getText().trim();
			String author = authorField.getText().trim();
			String publisher = publisherChoice.getSelectedItem().toString();
			String radio = "";
			if (radio1.isSelected()) {
				radio = radio1.getText().trim();
			}
			if (radio2.isSelected()) {
				radio = radio2.getText().trim();
			}
			selectResult = Service.selectBooks(name, author, publisher, radio);
			list.setListData(selectResult);
			if (selectResult.isEmpty()) {
				JOptionPane.showMessageDialog(null, "没有检索到符合条件的图书，请您重新输入！");
			}
			nameField.setText("");
			authorField.setText("");
			publisherChoice.setSelectedIndex(0);
		}
	}

	public class DeleteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String bookValue = (String) list.getSelectedValue();
			if (bookValue == null) {
				JOptionPane.showMessageDialog(null, "请先进行检索,然后再选择一本图书!");
				return;
			}
			String str = "";
			String str2[] = new String[7];
			for (int i = 0; i < bookValue.length() - 7; i++) {
				String str1 = bookValue.substring(i, i + 8);
				if (str1.equals("; 此书的总数量")) {
					str += bookValue.substring(0, i + 1);
					break;
				}
			}
			int index = 0;
			for (int i = 0; i < 7; i++) {
				index = str.indexOf(';');
				str2[i] = str.substring(0, index).trim();
				str = str.substring(index + 1);
			}
			int ok = JOptionPane.showConfirmDialog(null, "您决定要删除的信息如下：\n" + str2[0] + "\n" + str2[1] + "\n" + str2[2]
					+ "\n" + str2[3] + "\n" + str2[4] + "\n" + str2[5] + "\n" + str2[6] + "\n" + "您确定需要删除吗？");
			if (ok > 0) {
				JOptionPane.showMessageDialog(null, "已取消这次操作！");
				return;
			} else {
				String str1 = str2[0];
				int find = str1.indexOf(':');
				str1 = str2[0].substring(2, find);
				int id = Integer.parseInt(str1);
				int flag = Service.deleteBooks(id);
				if (flag == -1) {
					JOptionPane.showMessageDialog(null, "删除此图书失败，请您重新操作!");
				} else {
					JOptionPane.showMessageDialog(null, "成功删除此图书！");
				}
			}
		}
	}
}
