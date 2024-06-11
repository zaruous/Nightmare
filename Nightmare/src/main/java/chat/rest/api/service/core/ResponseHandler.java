/**
 * 
 */
package chat.rest.api.service.core;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

/**
 * 
 */
public interface ResponseHandler {

	/**
	 * http status 200
	 * 
	 * @param responseEntity
	 * @throws IOException
	 * @throws ParseException
	 */
	default void onSuccess(HttpEntity responseEntity) throws Exception {
		String responseBody = toString(responseEntity);
		System.out.println(responseBody);
	}

	/**
	 * http status is not 200
	 * 
	 * @param responseEntity
	 * @throws IOException
	 * @throws ParseException
	 */
	default void onFail(HttpEntity responseEntity) throws Exception {
		String responseBody = toString(responseEntity);
		System.out.println(responseBody);
	}

	/**
	 * @param e
	 */
	default void onException(Exception e) {
		e.printStackTrace();
	}

	/**
	 * @param responseEntity
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	default String toString(HttpEntity responseEntity) throws ParseException, IOException {
		return EntityUtils.toString(responseEntity);
	}

}
