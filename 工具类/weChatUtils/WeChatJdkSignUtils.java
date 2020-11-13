package com.springboot.core.weChatUtils;

import com.springboot.core.utils.ConmonUtis;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: bing
 * @Date: 2019/9/16 11:16
 * @Description: 微信的JS-SDK使用权限签名算法
 */
public class WeChatJdkSignUtils {
    /**
     * @Description: 微信的JS-SDK使用权限签名算法
     * @param jsapiTicket  sapi_ticket是公众号用于调用微信JS接口的临时票据
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return Map<String,String>
     */
    public static Map<String, String> Jsapisign(String jsapiTicket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String signature = "";
        String nonce_str =ConmonUtis.getUUID();//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳

        // 注意这里参数名必须全部小写，且必须有序
        String  string1 = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8")); // 对string1 字符串进行SHA-1加密处理
            signature = byteToHex(crypt.digest()); // 对加密后字符串转成16进制
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;

    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
