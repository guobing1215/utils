package com.springboot.core.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: get和post请求工具类
 * 
 * @date Created on 2018年4月19日
 */
public class HttpClientUtil {

	/**
	 * @Description:get请求
	 * @param url 网络连接地址
	 * @return String
	 * @date 2018年8月23日 下午3:30:58
	 */
	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * @Description:get请求，带参数
	 * @author AnYanSen
	 * @param url 网络连接地址
	 * @param
	 * @return String
	 * @date 2018年8月23日 下午3:31:57
	 */
	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	/**
	 * @Description:post请求，无参数
	 * @author AnYanSen
	 * @param url 连接地址
	 * @return String
	 * @date 2018年8月23日 下午3:33:26
	 */
	public static String doPost(String url) {
		return doPost(url, null);
	}

	/**
	 * @Description:post请求，参数为json格式
	 * @author AnYanSen
	 * @param url  网络连接地址
	 * @param json
	 * @return String
	 * @date 2018年8月23日 下午3:34:14
	 */
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	/**
	 * @Description:post请求，参数map格式
	 * @author AnYanSen
	 * @param url   网络连接地址
	 * @param param
	 * @return String
	 * @date 2018年8月23日 下午3:35:21
	 */
	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultString;

	}

	/**
	 * @Description:
	 * @author AnYanSen
	 * @param  url 网络地址
	 * @param  param 参数
	 * @param  path 保存图片路径
	 * @param  imgName 图片名称
	 * @return 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @date 2018年9月25日 下午5:51:38 
	 */
	public static void doGetAndImg(String url, Map<String, String> param, String path, String imgName) throws IOException, URISyntaxException {
		URL url2 = null;
		int imageNumber = 0;
		byte[] context = null;
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			url2 = new URL(uri.toString());
			DataInputStream dataInputStream = new DataInputStream(url2.openStream());

			// 图片名称+路径
			String img = path + imgName;
			FileOutputStream fileOutputStream = new FileOutputStream(new File(img));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;
			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
	}
	/**
	 * @Param [mediaId, accesstoken]
	 * @Author bing
	 * @Description //TODO 获取图片下载
	 * @Date 10:54 2019/9/10
	 * @return java.io.InputStream
	 **/
	public static InputStream getInputStream(String mediaId,String accesstoken) throws IOException {
		// 根据AccessToken获取media
		InputStream is = null;

		String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + accesstoken + "&media_id=" + mediaId;

		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			is = http.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
}
