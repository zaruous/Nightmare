/**
 * 
 */
package com.kyj.fx.nightmare.comm;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.kyj.fx.nightmare.ui.grid.TableName;



/**
 * 
 */
class AbstractDAOTest {

	@Test
	void updateBatchTest() throws Exception {
		var dao = new AbstractDAO();
		TestTest testTest1 = new TestTest(1,2);
		TestTest testTest2 = new TestTest(3,4);
		TestTest testTest3 = new TestTest(5,6);
		dao.updateBatch(Arrays.asList(testTest1, testTest2, testTest3));
	}
	
	@Test
	void insertBatchTest() throws Exception {
		var dao = new AbstractDAO();
		TestTest testTest1 = new TestTest(System.currentTimeMillis(),2);
		TestTest testTest2 = new TestTest(System.currentTimeMillis()+1,4);
		TestTest testTest3 = new TestTest(System.currentTimeMillis()+2,6);
		dao.insertBatch(Arrays.asList(testTest1, testTest2, testTest3));
	}
	@Test
	void deleteBatchTest() throws Exception {
		var dao = new AbstractDAO();
		
		TestTest testTest3 = new TestTest(System.currentTimeMillis(),6);
		dao.insertBatch(Arrays.asList(testTest3));
		int[] deleteBatch = dao.deleteBatch(Arrays.asList(testTest3));
		IntStream.of(deleteBatch).forEach(System.out::println);
		
		 dao.deleteBatch(Arrays.asList(testTest3));
	}
	
	@TableName("TEST_TEST")
	public static class TestTest extends AbstractDVO{
		long a;
		int b;
		
		public TestTest(long a, int b) {
			super();
			this.a = a;
			this.b = b;
		}
		
		public long getA() {
			return a;
		}

		public void setA(long a) {
			this.a = a;
		}

		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = b;
		}
		
	}

}
