/**
 * 
 */
package com.kyj.fx.websocket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 *  @Deprecated test@Deprecated
 */
@Deprecated
public class WsStarter implements Listener {

	@Override
	public void onOpen(WebSocket webSocket) {
		System.out.println("Connected to server");
		webSocket.sendText("{\"command\": \"/getHtml\"}", true);
		Listener.super.onOpen(webSocket);
	}

	@Override
	public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
		System.out.println("Received message: " + data);
		return Listener.super.onText(webSocket, data, last);
	}

	@Override
	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		System.out.println("Connection closed with exit code " + statusCode + " additional info: " + reason);
		return Listener.super.onClose(webSocket, statusCode, reason);
	}

	@Override
	public void onError(WebSocket webSocket, Throwable error) {
		System.err.println("An error occurred: " + error.getMessage());
		Listener.super.onError(webSocket, error);
	}

	@Deprecated
	public static void main(String[] args) {
		HttpClient client = HttpClient.newHttpClient();
		WebSocket.Builder builder = client.newWebSocketBuilder();
		CompletableFuture<WebSocket> webSocketFuture = builder.buildAsync(URI.create("ws://localhost:7070"),
				new WsStarter());

		try {
			WebSocket webSocket = webSocketFuture.get();
			webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Goodbye!")
					.thenRun(() -> System.out.println("WebSocket closed"));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
