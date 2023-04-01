/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 11. 11.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.nightmare;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
public class Utils {

	/**
	 * 빈을 Map으로 변환한다. 기본형 데이터만 Map으로 변환한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 9. 25.
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static <T> Map<String, Object> toMap(final T t) {
		List<String> fields = Stream.of(t.getClass().getDeclaredFields()).map(field -> field.getName()).collect(Collectors.toList());
		return toMap(t, fields);
	}

	public static <T> Map<String, Object> toMap(final T t, String... fields) {
		return toMap(t, Arrays.asList(fields));
	}

	public static <T> Map<String, Object> toMap(final T t, List<String> fields) {
		Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
			// Iterate over all the attributes
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

				// write메소드와 read메소드가 존재할때만.
				Method writeMethod = descriptor.getWriteMethod();
				Method readMethod = descriptor.getReadMethod();
				if (ValueUtil.isEmpty(writeMethod) || ValueUtil.isEmpty(readMethod)) {
					continue;
				}
				String methodName = ValueUtil.getSimpleMethodName(readMethod.getName());
				if (fields.contains(methodName)) {
					Object originalValue = readMethod.invoke(t);
					hashMap.put(methodName, originalValue);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return hashMap;
	}
	
	
	
	/**
	 * @최초생성일 2023. 4. 1.
	 */
	public static final Comparator<Path> PATH_NAME_COMPARE = new Comparator<Path>() {

		@Override
		public int compare(Path o1, Path o2) {

			String fname1 = o1.getFileName().toString();
			String fname2 = o2.getFileName().toString();
			if (Files.isDirectory(o1) == Files.isDirectory(o2)) {
				return fname1.compareTo(fname2);
			}

			if (Files.isDirectory(o1)) {
				return -1;
			}
			if (Files.isDirectory(o2)) {
				return 1;
			}
			
			return fname1.compareTo(fname2);
		}
	};
	
}
