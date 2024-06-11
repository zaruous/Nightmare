/**
 * 
 */
package chat.rest.api.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import chat.rest.api.service.core.AbstractPromptService;
import chat.rest.api.service.core.ChatBotConfig;
import chat.rest.api.service.core.ResponseHandler;
import chat.rest.api.service.core.VirtualPool;
import chat.rest.api.service.impl.Ollama3Service.Ollama3ResponseDVO;

/**
 * 
 */
public class RekaService extends AbstractPromptService {

	public RekaService() throws Exception {
		super();
	}

	@Override
	public ChatBotConfig createConfig() throws IOException {
		ChatBotConfig chatBotConfig = new ChatBotConfig();

		Properties properties = new Properties();
		try (InputStream in = new FileInputStream(new File("reka.properties"))) {
			properties.load(in);
		}

		chatBotConfig.setConfig(properties);
		return chatBotConfig;
	}

	@Override
	public void send(String message, ResponseHandler handler) throws Exception {
		StringEntity entity = new StringEntity(message);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "Bearer " + getConfig().getConfig().getProperty("api.key"));
		httpPost.setEntity(entity);
		VirtualPool.newInstance().execute(runAsync(handler, httpPost));
	}

	public String send(String message) throws Exception {
		// API 요청 생성
		Gson gson = new Gson();

		HttpGet get = new HttpGet(getConfig().getConfig().getProperty("accessTokenUrl"));
		get.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
		get.setHeader("Cookie",
				"appSession=eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwidWF0IjoxNzE1NDA5Mjc0LCJpYXQiOjE3MTUzOTgzMDIsImV4cCI6MTcxNTY2ODQ3NH0..ODFbKHs9rN6lSkCT.Q6dQl7tfiQrTZ4idnygHkYcIZ0VRoawLxAm4IFcf8yO3rtjKbX1nfoNtQ1uFsQtNAV7RJn6o2f5mkWlqfgDVz274GWql_NPWZIymapis2O91uo-mGTseoLKJOOUsvJBQ0HGmogIiMr-WiCUSCoNbqQv0LzwfZ4WpPxZzA8DFBnnSWHVWoXEr9m0_lNV7nOUF8e3VzooxlsqwtQQ68-J1ZNGkdwUSiyJBB4egse2amLMJOuB4n7PB9gT-SUvC0molfdIpP5YUqlJ911Jpm27OD14Z4kyAX4R3Oj1wa1EhvZFP4oYsH01ovKCAaosXa7a4wSFqtJdbZyUPVKx3zvZxG0df-rvtrf-3MRlqk9dQRyIt41d0xDMWVhan7ScKAFtwV9rqyvENAbjeNQ1ZyiwSQKhfMRmQttfQlE_5GBiCLFY8P-mek-WNyp33Q6DYpvv6TeDlOxTsbigOmRPVxYAzAPjjBpDMPtUf75NGHkDtbO5btnbjpCsB0NA2juuEebAUH4d0j7-My0kh2VfodpAt7cIooD3mGpJ43htsyeaOdumsKtZUu8rMDjzb5_I6F1KtU35ofQ_CVdM16z46c5KEXbcYyt9ssvbBMQT0IYYGaABMSK9E2Z2ropY-N5f0XaceHSKYvy025PYXRBBZFND0IyIjUhc65mcVxBIRFIIohu3kkS-N_O6k4Ard1dxScO9ISEoKqGOoj68dUPMTKSKMvjFykERy_Ft5m7WC2ajT1Vgmfq1E7cVYTd2z8oLR5-5IUoH71PAHKvVjZPHzshYjzwJMKXdIN9Coj2p0pPmyHJX9U-EGBK1RA5xVvxBCyF8jnx1tA7oTHB9lNJd7aH8cmj6-BNwr4Mv98T1yyPaAlptix8dT8rqF3Lh9BLAAtZgshjmKbUIJ3aa1rhA4p3Bzh4bAVkbGzOH1MOYNJYonzbSi7iXvvfgOYkcbNUYea_emfMxIJXmGtBVy_3EaOVMWhnYPT2GYPSs03XnwlimT7zVTJg_jj46gj7aSKNJTna4hf40ERlzQcbQu6i1TZH0kGqVFI5-4RkmqpKDRDERB184qocIQeF0dlMSg1Y5ZE1LekWtPec8EGNR2E6c_bdP_50EBN_6_0ztiO2Zxmu6RHpC042CESmsS0DHTYDqyDSEuepXXipMXGuksLN5Qo2UFXrYVE3-YJtMo8NCqsd9QX5f8II_1YjEknPEOR8S7O1QU3LZkVceEpHdmOdk7Q62JF-hXAcYlVK5v8hRxem9D1tJ8Z3pmQYEwSynPae486x_TIwfn-GRJ66IMA1iwDZHtQRz0UxK0s5p_cxPC3Lhhe1MsqIsrNfWzWhYqZ8lHxmPyVuLzSSO50iaMbpqmeqyubVnAAUCtsjE5cJVYRtrevZlnZa4UMcxiqZWVodyYJVxnbiBf4hRBhD9hWuUV5kiT2E2uKX_GVD563eHxwISXaAUk2It6VLO0hc89erf6MVfd42CC7hM5jTtajTBL37PvSBFLf8FtN7g6vTVS31fK0oSS4p525GGI739R1lo_bDq6NvE1VlL6wPdrKHtEmHTcUHDt4pBFFrnmeYnjcZtqUghTbESlRjUl9a9Jo6i-EmE72SSqZ5M9Wj-bRDEorROp4KZs11mPSLRgFpX74F0k22kGKV3v7jjMBLYIha8Duel1j8FkgPtF-FXfH5FVAUk2iuNM-WIoj1eFsItOLfvoap3PVppPSrAgp5wj5sJwUfn-Nf2MmXEiJKCNTgn14TXLQYb7nvQkY-5_n6_0Tnzel8jMpSk9InwalOW08BhoMLaFxAQNj6djbJAJiCl3r8PsSy84rfGgGqgO8pBvqxwJGJmIgbd72No_FC-yZQvWvxu6CfvX8vjXZyvI2CceGeX7qUYqou8G1ZpsTuV2QHsGbg6EzmYO7Y5v2k872P-k32lvOp8Fu30iHPvzIHWwCQEZZBkQm1c1xqzqR6JM9ZEcwAN1Luqcd8RnVlMFU2-Fv42jJmktOUwoGKgLDReWTEPkHROVsyE0s0u9EfVpE8NEk__TeBv1S-ugj80QHK_0WEusI8KdhnD7Z8aa4cAcPQSoTtiYrQ0kJzUPCwe_yqltqs6ZXVPJ6HLcVsmCK1PBZo_jGZBrzuFmx6g5reX7U-RX6SNknkyDUpc0vH1lzpffCXCrvms_6bkZH_MB-n6Wlp4c9N5WJsHGJ3tHb7SC4Q_VTHeIuGpPoMz3aZ6hSdp9P-QF2RhqFE4hWxIFIW7W8ZCNcqjVK22a9ArmON3ADhiODrn-4UpdistzXbbeJz8xOksZavNkSgKQ1uMeaf6WgvwQfBO_4Oyxc37tTedgWAxo08Oc4a7PTM8PGNne8zzqX2_fcxOwyNyZ-C407W40xbGdv7wOBg0mEiB8b0LazI4xMiuBv3jn0HTq3sht3yuzHyn9oicUe3VCBXqEyfMGAK4fwZh8AgJxCl6EXf9cDFK5cLr2q-WS4-bbHFJBw9tuDnxAmBW4PMnVmZNXOXlx3h_8pRlbjst1cOKXGwn2Gea-WflVIFXW6zZrpgAgWeGHWosMi3_EeohFwr6bM8eHQy0Jb4Ufzi8KlNw5leKSTC02sd9H-B9KbMdAfNAyxXJNzZfz2zTQwMFQJa_x8MmtSCS40fj3GKvbNl9KLuGlVnPTBiZeTOuii1xaR0XgwRctmQJoz0qOW17IANHIu5mwMPt7nEgGX6XpYSTvAQ4Y_TAo2idsyyd9Ie_O9wT5Sz-Q1bL5VFujyLAf2IJkbqypHYtEGbWNCW6QrCo1SwkWsFjhhCugrU7Q6i2yaQHA1oUE9dz0WSDnhPij9SwEkyiOpSMXqN_TOowJjJpL6HAuBPOkNHSInYmgvMbXw53n0ar1w7oyt0e0ycqFEBnumDPfCZKxaiTIp-OaKwrYQvbgwcoCgiOIVTodHIGnIHfj7T5f5LSvIooTCKNP35Rs0oaMfTwdffptH7TGhCVitV2hYW50tQgZgMfXxgm9ABuSMBEtX-b6UAXtLF100wJ1baCaDLK4RlYxG9SBt0P1a4ntrKvQOSPixFP1noujE-hU3YGriZ1wUbXvRypkRHeDKpPpvmlD3s9YEMYAFZTxJ3AzZSSaZVrU6kHlrSiPIl2CDHmTVAA6mn7htJijOisroObO02sMHerhHbmIM6nA7BfxXiAG579ha3pVKl0R6tX1EPsQI1gYqXD6pZU-V3kwdpZwvLyQjVVE-0QDhed18AI51e_Zs8NuMzDjZ8srnLXWRkzjHFsej1ZHhkod1tkOs_qfASjIYvGh9zU73KFoIGkO9TZO2kuFZmajAuUDJP0KCFfzyELEVH4-h7Yvz_LYk0YshW5_WX3adXrPHtAcF1tB6rjusmyP8g73MEOO8jaycw.2ulQ7sNxONGhLUgw84p_UQ");

		String accessToekn = "";
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {

			String string = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			System.out.println(string);
			HashMap fromJson = gson.fromJson(string, HashMap.class);
			accessToekn = (String) fromJson.get("accessToken");
			if (accessToekn == null) {
				throw new RuntimeException("token is invalid");
			}
		}

		var param = new HashMap<>();
		param.put("model_name", getConfig().getModel());
		param.put("conversation_history", List.of(Map.of("text", message, "type", "human")));
		param.put("random_seed", System.currentTimeMillis());
		param.put("stream", "True");
		param.put("use_code_interpreter", "False");
		param.put("use_search_engine", "False");

		String requestJson = gson.toJson(param);
		StringEntity entity = new StringEntity(requestJson, StandardCharsets.UTF_8);
		HttpPost httpPost = new HttpPost(getConfig().getRootUrl());
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Cookie",
				"appSession=eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwidWF0IjoxNzE1NDA5Mjc0LCJpYXQiOjE3MTUzOTgzMDIsImV4cCI6MTcxNTY2ODQ3NH0..ODFbKHs9rN6lSkCT.Q6dQl7tfiQrTZ4idnygHkYcIZ0VRoawLxAm4IFcf8yO3rtjKbX1nfoNtQ1uFsQtNAV7RJn6o2f5mkWlqfgDVz274GWql_NPWZIymapis2O91uo-mGTseoLKJOOUsvJBQ0HGmogIiMr-WiCUSCoNbqQv0LzwfZ4WpPxZzA8DFBnnSWHVWoXEr9m0_lNV7nOUF8e3VzooxlsqwtQQ68-J1ZNGkdwUSiyJBB4egse2amLMJOuB4n7PB9gT-SUvC0molfdIpP5YUqlJ911Jpm27OD14Z4kyAX4R3Oj1wa1EhvZFP4oYsH01ovKCAaosXa7a4wSFqtJdbZyUPVKx3zvZxG0df-rvtrf-3MRlqk9dQRyIt41d0xDMWVhan7ScKAFtwV9rqyvENAbjeNQ1ZyiwSQKhfMRmQttfQlE_5GBiCLFY8P-mek-WNyp33Q6DYpvv6TeDlOxTsbigOmRPVxYAzAPjjBpDMPtUf75NGHkDtbO5btnbjpCsB0NA2juuEebAUH4d0j7-My0kh2VfodpAt7cIooD3mGpJ43htsyeaOdumsKtZUu8rMDjzb5_I6F1KtU35ofQ_CVdM16z46c5KEXbcYyt9ssvbBMQT0IYYGaABMSK9E2Z2ropY-N5f0XaceHSKYvy025PYXRBBZFND0IyIjUhc65mcVxBIRFIIohu3kkS-N_O6k4Ard1dxScO9ISEoKqGOoj68dUPMTKSKMvjFykERy_Ft5m7WC2ajT1Vgmfq1E7cVYTd2z8oLR5-5IUoH71PAHKvVjZPHzshYjzwJMKXdIN9Coj2p0pPmyHJX9U-EGBK1RA5xVvxBCyF8jnx1tA7oTHB9lNJd7aH8cmj6-BNwr4Mv98T1yyPaAlptix8dT8rqF3Lh9BLAAtZgshjmKbUIJ3aa1rhA4p3Bzh4bAVkbGzOH1MOYNJYonzbSi7iXvvfgOYkcbNUYea_emfMxIJXmGtBVy_3EaOVMWhnYPT2GYPSs03XnwlimT7zVTJg_jj46gj7aSKNJTna4hf40ERlzQcbQu6i1TZH0kGqVFI5-4RkmqpKDRDERB184qocIQeF0dlMSg1Y5ZE1LekWtPec8EGNR2E6c_bdP_50EBN_6_0ztiO2Zxmu6RHpC042CESmsS0DHTYDqyDSEuepXXipMXGuksLN5Qo2UFXrYVE3-YJtMo8NCqsd9QX5f8II_1YjEknPEOR8S7O1QU3LZkVceEpHdmOdk7Q62JF-hXAcYlVK5v8hRxem9D1tJ8Z3pmQYEwSynPae486x_TIwfn-GRJ66IMA1iwDZHtQRz0UxK0s5p_cxPC3Lhhe1MsqIsrNfWzWhYqZ8lHxmPyVuLzSSO50iaMbpqmeqyubVnAAUCtsjE5cJVYRtrevZlnZa4UMcxiqZWVodyYJVxnbiBf4hRBhD9hWuUV5kiT2E2uKX_GVD563eHxwISXaAUk2It6VLO0hc89erf6MVfd42CC7hM5jTtajTBL37PvSBFLf8FtN7g6vTVS31fK0oSS4p525GGI739R1lo_bDq6NvE1VlL6wPdrKHtEmHTcUHDt4pBFFrnmeYnjcZtqUghTbESlRjUl9a9Jo6i-EmE72SSqZ5M9Wj-bRDEorROp4KZs11mPSLRgFpX74F0k22kGKV3v7jjMBLYIha8Duel1j8FkgPtF-FXfH5FVAUk2iuNM-WIoj1eFsItOLfvoap3PVppPSrAgp5wj5sJwUfn-Nf2MmXEiJKCNTgn14TXLQYb7nvQkY-5_n6_0Tnzel8jMpSk9InwalOW08BhoMLaFxAQNj6djbJAJiCl3r8PsSy84rfGgGqgO8pBvqxwJGJmIgbd72No_FC-yZQvWvxu6CfvX8vjXZyvI2CceGeX7qUYqou8G1ZpsTuV2QHsGbg6EzmYO7Y5v2k872P-k32lvOp8Fu30iHPvzIHWwCQEZZBkQm1c1xqzqR6JM9ZEcwAN1Luqcd8RnVlMFU2-Fv42jJmktOUwoGKgLDReWTEPkHROVsyE0s0u9EfVpE8NEk__TeBv1S-ugj80QHK_0WEusI8KdhnD7Z8aa4cAcPQSoTtiYrQ0kJzUPCwe_yqltqs6ZXVPJ6HLcVsmCK1PBZo_jGZBrzuFmx6g5reX7U-RX6SNknkyDUpc0vH1lzpffCXCrvms_6bkZH_MB-n6Wlp4c9N5WJsHGJ3tHb7SC4Q_VTHeIuGpPoMz3aZ6hSdp9P-QF2RhqFE4hWxIFIW7W8ZCNcqjVK22a9ArmON3ADhiODrn-4UpdistzXbbeJz8xOksZavNkSgKQ1uMeaf6WgvwQfBO_4Oyxc37tTedgWAxo08Oc4a7PTM8PGNne8zzqX2_fcxOwyNyZ-C407W40xbGdv7wOBg0mEiB8b0LazI4xMiuBv3jn0HTq3sht3yuzHyn9oicUe3VCBXqEyfMGAK4fwZh8AgJxCl6EXf9cDFK5cLr2q-WS4-bbHFJBw9tuDnxAmBW4PMnVmZNXOXlx3h_8pRlbjst1cOKXGwn2Gea-WflVIFXW6zZrpgAgWeGHWosMi3_EeohFwr6bM8eHQy0Jb4Ufzi8KlNw5leKSTC02sd9H-B9KbMdAfNAyxXJNzZfz2zTQwMFQJa_x8MmtSCS40fj3GKvbNl9KLuGlVnPTBiZeTOuii1xaR0XgwRctmQJoz0qOW17IANHIu5mwMPt7nEgGX6XpYSTvAQ4Y_TAo2idsyyd9Ie_O9wT5Sz-Q1bL5VFujyLAf2IJkbqypHYtEGbWNCW6QrCo1SwkWsFjhhCugrU7Q6i2yaQHA1oUE9dz0WSDnhPij9SwEkyiOpSMXqN_TOowJjJpL6HAuBPOkNHSInYmgvMbXw53n0ar1w7oyt0e0ycqFEBnumDPfCZKxaiTIp-OaKwrYQvbgwcoCgiOIVTodHIGnIHfj7T5f5LSvIooTCKNP35Rs0oaMfTwdffptH7TGhCVitV2hYW50tQgZgMfXxgm9ABuSMBEtX-b6UAXtLF100wJ1baCaDLK4RlYxG9SBt0P1a4ntrKvQOSPixFP1noujE-hU3YGriZ1wUbXvRypkRHeDKpPpvmlD3s9YEMYAFZTxJ3AzZSSaZVrU6kHlrSiPIl2CDHmTVAA6mn7htJijOisroObO02sMHerhHbmIM6nA7BfxXiAG579ha3pVKl0R6tX1EPsQI1gYqXD6pZU-V3kwdpZwvLyQjVVE-0QDhed18AI51e_Zs8NuMzDjZ8srnLXWRkzjHFsej1ZHhkod1tkOs_qfASjIYvGh9zU73KFoIGkO9TZO2kuFZmajAuUDJP0KCFfzyELEVH4-h7Yvz_LYk0YshW5_WX3adXrPHtAcF1tB6rjusmyP8g73MEOO8jaycw.2ulQ7sNxONGhLUgw84p_UQ");
		httpPost.setHeader("Authorization", "Bearer " + accessToekn);
		httpPost.setHeader("Origin", "https://chat.reka.ai");

		httpPost.setEntity(entity);

		// HttpClient를 사용하여 API 호출
		HttpEntity responseEntity = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpPost)) {
			// API 응답 처리
//			System.out.println(response.getStatusLine().getStatusCode());
			Stream.of(response.getAllHeaders()).forEach(System.out::println);
			responseEntity = response.getEntity();

			String str;
			try (BufferedReader r = new BufferedReader(new InputStreamReader(responseEntity.getContent()))) {
				str = r.lines().filter(ret -> !ret.isEmpty() && !ret.isBlank())
						.filter(ret -> !"event: message".equals(ret))
						.map(ret -> ret.startsWith("data:") ? ret.substring("data:".length()) : ret).map(ret -> {
							try {
								return gson.fromJson(ret, HashMap.class);
							} catch (Exception ex) {
								ex.printStackTrace();
								return null;
							}
						})
						.filter(ret -> ret!=null)
						.filter(ret -> "stop".equals(ret.get("finish_reason"))).map(ret -> {
							return ret.toString();
						}).collect(Collectors.joining());
			}
			return str;
		}
	}

	private Runnable runAsync(ResponseHandler handler, HttpPost httpPost) {
		return new Runnable() {

			@Override
			public void run() {
				// HttpClient를 사용하여 API 호출
				try (CloseableHttpClient httpClient = HttpClients.createDefault();
						CloseableHttpResponse response = httpClient.execute(httpPost)) {

					System.out.println(response.getStatusLine().getStatusCode());
					if (200 == response.getStatusLine().getStatusCode()) {
						// API 응답 처리
						HttpEntity responseEntity = response.getEntity();
						if (responseEntity != null) {
							handler.onSuccess(responseEntity);
						}
					} else {
						// API 응답 처리
						HttpEntity responseEntity = response.getEntity();
						handler.onSuccess(responseEntity);
					}

				} catch (Exception e) {
					handler.onException(e);
				}

			}
		};
	}

}
