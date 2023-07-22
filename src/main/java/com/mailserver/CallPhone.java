package com.mailserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallPhone {

	

	public void httpURLGETCase( String phone ) {
		String methodUrl = "http://ml.mailserver.plus:88/SingleCallByVoiceX/SingleCallByVoice/src/Sample.php";
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		String line = null;
		try {
			URL url = new URL(methodUrl + "?mobile="+phone);
			System.out.println(methodUrl + "?mobile="+phone );
			connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
			connection.setRequestMethod("GET");// 默认GET请求
			connection.connect();// 建立TCP连接
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 发送http请求
				StringBuilder result = new StringBuilder();
				// 循环读取流
				while ((line = reader.readLine()) != null) {
					result.append(line).append(System.getProperty("line.separator"));// "\n"
				}
				System.out.println(result.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connection.disconnect();
		}
	}

	
	
}
