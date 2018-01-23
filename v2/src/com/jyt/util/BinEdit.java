package com.jyt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;



public class BinEdit {
	public static final int LEN = 20;
	public static boolean debug = false;

	public static void _debug_in(String info) {
		if (debug)
			System.out.println(" ===>BinEdit::" + info);
	}

	public static void _debug_out(String info) {
		if (debug)
			System.out.println(" <===BinEdit::" + info);
	}

	public static void _debug_info(String info) {
		if (debug)
			System.out.println(" <===>BinEdit::" + info);
	}
	
	final static char[] digits = { '0', '1' };
	/**
	 * 将byte类型转换成二进制流
	 * @param b byte类型的数值
	 * @return 返回b所对应的0,1字符串流
	 */
	public static String toBinaryString(byte b) {
		char[] buff = new char[8];
		int charPos = 8;
		do {
			buff[--charPos] = digits[b & 1];
			b >>>= 1;
		} while (charPos > 0);

		return new String(buff);
	}

	public static String toCharString(byte b) {
		String str;
		switch (b) {
		case 10:
			str = "a";
			break;
		case 11:
			str = "b";
			break;
		case 12:
			str = "c";
			break;
		case 13:
			str = "d";
			break;
		case 14:
			str = "e";
			break;
		case 15:
			str = "f";
			break;
		default:
			str = b + "";
		}
		return str;
	}

	public static String toByteString(byte b) {
		byte b1;
		byte b2;
		b1 = (byte) ((b & 0xf0) >> 4);
		b2 = (byte) ((b & 0x0f));
		return toCharString(b1) + toCharString(b2);
	}

	public static String toByteString(byte[] bs) {
		String ret = "";
		for (int i = 0; i < bs.length; i++) {
			ret = ret + toByteString(bs[i]);
		}
		return ret;
	}

	public static void printTitle() {
		String str;
		System.out.print("    ");
		for (int i = 0; i < LEN; i++) {
			str = i + "";
			if (i < 10)
				System.out.print("  " + str);
			else
				System.out.print(" " + str);
		}
		System.out.print("   ----+----+----+----+");

		System.out.println();
	}

	public static String toPrintStringBytes(byte[] b) {
		String ret = "";
		String str;
		byte[] b_tmp = new byte[LEN];
		int left = LEN;

		int j = 0;
		int start = 0;
		for (int i = 0; i < b.length; i++) {
			if ((i % LEN) == 0) {
				if (j < 10)
					ret = ret + "   " + j;
				else if (j < 100)
					ret = ret + "  " + j;
				else if (j < 1000)
					ret = ret + " " + j;
				else
					ret = ret + "" + j;
			}
			String s = toByteString(b[i]);
			ret = ret + " " + s;
			left--;

			b_tmp[start] = b[i];
			start++;

			j++;

			if (((i + 1) % LEN) == 0) {
				System.out.print("   ");
				for (int k = 0; k < start; k++) {
					ret = ret + returnAscii(b_tmp[k]);
				}
				ret = ret + "\r\n";
				start = 0;
				left = LEN;
			}

		}

		for (int i = 0; i < left + 1; i++)
			ret = ret + "   ";
		for (int k = 0; k < start; k++) {
			ret = ret + returnAscii(b_tmp[k]);
		}
		ret = ret + "\r\n";

		return ret;
	}

	public static void printBytes(byte[] b) {
		String str;
		byte[] b_tmp = new byte[LEN];
		int left = LEN;

		int j = 0;
		int start = 0;
		for (int i = 0; i < b.length; i++) {
			if ((i % LEN) == 0) {
				if (j < 10)
					System.out.print("   " + j);
				else if (j < 100)
					System.out.print("  " + j);
				else if (j < 1000)
					System.out.print(" " + j);
				else
					System.out.print("" + j);
			}
			String s = toByteString(b[i]);
			System.out.print(" " + s);
			left--;

			b_tmp[start] = b[i];
			start++;

			j++;

			if (((i + 1) % LEN) == 0) {
				System.out.print("   ");
				for (int k = 0; k < start; k++) {
					System.out.print(returnAscii(b_tmp[k]));
				}
				System.out.println();
				start = 0;
				left = LEN;
			}

		}

		for (int i = 0; i < left + 1; i++)
			System.out.print("   ");
		for (int k = 0; k < start; k++) {
			System.out.print(returnAscii(b_tmp[k]));
		}
		System.out.println();

	}

