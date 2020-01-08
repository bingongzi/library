package library;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

public class DataOperator {
	Connection con = null; // 与特定数据库的连接（会话）。在连接上下文中执行 SQL 语句并返回结果
	private PreparedStatement pstmt; // 表示预编译的 SQL 语句的对象。使数据库操作效率较高效
	private String sql;// 存储sql语句
	// 加载数据库JDBC驱动程序

	public void loadDatabaseDriver() {
		try {
			//System.out.println("数据库驱动成功");
			Class.forName("com.mysql.jdbc.Driver"); // 加载JDBC-MySQL驱动
		} catch (ClassNotFoundException e) {
			System.out.println("数据库驱动加载失败！");
			System.out.println(e);
		}
	}

	// 连接myBooks数据库
	public void connect() {
		String uri = "jdbc:mysql://localhost:3306/mybooks?useSSL=true&characterEncoding=utf-8";
		String user = "root";
		String password = "123456";
		try { // 试图建立到给定数据库 URL 的连接。
			//System.out.println("数据库连接成功");
			con = DriverManager.getConnection(uri, user, password);
		} catch (SQLException e) {
			System.out.println("数据库连接出错！");
			System.out.println(e);
		}
	}

	// 在user表中添加admin用户(如果user为空)
	public void addSuperUser() {
		sql = "select * from user"; // 存储sql语句

		try {
			pstmt = con.prepareStatement(sql); // 得到预处理语句对象
			ResultSet rs = pstmt.executeQuery(); // 经查询得到数据库结果集的数据表，
			if (!rs.next()) {
				String name = "Admin";
				String password = MD5.GetMD5Code("123456"); // 对超级用户密码进行加密
				sql = "insert into user values(?,?)"; // 使用通配符?来代替字段的值
				pstmt = con.prepareStatement(sql); // 预编译sql语句
				pstmt.setString(1, name); // 设置超级用户名
				pstmt.setString(2, password); // 设置超级用户密码
				pstmt.executeUpdate(); // 更新数据库
				con.close();

			}
		} catch (SQLException e) {
			System.out.println("添加超级用户出错");
			System.out.println(e);
		}

	}

	// 查询用户表，核对用户名和密码是否正确
	public long userQuery(String userName, String password) {
		sql = "select userName from user where userName = ? and setPassword = ?"; // 存储sql语句
		try {
			pstmt = con.prepareStatement(sql); // 预处理sql语句
			pstmt.setString(1, userName);
			pstmt.setString(2, MD5.SetMD5Code(password));
			ResultSet rs = pstmt.executeQuery(); // 经查询得到
			if (rs.next()) {// 核对信息成功
				return rs.getLong(1);// 以 Java 编程语言中 int 的形式获取此 ResultSet
										// 对象的当前行中第1的值。
			}
			return 0;
		} catch (SQLException e) {
			return -1; // 查询出错，返回-1
		}
	}

