package com.springboot.core.weChatUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


/**
 * @Title:获取配置文件
 * @Author:gaoshicheng
 * @Datetime:2016年11月16日上午10:19:25
 */
public class PropertiesConfigUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PropertiesConfigUtil.class);
	private static Properties properties = new Properties();
	    // 通过类装载器装载进来
	    static {
	        try {
	            // 从类路径下读取属性文件
	            properties.load(PropertiesConfigUtil.class.getClassLoader()
	                    .getResourceAsStream("wx_config.properties"));
	        } catch (IOException e) {
	        	logger.error(e.toString());
	        }
	    }
	    /**
	     * 函数功能说明 ：读取配置项 Administrator 2012-12-14 修改者名字 ： 修改日期 ： 修改内容 ：
	     *
	     * @参数：
	     * @return void
	     * @throws
	     */
	    public static String readConfig(String key) {
	        return (String) properties.get(key);
	    }
}