	public static String returnAscii(byte b) {
		char c;

		c = (char) b;
		if ((c >= 32) && (c < 126))
			return c + "";
		else
			return ".";

	}

	public static int retInt(byte b) {
		int ret;
		if (b >= 0)
			ret = b;
		else
			ret = 256 + b;
		return ret;

	}

	public static int retInt(byte b1, byte b2) {
		int ret;
		ret = retInt(b1) * 256 + retInt(b2);
		return ret;

	}

	public static void printAscii(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.print("  " + returnAscii(b[i]));
		}
		System.out.println();
	}

	// get all bytes from file
	public static byte[] getBytes(String fileName) {
		byte[] b = null;
		try {
			File f = new File(fileName);
			FileInputStream fi = new FileInputStream(f);
			b = new byte[(int) f.length()];
			int data;
			int j = 0;
			// System.out.println("The file length is:"+f.length());
			while ((data = fi.read()) != -1) {
				b[j] = (byte) data;
				j++;
			}
			fi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public static byte getByteFromArr(byte[] b, int pos) {
		return b[pos];
	}

	// the most right is zero, the most left is 7
	public static boolean testbit(byte b, int pos) {
		if (pos > 7) {
			System.out.println("Error in BinEdit::testbit");
			return false;
		}

		byte b1 = (byte) ((b & 0xff) << (7 - pos));
		byte b2 = (byte) ((b1 & 0xff) >> 7);
		// System.out.println("b1="+b1+"\tb2="+b2);
		if (b2 == 1)
			return true;
		else
			return false;
	}

	public static byte[] getBytesFromArr(byte[] b, int pos, int length) {
		byte[] ret = new byte[length];
		for (int i = 0; i < length; i++) {
			if (i + pos > b.length) {
				System.out.println("In Function BinEdit::getBytesFromArr");
				System.out.println("Warning: get bytes out of Bind");
				break;
			}
			ret[i] = b[i + pos];
		}
		return ret;

	}

	public static byte[] getMarkerBytes(byte[] b, int pos) {
		_debug_in("getMarkerBytes");
		byte b1, b2, b3, b4;
		byte[] ret = null;
		b1 = b[pos];
		b2 = b[pos + 1];
		b3 = b[pos + 2];
		b4 = b[pos + 3];
		if (!toByteString(b1).equalsIgnoreCase("FF")) {
			_debug_out("getMarkerBytes" + 1);
			return ret;
		}
		if (toByteString(b1).equalsIgnoreCase("FF")
				&& toByteString(b2).equalsIgnoreCase("d8")) {
			ret = new byte[2];
			ret[0] = b1;
			ret[1] = b2;
			_debug_out("getMarkerBytes" + 2);
			return ret;
		}
		long l = retInt(b3, b4);
		System.out.println(" b1=" + toByteString(b1) + " b2="
				+ toByteString(b2) + " b3=" + toByteString(b3) + " b4="
				+ toByteString(b4) + " L is :" + l);
		ret = new byte[(int) l + 2];
		for (int i = 0; i < l + 2; i++) {
			ret[i] = b[pos + i];
		}
		_debug_out("getMarkerBytes" + 3);

		return ret;
	}

	public static void save(byte[] b, String fileName) {
		try {
			FileOutputStream fo = new FileOutputStream(fileName);
			int data;
			for (int i = 0; i < b.length; i++) {
				data = (int) b[i];
				fo.write(data);
			}
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] insert(byte[] b1, byte[] b2, int pos) {
		byte[] ret = null;
		int start = 0;
		if (pos > b1.length)
			return ret;
		ret = new byte[b1.length + b2.length];
		for (int i = 0; i < pos; i++) {
			ret[i] = b1[i];
		}
		start = pos;
		for (int i = 0; i < b2.length; i++) {
			ret[i + start] = b2[i];
		}
		start = start + b2.length;
		for (int i = 0; i < b1.length - pos; i++) {
			ret[i + start] = b1[i + pos];
		}
		return ret;
	}

	public static byte[] delete(byte[] b, int pos, int len) {
		byte[] ret = new byte[b.length - len];
		for (int i = 0; i < pos; i++) {
			ret[i] = b[i];
		}
		for (int i = pos; i < b.length - len; i++) {
			ret[i] = b[len + i];
		}
		return ret;
	}

	public static int search(byte[] b, byte[] b1) {
		int ret = -1;
		int pos = 0;
		for (int i = 0; i < b.length; i++) {
			boolean match = true;
			for (int j = 0; j < b1.length; j++) {
				if (pos + j > b.length) {
					match = false;
					break;
				}
				if (b[pos + j] != b1[j]) {
					match = false;
					break;
				}
			}
			if (match) {
				ret = pos;
				break;
			} else {
				pos++;
			}
		}
		return ret;
	}

	public static byte binValue(char c) {
		byte ret = 0;
		if (c == '0') {
			ret = 0;
		}
		if (c == '1') {
			ret = 1;
		}
		if (c == '2') {
			ret = 2;
		}
		if (c == '3') {
			ret = 3;
		}
		if (c == '4') {
			ret = 4;
		}
		if (c == '5') {
			ret = 5;
		}
		if (c == '6') {
			ret = 6;
		}
		if (c == '7') {
			ret = 7;
		}
		if (c == '8') {
			ret = 8;
		}
		if (c == '9') {
			ret = 9;
		}
		if (c == 'a') {
			ret = 10;
		}
		if (c == 'b') {
			ret = 11;
		}
		if (c == 'c') {
			ret = 12;
		}
		if (c == 'd') {
			ret = 13;
		}
		if (c == 'e') {
			ret = 14;
		}
		if (c == 'f') {
			ret = 15;
		}
		if (c == 'A') {
			ret = 10;
		}
		if (c == 'B') {
			ret = 11;
		}
		if (c == 'C') {
			ret = 12;
		}
		if (c == 'D') {
			ret = 13;
		}
		if (c == 'E') {
			ret = 14;
		}
		if (c == 'F') {
			ret = 15;
		}
		return ret;
	}

	public static byte[] binValueOf(String str) {
		byte[] tmp = new byte[str.length() / 2 + str.length() % 2];
		boolean odd = true;
		int j = 0;
		for (int i = 0; i < str.length(); i++) {
			if (odd) {
				tmp[j] = (byte) (binValue(str.charAt(i)) << 4);
				odd = false;
			} else {
				tmp[j] = (byte) (tmp[j] | binValue(str.charAt(i)));
				odd = true;
				j++;
			}
		}
		return tmp;

	}

	public static byte[] longToBytes(long l) {
		byte[] ret = new byte[4];
		ret[0] = (byte) (l >> 24);
		ret[1] = (byte) ((l << 8) >> 24);
		ret[2] = (byte) ((l << 16) >> 24);
		ret[3] = (byte) ((l << 24) >> 24);
		return ret;
	}

	public static byte[] shortToBytes(short s) {
		byte[] ret = new byte[2];
		ret[0] = (byte) (s >> 8);
		ret[1] = (byte) ((s << 8) >> 8);
		return ret;
	}

	// 00 00 00 0d is 14
	public static long bytesToLong(byte[] b) {
		if (b.length != 4) {
			return 0;
		}
		long l = 0;
		for (int i = 0; i < 4; i++) {
			long tmp;
			tmp = ((b[i] & 0xff) << ((3 - i) * 8));
			l = l | tmp;
		}
		return l;
	}

	public static int twoBytesToInt(byte[] b) {
		if (b.length != 2) {
			System.out.println("error in twoBytesToInt");
			return 0;
		}
		int l = 0;
		for (int i = 0; i < 2; i++) {
			int tmp;
			tmp = ((b[i] & 0xff) << ((1 - i) * 8));
			l = l | tmp;
		}
		return l;
	}

	public static boolean replace(byte[] b1, byte[] b2, int pos) {
		if (b2.length + pos > b1.length) {
			return false;
		}
		for (int i = 0; i < b2.length; i++) {
			b1[i + pos] = b2[i];
		}
		return true;
	}

	public static byte[] append(byte[] b1, byte[] b2) {
		if (b1 == null)
			return b2;
		if (b2 == null)
			return b1;
		byte[] ret = new byte[b1.length + b2.length];
		for (int i = 0; i < b1.length; i++) {
			ret[i] = b1[i];
		}
		for (int i = 0; i < b2.length; i++) {
			ret[i + b1.length] = b2[i];
		}
		return ret;
	}

	public static void main1(String[] args) {

		String s = "刘涛";
		try {
			System.out.println("not change");
			byte[] b0 = s.getBytes();
			System.out.println(toPrintStringBytes(b0));
			System.out.println("java->gb2312");
			byte[] b1 = s.getBytes("gb2312");
			System.out.println(toPrintStringBytes(b1));
			System.out.println("java->utf-8");
			byte[] b2 = s.getBytes("utf-8");
			System.out.println(toPrintStringBytes(b2));
			System.out.println("java->ISO-8859-1");
			byte[] b3 = s.getBytes("ISO-8859-1");
			System.out.println(toPrintStringBytes(b3));
			System.out.println("java->GBK");
			byte[] b4 = s.getBytes("GBK");
			printBytes(b4);
			String s3 = new String(b2, "utf-8");
			System.out.println("s3=" + s3);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 逆转字节数组
	 * 
	 * @param b
	 * @return
	 */
	private static byte[] reverse(byte[] b) {

		byte[] temp = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			temp[i] = b[b.length - 1 - i];
		}
		return temp;
	}



	public static final void write_int(int n, byte[] bs) {

		bs[1] = (byte) (n % 256);
		bs[0] = (byte) (n / 256);
		return;
	}
	
	public static final int read_int(byte[] bs)
	{
		int ret = retInt(bs[0],bs[1]);
		return ret;
	}

	public static void main(String[] args) throws Exception {
		byte[] bs = new byte[2];
		write_int(52390, bs);
		int bi = read_int(bs);
		System.out.println(bi);

		byte[] bs2 = new byte[] { 0x00,0x3E };
		int bi2 = read_int(bs2);
		System.out.println(bi2);

	}
	
	public static void main3(String[] args)
	{
		byte[] bs1 = new byte[]{(byte)10};
		byte[] bs2 = new byte[]{(byte)11};
		byte[] bs3 = append(bs1,bs2);
		System.out.println(bs3.length);
		System.out.println(toByteString(bs3));
		
	}
	
	

	
	public static byte[] int_to_byte4(int n)
	{
		byte[] bs = null;
		int tmp = n;
		byte bs1 = (byte)(tmp & 0x000000FF);
		tmp = tmp >> 8;
		byte bs2 = (byte)(tmp & 0x000000FF);
		tmp = tmp >> 8;
		byte bs3 = (byte)(tmp & 0x000000FF);
		tmp = tmp >> 8;
		byte bs4 = (byte)(tmp & 0x000000FF);
		bs = new byte[]{bs4,bs3,bs2,bs1};
		return bs;
	}
	
	public static byte[] float_to_byte4(float f)
	{
		byte[] bs = null;
		float tmp_float = f;
		int tmp = Float.floatToIntBits(tmp_float);
		byte bs1 = (byte)(tmp & 0x000000FF);
		tmp = tmp >> 8;
		byte bs2 = (byte)(tmp & 0x000000FF);
		tmp = tmp >> 8;
		byte bs3 = (byte)(tmp & 0x000000FF);
		tmp = tmp >> 8;
		byte bs4 = (byte)(tmp & 0x000000FF);
		bs = new byte[]{bs4,bs3,bs2,bs1};
		return bs;
	}
	
	public static int byte4_to_int(byte[] bs)
	{
		String s = "";
		for(int i=0;i<bs.length;i++)
		{
			s = s+toByteString(bs[i]);
		}
		int ret = (int)Long.parseLong(s,16);
		return ret;
	}	
	
	public static float byte4_to_float(byte[] bs)
	{
		String s = "";
		for(int i=0;i<bs.length;i++)
		{
			s = s+toByteString(bs[i]);
		}
		int ret = (int)Long.parseLong(s,16);
		float ret_float = Float.intBitsToFloat(ret);
		return ret_float;
	}		
	
	public static void print_byte4(byte[] bs)
	{
		for(int i=0;i<4;i++)
		{
			String s = toByteString(bs[i]);
			System.out.print(s);
		}
		System.out.println();
	}
	public static void main5(String[] args)
	{
		String h = "你好，王先生。";
		byte[] bs = h.getBytes();
		String s1 = BinEdit.toByteString(bs);
		System.out.println(s1);
		byte[] bs2 = BinEdit.binValueOf(s1);
		String h2 = new String(bs2);
		System.out.println(h2);
	}
	public static void main6(String[] args)
	{
		byte[] mask = BinEdit.binValueOf("00000102030405060708090001020304050607080900");
		int mask_len = 22;
		MyPrint.print(BinEdit.toByteString(mask), new Exception());
		byte[] new_mask = Arrays.copyOf(mask, mask_len);
		new_mask[0] = (byte) (new_mask[0] & 0x00);
		new_mask[1] = (byte) (new_mask[1] & 0x00);
		new_mask[2] = (byte) (new_mask[2] & 0x00);
		new_mask[3] = (byte) (new_mask[3] & 0x00);
		new_mask[4] = (byte) (new_mask[4] & 0x00);
		new_mask[5] = (byte) (new_mask[5] & 0x00);
		new_mask[6] = (byte) (new_mask[6] & 0x1b);// 0x6003
		new_mask[7] = (byte) (new_mask[7] & 0xff);
		new_mask[8] = (byte) (new_mask[8] & 0x00);// 0x6004
		new_mask[9] = (byte) (new_mask[9] & 0x3f);
		new_mask[10] = (byte) (new_mask[10] & 0x00);// 0x6005
		new_mask[11] = (byte) (new_mask[11] & 0x7f);
		new_mask[12] = (byte) (new_mask[12] & 0x08);// 0x6006
		new_mask[13] = (byte) (new_mask[13] & 0xff);
		new_mask[14] = (byte) (new_mask[14] & 0x03);// 0x6007
		new_mask[15] = (byte) (new_mask[15] & 0xff);
		new_mask[16] = (byte) (new_mask[16] & 0x00);// 0x6008
		new_mask[17] = (byte) (new_mask[17] & 0x00);
		new_mask[18] = (byte) (new_mask[18] & 0x00);// 0x6009
		new_mask[19] = (byte) (new_mask[19] & 0x36);
		new_mask[20] = (byte) (new_mask[20] & 0x00);// 0x600a
		new_mask[21] = (byte) (new_mask[21] & 0x07);
		MyPrint.print(BinEdit.toByteString(new_mask), new Exception());
		for(int i=0;i<22;i++)
		{
			MyPrint.print(i+")"+BinEdit.toBinaryString(new_mask[i]), new Exception());
		}
		
	}

}
