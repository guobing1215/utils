package com.springboot.core.weChatUtils;

import com.alibaba.fastjson.JSONObject;
import com.springboot.core.utils.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: TencentMapUtils
 * @Description: 腾讯地图相关工具类
 * @author AnYanSen
 * @date 2018年9月26日 上午11:36:34
 *
 */
public class TencentMapUtils {
	// 本接口提供由地址描述到所述位置坐标的转换
	public static final String TENMAP_TOCOORDINATE = "https://apis.map.qq.com/ws/geocoder/v1/";
	// 本接口提供由地址描述到所述位置坐标的转换
	public static final String TENMAP_TOADDRESS = "https://apis.map.qq.com/ws/geocoder/v1/";
	/**
	 * @Description:调用腾讯地图将地址转为坐标
	 * @author AnYanSen
	 * @param  address 门店地址
	 * @param  key 开发密钥（Key）
	 * @return JSONObject
	 * @date 2018年9月26日 上午11:47:49 
	 */
	public static JSONObject addressToCoordinate(String address, String key) {
		Map<String,String> maps = new HashMap<>();
		maps.put("address", address);
		maps.put("key", key);
		String doGet = HttpClientUtil.doGet(TencentMapUtils.TENMAP_TOCOORDINATE, maps);
		return JSONObject.parseObject(doGet);
	}
	
	/**
	 * @Description:调用腾讯地图将经纬度转地址
	 * @author AnYanSen
	 * @param  location  位置坐标，格式：location=lat<纬度>,lng<经度>
	 * 					 例如location= 39.984154,116.307490
	 * @param  key开发密钥（Key）
	 * @return JSONObject
	 * @date 2018年9月26日 下午1:07:00 
	 */
	public static JSONObject CoordinateToAddress(String location, String key) {
		Map<String,String> maps = new HashMap<>();
		maps.put("location", location);
		maps.put("key", key);
		String doGet = HttpClientUtil.doGet(TencentMapUtils.TENMAP_TOADDRESS, maps);
		return JSONObject.parseObject(doGet);
	}


}
