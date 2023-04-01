/**
 *
 */
package com.kyj.fx.nightmare.comm;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * MAC(12)-JVM(6)-DATETIME(12)-SEQ(3) 형태의 Unique한 Id를 생성. DATETIME(1000분의1초까지의
 * 값), JVM, SEQ 값들은 36진수 값으로 표현.
 *
 * @name_ko 아이디 생성 유틸 클래스
 * @author WiseBell
 */
public class IdGenUtil {
	private static short counter = (short) 0;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 4);
	private static String MACHINE = "";
	private static boolean initiaized = false;

	// static {
	// StringBuffer buf = new StringBuffer();
	// try {
	// InetAddress ia = InetAddress.getLocalHost();
	// NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
	// byte[] mac = ni.getHardwareAddress();
	//
	// for (int i = 0; i < 6; i++) {
	// buf.append(String.format("%02x", mac[i]));
	// }
	// } catch (Exception e) {
	// buf = new StringBuffer("000000000000");
	// }
	//
	// MACHINE = buf.toString();
	// }

	private static void initialize() {
		if (initiaized) {
			return;
		}
		synchronized (IdGenUtil.class) {
			StringBuffer buf = new StringBuffer();
			try {
				InetAddress ia = InetAddress.getLocalHost();
				NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
				byte[] mac = ni.getHardwareAddress();

				for (int i = 0; i < 6; i++) {
					buf.append(String.format("%02x", mac[i]));
				}
			} catch (Exception e) {
				buf = new StringBuffer("000000000000");
			}

			MACHINE = buf.toString();
			initiaized = true;
		}
	}

	/*
	 * private static final int IP; static { int ipadd; try { ipadd = toInt(
	 * InetAddress.getLocalHost( ).getAddress( ) ); } catch ( Exception e ) {
	 * ipadd = 0; } IP = ipadd; }
	 *
	 * private static final long MAC; static { long macadd; try { InetAddress ia
	 * = InetAddress.getLocalHost( ); NetworkInterface ni =
	 * NetworkInterface.getByInetAddress( ia ); macadd = toLong(
	 * ni.getHardwareAddress( ) ); } catch ( Exception e ) { macadd = 0; } MAC =
	 * macadd; }
	 *
	 * /* private static int getIP( ) { return IP; }
	 *
	 * private static int toInt( byte[ ] bytes ) { int result = 0; for ( int i =
	 * 0; i < 4; i++ ) { result = ( result << 8 ) - Byte.MIN_VALUE + (int)
	 * bytes[ i ]; } return result; }
	 *
	 * private static long toLong( byte[ ] bytes ) { long result = 0; for ( int
	 * i = 0; i < 6; i++ ) { result = ( result << 8 ) - Byte.MIN_VALUE + (int)
	 * bytes[ i ]; } return result; }
	 */

	/**
	 * Unique across JVMs on this machine (unless they load this class in the
	 * same quater second - very unlikely)
	 */
	private static int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there are >
	 * Short.MAX_VALUE instances created in a millisecond)
	 */
	public static short getCount() {
		synchronized (IdGenUtil.class) {
			if (counter < 0) {
				counter = 0;
			}

			return counter++;
		}
	}

	/**
	 * Unique in a network
	 */
	public static String getMachine() {
		initialize();
		return MACHINE;
	}

	public static String format00(int intValue) {
		String formatted = NumeralConverter.to36(intValue);
		StringBuffer buf = new StringBuffer("00");
		buf.replace(2 - formatted.length(), 2, formatted);
		return buf.toString();
	}

	public static String format000(int intValue) {
		String formatted = NumeralConverter.to36(intValue);
		StringBuffer buf = new StringBuffer("000");
		buf.replace(3 - formatted.length(), 3, formatted);
		return buf.toString();
	}

	public static String format(int intValue) {
		String formatted = NumeralConverter.to36(intValue);
		StringBuffer buf = new StringBuffer("000000");
		buf.replace(6 - formatted.length(), 6, formatted);
		return buf.toString();
	}

	public static String format(short shortValue) {
		String formatted = NumeralConverter.to36(shortValue);
		StringBuffer buf = new StringBuffer("000");
		buf.replace(3 - formatted.length(), 3, formatted);
		return buf.toString();
	}

	public static String getTimestamp17() {
		String formatted = DateUtil.getCurrentDateString(DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSSS);
		StringBuffer buf = new StringBuffer("00000000000000000");
		buf.replace(0, formatted.length(), formatted);
		return buf.toString();
	}

	public static String getTimestamp() {
		Calendar today = Calendar.getInstance();
		StringBuffer buf = new StringBuffer();
		buf.append(format000(today.get(Calendar.YEAR)));
		buf.append(NumeralConverter.to(today.get(Calendar.MONTH) + 1, 36));
		buf.append(NumeralConverter.to(today.get(Calendar.DAY_OF_MONTH), 36));
		buf.append(NumeralConverter.to(today.get(Calendar.HOUR_OF_DAY), 36));
		buf.append(format00(today.get(Calendar.MINUTE)));
		buf.append(format00(today.get(Calendar.SECOND)));
		buf.append(format00(today.get(Calendar.MILLISECOND)));
		return buf.toString();
	}

	public static String generate() {
		StringBuffer buf = new StringBuffer(33);
		buf.append(getMachine());
		buf.append(format(getJVM()));
		buf.append(getTimestamp());
		buf.append(format(getCount()));

		return buf.toString().toLowerCase();
	}

	public static String generate(char seperator) {
		StringBuffer buf = new StringBuffer(36);
		buf.append(getMachine()).append(seperator);
		buf.append(format(getJVM())).append(seperator);
		buf.append(getTimestamp()).append(seperator);
		buf.append(format(getCount()));

		return buf.toString().toLowerCase();
	}

	public static String generateRand36(int length) {
		StringBuffer buf = new StringBuffer();
		SecureRandom rand = new SecureRandom();
		for (int i = 0; i < length; i++) {
			buf.append(NumeralConverter.to36(rand.nextInt(36)));
		}
		return buf.toString();
	}
	
	/**
	 * @작성자 : KYJ (zaruous@naver.com)
	 * @작성일 : 2021. 12. 8. 
	 * @return
	 */
	public static String randomGuid() {
		return UUID.randomUUID().toString();
	}
	
	public class NumeralConverter {

		public static void main(String[] args) throws Exception {
			// 10진수 값을 지정한 진수 값으로 변환
			int value = 291;

			System.out.println(flip(291));
			System.out.println(flip(-291));

			String n16 = NumeralConverter.to(value, 16);
			String n21 = NumeralConverter.to(value, 21);
			String n32 = NumeralConverter.to(value, 32);
			String n36 = NumeralConverter.to(value, 36);

			// n진수 값을 10진수 값으로 변환
			value = NumeralConverter.to10(n16, 16);
			value = NumeralConverter.to10(n21, 21);
			value = NumeralConverter.to10(n32, 32);
			value = NumeralConverter.to10(n36, 36);

			// 10진수 값 리스트를 n진수 값 리스트로 변환
			int[] values = new int[] { 10, 21, 32, 36, 39 };
			List<Integer> valueList = new ArrayList<Integer>();
			for (int v : values) {
				valueList.add(v);
			}
			List<String> result16List = NumeralConverter.to(valueList, 16);

			// n진수 값 리스트를 m진수 값 리스트로 변환
			NumeralConverter.to(result16List, 16, 21);
			NumeralConverter.to(result16List, 16, 32);
			NumeralConverter.to(result16List, 16, 36);

			// 10진수 값을 지정한 진수 값으로 변환
			n16 = NumeralConverter.to16(value);
			n21 = NumeralConverter.to21(value);

			// n진수 값을 지정한 진수 값으로 변환
			n32 = NumeralConverter.to32(n16, 16);
			n36 = NumeralConverter.to36(n21, 21);

			// n진수 값의 x만큼 증가한 값을 얻어옴
			n16 = NumeralConverter.getNext(n16, 16, 36);
			n21 = NumeralConverter.getNext(n21, 21, 36);
			n32 = NumeralConverter.getNext(n32, 32, 36);
			n36 = NumeralConverter.getNext(n36, 36, 36);

			// n진수 값의 다음값(1증가한 값)을 얻어옴
			n16 = NumeralConverter.getNext(n16, 16);
			n21 = NumeralConverter.getNext(n21, 21);
			n32 = NumeralConverter.getNext(n32, 32);
			n36 = NumeralConverter.getNext(n36, 36);

			// 진수 값의 다음값(1증가한 값)을 얻어옴
			n16 = NumeralConverter.getNext16(n16);
			n21 = NumeralConverter.getNext21(n21);
			n32 = NumeralConverter.getNext32(n32);
			n36 = NumeralConverter.getNext36(n36);
		}

		/* 진법 변환에 사용되는 36개 문자셋 */
		private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		/**
		 * 10진수 값 {@code value}를 {@code numeral}진수 값으로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 10진수 값
		 * @param numeral
		 *            변환하고자 하는 진수 (16, 21, 32, 36)
		 * @return 변환된 진수 값
		 */
		public static String to(int value, int numeral) {
			StringBuffer buffer = new StringBuffer();
			int temp = value;

			if (temp == 0) {
				return "0";
			}

			while (temp != 0) {
				int remainder = temp % numeral;
				temp = temp / numeral;
				buffer.append(DIGITS.charAt(remainder));
			}

			return buffer.reverse().toString();
		}

		/**
		 * 10진수 값 {@code value}를 {@code numeral}진수 값으로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 10진수 값
		 * @param numeral
		 *            변환하고자 하는 진수 (16, 21, 32, 36)
		 * @return 변환된 진수 값
		 */
		public static String to(long value, int numeral) {
			StringBuffer buffer = new StringBuffer();
			long temp = value;

			if (temp == 0) {
				return "0";
			}

			while (temp != 0) {
				int remainder = (int) (temp % numeral);
				temp = temp / numeral;
				buffer.append(DIGITS.charAt(remainder));
			}

			return buffer.reverse().toString();
		}

		/**
		 * {@code numeral}진수 값 {@code value}를 10진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 진수 값
		 * @param numeral
		 *            변환 대상의 진수 (16, 21, 32, 36)
		 * @return 10진수로 변환된 값
		 * @throws Exception
		 */
		public static int to10(String value, int numeral) throws Exception {
			int result = 0;
			String temp = value.toUpperCase();

			for (int i = 0; i < temp.length(); i++) {
				char digit = temp.charAt(i);
				int index = DIGITS.indexOf(digit);
				if (index == -1 || index >= numeral) {
					throw new IllegalArgumentException("Invalid value");
				}

				result = result * numeral + index;
			}
			return result;
		}

		/**
		 * {@code numeral}진수 값 {@code value}를 10진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 진수 값
		 * @param numeral
		 *            변환 대상의 진수 (16, 21, 32, 36)
		 * @return 10진수로 변환된 값
		 * @throws Exception
		 */
		public static long toLong10(String value, int numeral) throws Exception {
			long result = 0;
			String temp = value.toUpperCase();

			for (int i = 0; i < temp.length(); i++) {
				char digit = temp.charAt(i);
				int index = DIGITS.indexOf(digit);
				if (index == -1 || index >= numeral) {
					throw new IllegalArgumentException("Invalid value");
				}

				result = result * numeral + index;
			}
			return result;
		}

		/**
		 * 10진수를 16진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 10진수 값
		 * @return 16진수로 변환된 값
		 */
		public static String to16(int value) {
			return to(value, 16);
		}

		public static String to16(boolean value) {
			return value ? "1" : "0";
		}

		public static String to16(String value) {
			return to16(Integer.parseInt(value));
		}

		/**
		 * 10진수를 21진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 10진수 값
		 * @return 21진수로 변환된 값
		 */
		public static String to21(int value) {
			return to(value, 21);
		}

		/**
		 * 10진수를 32진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 10진수 값
		 * @return 32진수로 변환된 값
		 */
		public static String to32(int value) {
			return to(value, 32);
		}

		/**
		 * 10진수를 36진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 10진수 값
		 * @return 36진수로 변환된 값
		 */
		public static String to36(int value) {
			return to(value, 36);
		}

		/**
		 * {@code numeral}진수 값 {@code value}를 16진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 진수 값
		 * @param numeral
		 *            변환 대상의 진수 (16, 21, 32, 36)
		 * @return 16진수로 변환된 값
		 * @throws Exception
		 */
		public static String to16(String value, int numeral) throws Exception {
			return to(to10(value, numeral), 16);
		}

		/**
		 * {@code numeral}진수 값 {@code value}를 21진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 진수 값
		 * @param numeral
		 *            변환 대상의 진수 (16, 21, 32, 36)
		 * @return 21진수로 변환된 값
		 * @throws Exception
		 */
		public static String to21(String value, int numeral) throws Exception {
			return to(to10(value, numeral), 21);
		}

		/**
		 * {@code numeral}진수 값 {@code value}를 32진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 진수 값
		 * @param numeral
		 *            변환 대상의 진수 (16, 21, 32, 36)
		 * @return 32진수로 변환된 값
		 * @throws Exception
		 */
		public static String to32(String value, int numeral) throws Exception {
			return to(to10(value, numeral), 32);
		}

		/**
		 * {@code numeral}진수 값 {@code value}를 36진수로 변환합니다.
		 *
		 * @param value
		 *            변환 대상이 되는 진수 값
		 * @param numeral
		 *            변환 대상의 진수 (16, 21, 32, 36)
		 * @return 36진수로 변환된 값
		 * @throws Exception
		 */
		public static String to36(String value, int numeral) throws Exception {
			return to(to10(value, numeral), 36);
		}

		/**
		 * 10진수 값 리스트 {@code valueList}의 값들을 {@code toNumeral}진수 값들로 변환합니다.
		 *
		 * @param valueList
		 *            변환 대상이 되는 10진수 값 리스트
		 * @param toNumeral
		 *            변환하고자 하는 진수 (16, 21, 32, 36)
		 * @return 변환된 값 리스트
		 * @throws Exception
		 */
		public static List<String> to(List<Integer> valueList, int toNumeral) throws Exception {
			List<String> resultList = new ArrayList<String>();

			for (int value : valueList) {
				resultList.add(to(value, toNumeral));
			}

			return resultList;
		}

		/**
		 * {@code fromNumeral}진수 값 리스트 {@code valueList}의 값들을 {@code toNumeral}진수
		 * 값들로 변환합니다.
		 *
		 * @param valueList
		 *            변환 대상이 되는 진수 값 리스트
		 * @param fromNumeral
		 *            변환 대상 진수 (16, 21, 32, 36)
		 * @param toNumeral
		 *            변환하고자 하는 진수 (16, 21, 36)
		 * @return 변환된 값 리스트
		 * @throws Exception
		 */
		public static List<String> to(List<String> valueList, int fromNumeral, int toNumeral) throws Exception {
			List<String> resultList = new ArrayList<String>();

			for (String value : valueList) {
				resultList.add(to(to10(value, fromNumeral), toNumeral));
			}

			return resultList;
		}

		/**
		 * {@code numeral}진수 값 {@code value}를 {@code step}만큼 증가시킨 값을 얻어옵니다.
		 *
		 * @param value
		 *            대상이 되는 진수 값
		 * @param numeral
		 *            대상 진수
		 * @param step
		 *            증가시키고자 하는 값의 크기
		 * @return 값이 증가된 진수 값
		 * @throws Exception
		 */
		public static String getNext(String value, int numeral, int step) throws Exception {
			return to(to10(value, numeral) + step, numeral);
		}

		/**
		 * {@code numeral}진수 값 {@code value}의 다음값(1 증가한 값)을 얻어옵니다.
		 *
		 * @param value
		 *            대상이 되는 진수 값
		 * @param numeral
		 *            대상 진수
		 * @return 값이 증가된 진수 값
		 * @throws Exception
		 */
		public static String getNext(String value, int numeral) throws Exception {
			return getNext(value, numeral, 1);
		}

		/**
		 * 16진수 값 {@code value}의 다음값(1 증가시킨 값)을 얻어옵니다.
		 *
		 * @param value
		 *            대상이되는 16진수 값
		 * @return 값이 증가된 16진수 값
		 * @throws Exception
		 */
		public static String getNext16(String value) throws Exception {
			return getNext(value, 16, 1);
		}

		/**
		 * 21진수 값 {@code value}의 다음값(1 증가시킨 값)을 얻어옵니다.
		 *
		 * @param value
		 *            대상이되는 21진수 값
		 * @return 값이 증가된 21진수 값
		 * @throws Exception
		 */
		public static String getNext21(String value) throws Exception {
			return getNext(value, 21, 1);
		}

		/**
		 * 32진수 값 {@code value}의 다음값(1 증가시킨 값)을 얻어옵니다.
		 *
		 * @param value
		 *            대상이되는 32진수 값
		 * @return 값이 증가된 32진수 값
		 * @throws Exception
		 */
		public static String getNext32(String value) throws Exception {
			return getNext(value, 32, 1);
		}

		/**
		 * 36진수 값 {@code value}의 다음값(1 증가시킨 값)을 얻어옵니다.
		 *
		 * @param value
		 *            대상이되는 36진수 값
		 * @return 값이 증가된 36진수 값
		 * @throws Exception
		 */
		public static String getNext36(String value) throws Exception {
			return getNext(value, 36, 1);
		}

		/**
		 * 숫자를 뒤집은 값을 리턴
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 2. 24.
		 * @param num
		 * @return
		 */
		public static int flip(int num) {
			int result = 0;
			while (num != 0) {
				result = result * 10 + num % 10;
				num /= 10;
			}
			return result;
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 3. 25.
		 * @param b
		 * @return
		 */
		public static int unsignedInt(byte b) {
			return Byte.toUnsignedInt(b);
//			return b & 0xFF;
		}
	}
}
