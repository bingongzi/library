package library;

public class MD5 {
	//简单加密
	public static String GetMD5Code(String password) {
		String sourceStr = "伶掀毓津予撬锣汪扛允主级";
		char[] s = sourceStr.toCharArray();
		char[] p = password.toCharArray();
		int len1 = s.length;
		int len2 = p.length;
		for (int i = 0; i < len2; i++) {
			int t = p[i] + s[i % len1];
			p[i] = (char) t;//对原密码进行加密
		}
		return String.valueOf(p);
	}
	//简单解密
	public static String SetMD5Code(String password) {
		String sourceStr = "伶掀毓津予撬锣汪扛允主级";
		char[] s = sourceStr.toCharArray();
		char[] p = password.toCharArray();
		int len1 = s.length;
		int len2 = p.length;
		for (int i = 0; i < len2; i++) {
			int t = p[i] - s[i % len1];
			p[i] = (char) t;//化为原密码
		}
		return String.valueOf(p);
	}
}
