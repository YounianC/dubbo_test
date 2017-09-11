package net.younian.dubbo;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * HttpRequest
 * 
 * @author Orient
 *
 */
public abstract class HttpRequest {
	/**
	 * Log
	 */

	/**
	 * 向指定URL发送GET请求，可加自定义请求头
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param headers
	 *            自定义请求头
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param, String token) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("access_token", token);
			// 建立实际的连接
			connection.connect();
			// 遍历所有的响应头字段
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			result = sb.toString();
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
		return result;
	}
}