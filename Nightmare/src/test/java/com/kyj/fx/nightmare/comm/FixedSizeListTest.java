/**
 * 
 */
package com.kyj.fx.nightmare.comm;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class FixedSizeListTest {

	@Test
	void test() {
		int testSize = 3;
		var fixedSizeList = new FixedSizeList<String>(testSize);
		fixedSizeList.add("tes1");
		fixedSizeList.add("test2");
		fixedSizeList.add("test3");
		fixedSizeList.add("test4");
		fixedSizeList.add("test5");
		Assert.assertEquals(testSize,fixedSizeList.size());
		System.out.println(fixedSizeList.size());
		
		Assert.assertEquals("test3",fixedSizeList.get(0));
		Assert.assertEquals("test4",fixedSizeList.get(1));
		Assert.assertEquals("test5",fixedSizeList.get(2));
		
		fixedSizeList.addAll(Arrays.asList("test1", "test2", "test3", "test4"));
		
		Assert.assertEquals(testSize,fixedSizeList.size());
		fixedSizeList.forEach(a -> System.out.println(a));
		
	}

}
