/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 5. 1.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm;

import org.apache.commons.codec.DecoderException;

/**
 * @author KYJ
 *
 */
public class Hex {

//	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static char[] encode(byte[] bytes) {
		return org.apache.commons.codec.binary.Hex.encodeHex(bytes);
//		final int nBytes = bytes.length;
//		char[] result = new char[2 * nBytes];
//
//		int j = 0;
//		for (int i = 0; i < nBytes; i++) {
//			// Char for top 4 bits
//			result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
//			// Bottom 4
//			result[j++] = HEX[(0x0F & bytes[i])];
//		}
//
//		return result;
	}

	public static byte[] decode(char[] chars) {
		try {
			return org.apache.commons.codec.binary.Hex.decodeHex(chars);
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] decode(CharSequence s)  {
		char[] chars = null;
		int size = s.length();
		chars = new char[size];
		
		for(int i=0; i< size; i++)
			chars[i] = s.charAt(i); 
		try {
			return org.apache.commons.codec.binary.Hex.decodeHex(chars);
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}

}
