package com.bcloud.common.util;

/**
 * @description 生成随机字符串验证码的工具
 * @author zhangrui
 * @date 2017年4月10日
 */
public class RandomUtils {
	/**
	 * 验证码长度
	 */
	private static int length = 6;
	
	/**
	 * 返回随机串
	 * @return 随机串
	 */
	public static String getRandomStr() {
		return getRandomStr(true, length);
	}
	
	/**
	 * 返回随机串
	 * @param numOrNot 是否数字
	 * @return 随机串
	 */
	public static String getRandomStr(boolean numOrNot) {
		return getRandomStr(numOrNot, length);
	}
	
	/**
	 * 返回随机串
	 * @param numOrNot 是否数字
	 * @param length 长度
	 * @return 随机串
	 */
	public static String getRandomStr(boolean numOrNot, int length) {
		String strTable = numOrNot ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";;
		
		StringBuilder randomStr = new StringBuilder();
		for(int i = 0; i < length; i++) {
			int index = (int) (Math.random()*strTable.length());
			char c = strTable.charAt(index);
			randomStr.append(c);
		}
		
		return randomStr.toString();
	}

	/**
	 * 返回随机串  大写字母
	 * @param numOrNot 是否数字
	 * @param length 长度
	 * @return 随机串
	 */
	public static String getRandomCode(boolean numOrNot, int length) {
		String strTable = numOrNot ? "1234567890" : "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";;

		StringBuilder randomStr = new StringBuilder();
		for(int i = 0; i < length; i++) {
			int index = (int) (Math.random()*strTable.length());
			char c = strTable.charAt(index);
			randomStr.append(c);
		}

		return randomStr.toString();
	}
}
