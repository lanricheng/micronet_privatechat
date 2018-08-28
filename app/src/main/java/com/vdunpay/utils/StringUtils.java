package com.vdunpay.utils;

import java.io.UnsupportedEncodingException;
public class StringUtils {

	/***
	 * int转多位Hex
	 *
	 * @param in
	 *            输入
	 * @param len
	 *            转换出来的Hex位数
	 * @return Hex
	 * @author pagekpang@gmail.com
	 */
	public static String intToHex(int in, int len) {
		String retString = "";
		int localInt = in;
		byte localbyte[] = new byte[len];
		for (int i = 0; i < localbyte.length; i++) {
			localbyte[len - i - 1] = (byte) ((localInt >> (i * 8)) & (0xff));
		}
		retString = bytesToHex(localbyte);
		return retString;
	}

	/***
	 * USC字符串转字符
	 *
	 * @param ucs
	 *            USC字符串
	 * @return
	 * @author pagekpang@gmail.com
	 */
	public static String ucs2ToCharacter(String ucs) {
		byte[] bUnicode = hexToBytes(ucs);
		String sTemp = null;
		try {
			sTemp = new String(bUnicode, "UTF-16BE");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return sTemp;
	}

	public static String characterTousc2(String zw, boolean f80) {
		byte[] b = null;
		try {
			b = zw.getBytes("UnicodeBigUnmarked");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		int nTmp = 0;
		String strTmp = "";
		StringBuffer strBuffer = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			nTmp = b[i] & 0xff;
			strTmp = Integer.toHexString(nTmp);
			while (strTmp.length() < 2) {
				strTmp = "0" + strTmp;
			}
			strTmp = strTmp.toUpperCase();
			strBuffer.append(strTmp);
		}
		String returnstr = strBuffer.toString();
		if (f80)
			returnstr = "80" + returnstr;
		else {
			return returnstr;
		}
		return returnstr;
	}

	private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static final String bytesToHex(byte[] bcd) {
		if (bcd == null) {
			return "";
		}
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++) {
			s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
			s.append(bcdLookup[bcd[i] & 0x0f]);
		}

		return s.toString();
	}

	public static final byte[] hexToBytes(String s) {
		byte[] bytes;
		if (s == null || s.length() == 0) {
			return null;
		}
		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}

	public static String merge(String[] s, String spliter) {
		String retString = "";

		for (String string : s) {
			retString += string + spliter;
		}
		retString = retString.substring(retString.length() - spliter.length());
		return retString;
	}
}
