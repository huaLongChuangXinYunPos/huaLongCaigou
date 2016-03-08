package Tool;

import java.math.BigDecimal;

public class StringUtils {

	public static boolean isInteger(String str) {
		if (str.isEmpty()||str.equals("0")) {
			return false;
		}
		String integers = "^[0-9]*$";
		return str.matches(integers);
	}

	public static boolean isDouble(String str) {
		if (str.isEmpty()||str.equals("0")) {
			return false;
		}
		try {
			 Double.parseDouble(str); //转型成功就是double,不成功就返回false
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String isRounding(double f) {
		BigDecimal b = new BigDecimal(f);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  //保留两位
		return ""+f1;
	}

}
