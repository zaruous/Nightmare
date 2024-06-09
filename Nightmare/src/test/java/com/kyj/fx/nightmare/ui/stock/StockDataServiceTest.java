/**
 * 
 */
package com.kyj.fx.nightmare.ui.stock;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class StockDataServiceTest {

	/**
	 * Test method for {@link com.kyj.fx.nightmare.ui.stock.StockDataService#doService(java.io.File)}.
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws MalformedURLException 
	 */
	@Test
	void testDoService() throws MalformedURLException, UnsupportedEncodingException, IOException {
		
		StockDataMasterDVO doService = StockDataService.doService(new File("stock.json"));
//		System.out.println(doService);
		System.out.println(doService.getCurrentDatetime());
		int size = doService.getOutBlock1().size();
		System.out.println(size);
		Assert.assertNotEquals(0, size);
		System.out.println("증권정보가 존재.");
	}

}
