/**
 * 
 */
package chat.rest.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import chat.rest.api.ChatBot;
import chat.rest.api.ChatBot.API;
import chat.rest.api.service.core.AbstractPromptService;
import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.ChatBotService;
import chat.rest.api.service.core.ResponseHandler;
import chat.rest.api.service.core.VirtualPool;

/**
 * 
 * 여러 GPT 모델들을 호출하고 결과 확인
 */
public class ArenaService extends AbstractPromptService {

	public ArenaService() throws Exception {
		super();
	}

	public interface ArenaHandler {
		public void onFinish(API api, String result, long costTimeMills);
	}

	class Response {
		API api;
		String retMessage;
		long costTimeMills;
	}

	/**
	 * @param message
	 * @param handler
	 * @throws Exception
	 */
	public void send(String message, ArenaHandler handler) throws Exception {
		API[] values = ChatBot.API.values();
		VirtualPool newInstance = VirtualPool.newInstance();

		ArrayList<Callable<Response>> arrayList = new ArrayList<>();
		for (API api : values) {
			Callable<Response> c = new Callable<>() {
				@Override
				public Response call() throws Exception {
					long start = System.currentTimeMillis();
					System.out.println(api.name() + " start time : " + start);
					ChatBotService newBotService = ChatBot.newBotService(api);
					String send = newBotService.send(message);
					long end = System.currentTimeMillis();
					System.out.println(api.name() + " cost time : " + (end - start) + " mills");

					Response response = new Response();
					response.api = api;
					response.costTimeMills = (end - start);
					response.retMessage = send;
					return response;
				}
			};
			arrayList.add(c);
		}

		List<Future<Response>> invokeAll = newInstance.invokeAll(arrayList);
		invokeAll.forEach(r -> {
			Response response;
			try {
				response = r.get(15, TimeUnit.SECONDS);
				handler.onFinish(response.api, response.retMessage, response.costTimeMills);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		newInstance.shutdown();
	}

	@Deprecated
	@Override
	public String send(String message) throws Exception {
		throw new RuntimeException("not support.");
	}

	@Deprecated
	@Override
	public ChatBotConfig createConfig() throws Exception {
		return new ChatBotConfig();
	}

	@Deprecated
	@Override
	public void send(String message, ResponseHandler handler) throws Exception {
		throw new RuntimeException("not support.");
	}

}
