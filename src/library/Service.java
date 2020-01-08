package library;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;

public class Service {
	// 创建数据库操作对象
	private static DataOperator dataOperator = new DataOperator();

	// 对用户要登录的一系列操作，返回用户名即学号，参数:userName:用户名（学号），password:密码
	public static long login(String userName, String password) {
		dataOperator.loadDatabaseDriver(); // 加载数据库的JDBC驱动程序
		dataOperator.connect(); // 连接数据库
		// dataOperator.addSuperUser(); //添加admin用户(如果users表为空)
		return dataOperator.userQuery(userName, password); // 核对用户名和密码是否正确
	}

	// 关闭数据库连接
	public static void quit() {
		dataOperator.disconnect();
	}

	// 关闭窗口，释放程序 参数：jframe:窗体
	public static void windowClose(JFrame jframe) {
		// 添加关闭窗口事件
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Service.quit(); // 结束业务操作，关闭数据库连接
				System.exit(0); // 关闭窗口
			}
		});
	}

	// 注册用户信息 参数：str1: 用户名（学号），str2:设置密码,str3:确认密码，str4:姓名，str5:性别，str6:出生日期
	public static void regisiteUser(String str1, String str2, String str3, String str4, String str5, String str6) {
		dataOperator.regisite(str1, str2, str3, str4, str5, str6);
	}

	// 添加图书，返回0即成功添加，否则添加失败 参数：bookInfo:图书信息
	public static int addBook(Vector<String> bookInfo) {
		if (dataOperator.insert(bookInfo) == 0) { // 录入图书成功
			return 0;
		} else { // 录入图书失败
			return -1;
		}
	}

	// 获取出版社信息，返回Vector字符串数组
	public static Vector<String> getPublisher() {
		return dataOperator.publisherQuery();
	}

	// 检索图书信息，返回Vector字符串数组为表示图书信息
	// 参数：name:书名，author:作者，publisher:出版社，radio：查询方式：完全一致，模糊查询
	public static Vector<String> selectBooks(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		result = dataOperator.selectbooks(name, author, publisher, radio);
		return result;
	}

	// 删除图书信息，通过图书顺序标识号删除 参数：id:图书顺序号
	public static int deleteBooks(int id) {
		return dataOperator.deleteId(id);
	}

	// 修改图书 返回0表示修改成功，否则修改失败 参数：bookInfo1:图书信息，id:图书顺序号（字符串型）
	public static int updateBook(Vector<String> bookInfo1, String id) {
		if (dataOperator.update(bookInfo1, id) == 0) { // 录入图书成功
			return 0;
		} else { // 录入图书失败
			return -1;
		}
	}

	// 借阅图书 返回值表示是否借阅成功，参数id:图书顺序号，uesrId:用户名（学号），lendQuantify:借书量
	public static int lendBook(int id, long userId, int lendQuantify) {
		return dataOperator.lend(id, userId, lendQuantify);
	}

	// 查看借阅图书记录 参数：name:书名，author:作者，publisher:出版社，radio：查询方式：完全一致，模糊查询
	public static Vector<String> selectRecordBooks(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		result = dataOperator.selectrecordBooks(name, author, publisher, radio);
		return result;
	}
	// 查看还书记录 参数：name:书名，author:作者，publisher:出版社，radio：查询方式：完全一致，模糊查询
	public static Vector<String> selectReturnBooks(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		result = dataOperator.selectreturnBooks(name, author, publisher, radio);
		return result;
	}
	// 查看未还书记录 参数：name:书名，author:作者，publisher:出版社，radio：查询方式：完全一致，模糊查询
	public static Vector<String> selectReturn1Books(String name, String author, String publisher, String radio) {
		Vector<String> result = new Vector<String>();
		result = dataOperator.selectreturn1Books(name, author, publisher, radio);
		return result;
	}

	// 查询所借的书对应的图书顺序的标识号    参数：lendRecordId:借书记录标识号
	public static int BookIdQuery(int lendRecordId) {
		return dataOperator.bookIdQuery(lendRecordId);
	}

	// 根据图书标识查询出图书信息    参数：bookId:图书顺序标识号
	public static String QueryBookInfo(int bookId) {
		return dataOperator.queryBookInfo(bookId);
	}

	// 还书操作 参数：lendRecordId:借书记录标识号，bookId:图书顺序标识号，userId:用户名（学号）,lnndQuantify:借出数量
	public static int ReturnBook(int lendRecordId, int bookId, long userId, int lendQuantify) {
		return dataOperator.returnBook(lendRecordId, bookId, userId, lendQuantify);
	}
}