	// 注册用户信息
	public void regisite(String str1, String str2, String str3, String str4, String str5, String str6) {
		loadDatabaseDriver();
		connect();
		sql = "insert into user values(0,?,?,?,?,?,?)"; // 更新SQL语句内容
		int ok = 0;
		String s1 = ""; // 暂存设置密码
		String s2 = ""; // 暂存确认密码
		try {
			pstmt = con.prepareStatement(sql);// 得到预处理语句对象
			pstmt.setLong(1, Long.parseLong(str1));// 注册用户名（账号）
			pstmt.setString(2, str2);// 注册设置密码
			pstmt.setString(3, str3);// 注册确认密码
			pstmt.setString(4, str4);// 注册姓名
			pstmt.setString(5, str5);// 注册性别
			pstmt.setString(6, str6);// 注册出生日期
			s1 += str2;
			s2 += str3;
			if (s1.equals(s2)) {
				ok = pstmt.executeUpdate();// 更新数据库
			}
			con.close();// 关闭数据库
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "学号已被注册", "警告", JOptionPane.WARNING_MESSAGE);
		}
		if (ok != 0) {
			JOptionPane.showMessageDialog(null, "注册成功", "恭喜", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "注册失败，设定密码不一致", "警告", JOptionPane.WARNING_MESSAGE);
		}
	}

	// 录入图书信息
	public int insert(Vector<String> bookInfo) {
		sql = "insert into books values(0,?,?,?,?,?,?,?,?,0)"; // 存储sql语句
		try {
			pstmt = con.prepareStatement(sql); // 预处理sql语句
			for (int i = 1; i <= bookInfo.size(); i++) {// 循环遍历得到录入内容
				if (i == 8) { // 录入书本数量为整数，要转化为整数
					pstmt.setInt(i, Integer.parseInt(bookInfo.elementAt(i - 1).trim()));// elmentAt返回指定索引处的组件。
				} else { // 录入内容为字符串
					pstmt.setString(i, bookInfo.elementAt(i - 1).trim());
				}
			}
			pstmt.executeUpdate(); // 更新数据库
			return 0; // 录入成功
		} catch (SQLException e) {
			return -1; // 录入失败
		}

	}

	// 读取出版社信息
	public Vector<String> publisherQuery() {
		Vector<String> publisherInfo = new Vector<String>();
		sql = "select distinct publisher from books";
		try {
			pstmt = con.prepareStatement(sql);
			publisherInfo.add("");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				publisherInfo.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return publisherInfo;
	}

	// 读取检索的图书信息
	public Vector<String> selectbooks(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		sql = "select * from books";
		int flag = 0;
		if (name.equals("")) {
			if (author.equals("")) {
				if (!publisher.equals("")) {
					sql += " where publisher like ?";
					flag = 1;
				}
			} else {
				if (publisher.equals("")) {
					sql += " where author like ?";
					flag = 2;
				} else {
					sql += " where author like ? and publisher like ?";
					flag = 3;
				}
			}
		} else {
			if (author.equals("") && publisher.equals("")) {
				sql += " where name like ?";
				flag = 4;
			}
			if (author.equals("") && !publisher.equals("")) {
				sql += " where name like ? and publisher like ?";
				flag = 5;
			}
			if (!author.equals("") && publisher.equals("")) {
				sql += " where name like ? and author like ?";
				flag = 6;
			}
			if (!author.equals("") && !publisher.equals("")) {
				sql += " where name like ? and author like ? and publisher like ?";
				flag = 7;
			}
		}
		if (radio.equals("模糊查询")) {
			name = "%" + name + "%";
			author = "%" + author + "%";
			publisher = "%" + publisher + "%";
		}
		try {
			pstmt = con.prepareStatement(sql);
			switch (flag) {
			case 0:
				break;
			case 1:
				pstmt.setString(1, publisher);
				break;
			case 2:
				pstmt.setString(1, author);
				break;
			case 3:
				pstmt.setString(1, author);
				pstmt.setString(2, publisher);
				break;
			case 4:
				pstmt.setString(1, name);
				break;
			case 5:
				pstmt.setString(1, name);
				pstmt.setString(2, publisher);
				break;
			case 6:
				pstmt.setString(1, name);
				pstmt.setString(2, author);
				break;
			case 7:
				pstmt.setString(1, name);
				pstmt.setString(2, author);
				pstmt.setString(3, publisher);
				break;
			}
			ResultSet rs = pstmt.executeQuery();
			String str = "";
			while (rs.next()) {
				for (int i = 1; i <= 10/* rs.getMetaData().getColumnCount() */; i++) {
					if (i == 1 || i == 9 || i == 10) {
						if (i == 1) {
							str += "序号" + rs.getInt(i) + ": ";
						}
						if (i == 9) {
							str += "此书的总数量: " + rs.getInt(i) + "; ";
						}
						if (i == 10) {
							str += "已被借走的数量: " + rs.getInt(i) + ";";
						}
					} else {
						if (i == 2) {
							str += "书号: ";
						}
						if (i == 3) {
							str += "书名: ";
						}
						if (i == 4) {
							str += "作者: ";
						}
						if (i == 5) {
							str += "出版社: ";
						}
						if (i == 6) {
							str += "价格: ";
						}
						if (i == 7) {
							str += "出版日期: ";
						}
						if (i == 8) {
							str += "位置: ";
						}
						str += rs.getString(i).trim() + "; ";
					}
				}
				str = str.substring(0, str.length());
				result.add(str);
				str = "";
			}
		} catch (SQLException e) {
			System.out.println("查询数据库出错");
			System.out.println(e);
		}

		return result;
	}
	
	// 删除图书
	public int deleteId(int id) {
		sql = "delete from books where id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("删除数据出错");
			System.out.println(e);
			return -1;
		}
		return 0;
	}

	// 关闭数据库连接
	public void disconnect() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	// 更新图书信息
	public int update(Vector<String> bookInfo1, String id) {
		int Id = Integer.parseInt(id);
		//System.out.println(Id);
		sql = "update books set isbn = ?, name = ?, author = ?, publisher = ?, price = ?, pubDate = ?, deposit = ?, quantify = ? where id = ?"; // 存储sql语句
		try {
			pstmt = con.prepareStatement(sql); // 预处理sql语句
			for (int i = 1; i <= bookInfo1.size(); i++) {// 循环遍历得到录入内容
				//System.out.println(bookInfo1.elementAt(i - 1).trim());
				if (i == 8) { // 录入书本数量为整数，要转化为整数
					pstmt.setInt(i, Integer.parseInt(bookInfo1.elementAt(i - 1).trim()));// elmentAt返回指定索引处的组件。
				} else { // 录入内容为字符串
					pstmt.setString(i, bookInfo1.elementAt(i - 1).trim());
				}
			}
			pstmt.setInt(bookInfo1.size() + 1, Id);
			pstmt.executeUpdate(); // 更新数据库
			return 0; // 录入成功
		} catch (SQLException e) {
			System.out.println(e);
			return -1; // 录入失败
		}

	}

	// 开始借书
	public int lend(int id, long userId, int lendQuantify) {
		String name = "";
		sql = "select name from user where userName = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, userId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			name = rs.getString(1);
		} catch (SQLException e) {
			System.out.println(e);
			return -1;
		}

		try {
			con.setAutoCommit(false);// 关闭数据库事务自动更新模式
			if (updateStock(id, lendQuantify) == -1) {
				return -1;
			}
			if (insertLendRecord(id, userId, name) == -1) {
				return -1;
			}
			con.commit();
			con.setAutoCommit(true);
			return 0;
		} catch (SQLException e) {
			System.out.println(e);
			rollback();
			return -1;
		}
	}

	// 回滚,撤销事务
	public void rollback() {
		if (con == null) {
			return;
		}
		System.err.println("发生异常，正在撤销事务");
		try {
			con.rollback();// 撤销事务
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	// 修改借、还图书时的借书数量
	public int updateStock(int id, int lendQuantify) {
		sql = "update books set lend = ? where id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, lendQuantify);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
			return 0;
		} catch (SQLException e) {
			System.err.println("修改库存记录出错！");
			System.out.println(e);
			rollback();
			return -1;
		}
	}

	// 在借书记录表中添加借书的内容
	public int insertLendRecord(int id, long userId, String name) {
		sql = "insert into lendrecord values(0,?,?,?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setLong(2, userId);
			pstmt.setString(3, name);
			java.util.Date d = new java.util.Date();
			pstmt.setString(4, d.toString());
			pstmt.setString(5, "待还");
			pstmt.executeUpdate();
			return 0;
		} catch (SQLException e) {
			System.err.println("存储借书记录出错!");
			System.out.println(e);
			rollback();
			return -1;
		}
	}

	// 查看借书记录
	public Vector<String> selectrecordBooks(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		sql = "select lendRecord.id,books.isbn,books.name,books.author,books.publisher,books.pubDate,lendRecord.userId,"
				+ "lendRecord.borrowName,lendRecord.borrowDate,lendRecord.state from lendRecord inner join books on "
				+ "lendRecord.bookId = books.id inner join user on lendRecord.userId = user.userName";
		int flag = 0;
		if (name.equals("")) {
			if (author.equals("")) {
				if (!publisher.equals("")) {
					sql += " where publisher like ?";
					flag = 1;
				}
			} else {
				if (publisher.equals("")) {
					sql += " where author like ?";
					flag = 2;
				} else {
					sql += " where author like ? and publisher like ?";
					flag = 3;
				}
			}
		} else {
			if (author.equals("") && publisher.equals("")) {
				sql += " where books.name like ?";
				flag = 4;
			}
			if (author.equals("") && !publisher.equals("")) {
				sql += " where books.name like ? and publisher like ?";
				flag = 5;
			}
			if (!author.equals("") && publisher.equals("")) {
				sql += " where books.name like ? and author like ?";
				flag = 6;
			}
			if (!author.equals("") && !publisher.equals("")) {
				sql += " where books.name like ? and author like ? and publisher like ?";
				flag = 7;
			}
		}
		if (radio.equals("模糊查询")) {
			name = "%" + name + "%";
			author = "%" + author + "%";
			publisher = "%" + publisher + "%";
		}
		try {
			pstmt = con.prepareStatement(sql);
			switch (flag) {
			case 0:
				break;
			case 1:
				pstmt.setString(1, publisher);
				break;
			case 2:
				pstmt.setString(1, author);
				break;
			case 3:
				pstmt.setString(1, author);
				pstmt.setString(2, publisher);
				break;
			case 4:
				pstmt.setString(1, name);
				break;
			case 5:
				pstmt.setString(1, name);
				pstmt.setString(2, publisher);
				break;
			case 6:
				pstmt.setString(1, name);
				pstmt.setString(2, author);
				break;
			case 7:
				pstmt.setString(1, name);
				pstmt.setString(2, author);
				pstmt.setString(3, publisher);
				break;
			}
			ResultSet rs = pstmt.executeQuery();
			String str = "";
			while (rs.next()) {
				for (int i = 1; i <= 10/* rs.getMetaData().getColumnCount() */; i++) {
					if (i == 1) {
						str += "记录序号" + rs.getInt(i) + ": ";
					} else if (i == 7) {
						str += "学号: " + rs.getLong(i) + "; ";
					} else {
						if (i == 2) {
							str += "书号: ";
						}
						if (i == 3) {
							str += "书名: ";
						}
						if (i == 4) {
							str += "作者: ";
						}
						if (i == 5) {
							str += "出版社: ";
						}
						if (i == 6) {
							str += "出版日期: ";
						}
						if (i == 8) {
							str += "用户姓名: ";
						}
						if (i == 9) {
							str += "借书日期: ";
						}
						if (i == 10) {
							str += "还书状态: ";
						}
						str += rs.getString(i).trim() + "; ";
					}
				}
				str = str.substring(0, str.length());
				result.add(str);
				str = "";
			}
		} catch (SQLException e) {
			System.out.println("查询数据库出错");
			System.out.println(e);
		}

		return result;
	}
	// 查看还书记录
	public Vector<String> selectreturnBooks(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		sql = "select lendRecord.id,books.isbn,books.name,books.author,books.publisher,books.pubDate,lendRecord.userId,"
				+ "lendRecord.borrowName,lendRecord.borrowDate,lendRecord.state from lendRecord inner join books on "
				+ "lendRecord.bookId = books.id inner join user on lendRecord.userId = user.userName where state = ?";
		int flag = 0;
		if (name.equals("")) {
			if (author.equals("")) {
				if (!publisher.equals("")) {
					sql += " and publisher like ?";
					flag = 1;
				}
			} else {
				if (publisher.equals("")) {
					sql += " and author like ?";
					flag = 2;
				} else {
					sql += " and author like ? and publisher like ?";
					flag = 3;
				}
			}
		} else {
			if (author.equals("") && publisher.equals("")) {
				sql += " and books.name like ?";
				flag = 4;
			}
			if (author.equals("") && !publisher.equals("")) {
				sql += " and books.name like ? and publisher like ?";
				flag = 5;
			}
			if (!author.equals("") && publisher.equals("")) {
				sql += " and books.name like ? and author like ?";
				flag = 6;
			}
			if (!author.equals("") && !publisher.equals("")) {
				sql += " and books.name like ? and author like ? and publisher like ?";
				flag = 7;
			}
		}
		if (radio.equals("模糊查询")) {
			name = "%" + name + "%";
			author = "%" + author + "%";
			publisher = "%" + publisher + "%";
		}
		try {
			pstmt = con.prepareStatement(sql);
			switch (flag) {
			case 0:
				pstmt.setString(1, "已还");
				break;
			case 1:
				pstmt.setString(1, "已还");
				pstmt.setString(2, publisher);
				break;
			case 2:
				pstmt.setString(1, "已还");
				pstmt.setString(2, author);
				break;
			case 3:
				pstmt.setString(1, "已还");
				pstmt.setString(2, author);
				pstmt.setString(3, publisher);
				break;
			case 4:
				pstmt.setString(1, "已还");
				pstmt.setString(2, name);
				break;
			case 5:
				pstmt.setString(1, "已还");
				pstmt.setString(2, name);
				pstmt.setString(3, publisher);
				break;
			case 6:
				pstmt.setString(1, "已还");
				pstmt.setString(2, name);
				pstmt.setString(3, author);
				break;
			case 7:
				pstmt.setString(1, "已还");
				pstmt.setString(2, name);
				pstmt.setString(3, author);
				pstmt.setString(4, publisher);
				break;
			}
			ResultSet rs = pstmt.executeQuery();
			String str = "";
			while (rs.next()) {
				for (int i = 1; i <= 10/* rs.getMetaData().getColumnCount() */; i++) {
					if (i == 1) {
						str += "记录序号" + rs.getInt(i) + ": ";
					} else if (i == 7) {
						str += "学号: " + rs.getLong(i) + "; ";
					} else {
						if (i == 2) {
							str += "书号: ";
						}
						if (i == 3) {
							str += "书名: ";
						}
						if (i == 4) {
							str += "作者: ";
						}
						if (i == 5) {
							str += "出版社: ";
						}
						if (i == 6) {
							str += "出版日期: ";
						}
						if (i == 8) {
							str += "用户姓名: ";
						}
						if (i == 9) {
							str += "借书日期: ";
						}
						if (i == 10) {
							str += "还书状态: ";
						}
						str += rs.getString(i).trim() + "; ";
					}
				}
				str = str.substring(0, str.length());
				result.add(str);
				str = "";
			}
		} catch (SQLException e) {
			System.out.println("查询数据库出错");
			System.out.println(e);
		}

		return result;
	}
	// 查看未还书信息
	public Vector<String> selectreturn1Books(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		sql = "select lendRecord.id,books.isbn,books.name,books.author,books.publisher,books.pubDate,lendRecord.userId,"
				+ "lendRecord.borrowName,lendRecord.borrowDate,lendRecord.state from lendRecord inner join books on "
				+ "lendRecord.bookId = books.id inner join user on lendRecord.userId = user.userName where state = ?";
		int flag = 0;
		if (name.equals("")) {
			if (author.equals("")) {
				if (!publisher.equals("")) {
					sql += " and publisher like ?";
					flag = 1;
				}
			} else {
				if (publisher.equals("")) {
					sql += " and author like ?";
					flag = 2;
				} else {
					sql += " and author like ? and publisher like ?";
					flag = 3;
				}
			}
		} else {
			if (author.equals("") && publisher.equals("")) {
				sql += " and books.name like ?";
				flag = 4;
			}
			if (author.equals("") && !publisher.equals("")) {
				sql += " and books.name like ? and publisher like ?";
				flag = 5;
			}
			if (!author.equals("") && publisher.equals("")) {
				sql += " and books.name like ? and author like ?";
				flag = 6;
			}
			if (!author.equals("") && !publisher.equals("")) {
				sql += " and books.name like ? and author like ? and publisher like ?";
				flag = 7;
			}
		}
		if (radio.equals("模糊查询")) {
			name = "%" + name + "%";
			author = "%" + author + "%";
			publisher = "%" + publisher + "%";
		}
		try {
			pstmt = con.prepareStatement(sql);
			switch (flag) {
			case 0:
				pstmt.setString(1, "待还");
				break;
			case 1:
				pstmt.setString(1, "待还");
				pstmt.setString(2, publisher);
				break;
			case 2:
				pstmt.setString(1, "待还");
				pstmt.setString(2, author);
				break;
			case 3:
				pstmt.setString(1, "待还");
				pstmt.setString(2, author);
				pstmt.setString(3, publisher);
				break;
			case 4:
				pstmt.setString(1, "待还");
				pstmt.setString(2, name);
				break;
			case 5:
				pstmt.setString(1, "待还");
				pstmt.setString(2, name);
				pstmt.setString(3, publisher);
				break;
			case 6:
				pstmt.setString(1, "待还");
				pstmt.setString(2, name);
				pstmt.setString(3, author);
				break;
			case 7:
				pstmt.setString(1, "待还");
				pstmt.setString(2, name);
				pstmt.setString(3, author);
				pstmt.setString(4, publisher);
				break;
			}
			ResultSet rs = pstmt.executeQuery();
			String str = "";
			while (rs.next()) {
				for (int i = 1; i <= 10/* rs.getMetaData().getColumnCount() */; i++) {
					if (i == 1) {
						str += "记录序号" + rs.getInt(i) + ": ";
					} else if (i == 7) {
						str += "学号: " + rs.getLong(i) + "; ";
					} else {
						if (i == 2) {
							str += "书号: ";
						}
						if (i == 3) {
							str += "书名: ";
						}
						if (i == 4) {
							str += "作者: ";
						}
						if (i == 5) {
							str += "出版社: ";
						}
						if (i == 6) {
							str += "出版日期: ";
						}
						if (i == 8) {
							str += "用户姓名: ";
						}
						if (i == 9) {
							str += "借书日期: ";
						}
						if (i == 10) {
							str += "还书状态: ";
						}
						str += rs.getString(i).trim() + "; ";
					}
				}
				str = str.substring(0, str.length());
				result.add(str);
				str = "";
			}
		} catch (SQLException e) {
			System.out.println("查询数据库出错");
			System.out.println(e);
		}

		return result;
	}
	// 还书时根据借书记录标识查询出图书标识
	public int bookIdQuery(int lendRecordId) {
		sql = "select bookId from lendRecord where id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, lendRecordId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			return -1;
		}
		return 0;
	}

	// 还书时，根据图书标识查询出图书信息
	public String queryBookInfo(int bookId) {
		sql = "select * from books where id = ?";
		String str[] = new String[10];
		String bookInfo = "";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, bookId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {// 如果查询到此书
				for (int i = 0; i < 10; i++) {
					if (i == 0 || i == 8 || i == 9) {
						str[i] = String.valueOf(rs.getInt(i + 1));
					} else {
						str[i] = rs.getString(i + 1);
					}
				}
			}
		} catch (SQLException e) {
			return null;
		}
		bookInfo = "书号: " + str[1] + "\n" + "书名: " + str[2] + "\n" + "作者: " + str[3] + "\n" + "出版社: " + str[4] + "\n"
				+ "价格: " + str[5] + "\n" + "出版时间: " + str[6] + "\n" + "存放位置: " + str[7] + "\n" + "此书的总数量: " + str[8]
				+ "\n" + "借出数量: " + str[9] + "\n";
		return bookInfo;
	}

	// 还书的操作
	public int returnBook(int lendRecordId, int bookId, long userId, int lendQuantify) {
		try {
			con.setAutoCommit(false);// 关闭数据库事务自动更新模式
			if (updateStateOfLendRecord(lendRecordId) != 0) {
				return -1;
			}
			if (updateStock(bookId, lendQuantify) == -1) {
				return -1;
			}
			if (insertReturnRecord(bookId, userId) == -1) {
				return -1;
			}
			con.commit();
			con.setAutoCommit(true);
			return 0;
		} catch (SQLException e) {
			rollback();
			return -1;
		}
	}

	// 还书时修改借书记录的状态值
	public int updateStateOfLendRecord(int lendRecordId) {
		sql = "update lendRecord set state = ? where id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "已还");
			pstmt.setInt(2, lendRecordId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			rollback();
			return -1;
		}
		return 0;
	}

	// 在还书表中添加还书的内容
	public int insertReturnRecord(int bookId, long userId) {
		String name1 = "";
		sql = "select userName from user where userName = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, userId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				name1 = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println(e);
			rollback();
			return -1;
		}
		sql = "insert into returnrecord values(0,?,?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, bookId);
			pstmt.setLong(2, userId);
			pstmt.setString(3, name1);
			java.util.Date d = new java.util.Date();
			pstmt.setString(4, d.toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			rollback();
			return -1;
		}
		return 0;
	}
}
