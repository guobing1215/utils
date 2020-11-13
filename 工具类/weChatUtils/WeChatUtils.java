package com.springboot.core.weChatUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.core.utils.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信接口工具类
 * @author AnYanSen
 * @date 2018年8月31日 下午3:56:46
 */
public class WeChatUtils {
    private static Logger logger = LoggerFactory.getLogger(WeChatUtils.class);
    /**
     * 获取接口凭证接口
     */
    private static final String WECHAT_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 拉取会员注册信息接口
     */
    private static final String WECHAT_MEMBERCARDINFO = "https://api.weixin.qq.com/card/membercard/userinfo/get?access_token=";
    /**
     * 查询卡券详情接口
     */
    private static final String WECHAT_CHATCARDINFO = "https://api.weixin.qq.com/card/get?access_token=";
    /**
     * 获取用户基本信息（包括UnionID机制）接口
     */
    private static final String WECHAT_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info";

    /**
     * 批量获取用户基本信息
     */
    private static final String BATCH_GET_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=";

    /**
     * 根据OpenID列表群发接口
     */
    private static final String WECHAT_MASSCARD = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";
    /**
     * 客服接口-发消息
     */
    private static final String WECHAT_SENDSUB = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
    /**
     * 获取用户已领取卡券接口
     */
    private static final String WECHAT_RECEIVEDCARD = "https://api.weixin.qq.com/card/user/getcardlist?access_token=";
    /**
     * 核销Code接口
     */
    private static final String WECHAT_DELETECODE = "https://api.weixin.qq.com/card/code/consume?access_token=";
    /**
     * 通过code换取网页授权access_token
     */
    private static final String WECHAT_WEBACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 获得jsapi_ticket或者api_ticket接口
     */
    private static final String WECHAT_JSAPITICKEY = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    /**
     * 使用授权码换取公众号或小程序的接口调用凭据和授权信息
     */
    private static final String WECHAT_AUTHORIZERACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth";

    /**
     * 获取第三方平台component_access_token
     */
    private static final String WECHAT_COMPONENTACCTESSTOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";

    /**
     * 根据activate_ticket获取到用户填写的信息(微信网页授权开发)
     */
    private static final String WECHAT_ACTIVATE_TICKET = "https://api.weixin.qq.com/card/membercard/activatetempinfo/get?access_token=";

    /**
     * Code解码接口
     */
    private static final String WECHAT_DECODE = "https://api.weixin.qq.com/card/code/decrypt?access_token=";

    /**
     * 公众号可以使用本接口获取临时素材（即下载临时的多媒体文件）
     */
    private static final String WECHAT_MATERIAL = "https://api.weixin.qq.com/cgi-bin/media/get";

    /**
     * 创建标签
     */
    private static final String WECHAT_ADDTAG = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=";

    /**
     * 修改标签
     */
    private static final String WECHAT_CHANGETAG = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=";

    /**
     * 删除标签
     */
    private static final String WECHAT_DELETETAG = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=";

    /**
     * 获取公众号已创建的标签
     */
    private static final String WECHAT_GETTAG = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=";

    /**
     * 批量为用户打标签
     */
    private static final String WECHAT_ADDUSERANDTAG = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=";

    /**
     * 批量为用户取消标签
     */
    private static final String WECHAT_DELETEUSERANDTAG = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=";

    /**
     * 获取用户身上的标签列表
     */
    private static final String WECHAT_GETTAGANDOPENID = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=";

    /**
     * 查询Code接口
     */
    private static final String WECHAT_CHECKCODE = "https://api.weixin.qq.com/card/code/get?access_token=";

    /**
     * 用户openId接口
     */
    private static final String WECHAT_OPENID = "https://api.weixin.qq.com/cgi-bin/user/get";
    /**
     * 创建卡券二维码接口
     */
    private static final String WECHAT_CREATEQRCODE = "https://api.weixin.qq.com/card/qrcode/create?access_token=";
    /**
     * 生成带参数的二维码(公众号)
     */
    private static final String WECHAT_CREATEQRCODEBYCGI = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    /**
     * 获取开卡组件链接接口
     */
    private static final String WECHAT_ACTIVATE_GETURL = "https://api.weixin.qq.com/card/membercard/activate/geturl?access_token=";
    /**
     * 发送模板消息的接口
     */
    private static final String WECHAT_TEMPLATE_SEND="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    /**
     * @param AppID     微信公众号appid
     * @param AppSecret 微信公众号秘钥
     * @return String
     * @Description:获取AccessToken
     * @author AnYanSen
     * @date 2018年9月3日 下午1:50:18
     */
    public static String getAccessToken(String AppID, String AppSecret) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", AppID);
        params.put("secret", AppSecret);
        String res = HttpClientUtil.doGet(WeChatUtils.WECHAT_ACCESSTOKEN, params);
        JSONObject json = JSONObject.parseObject(res);
        return json.getString("access_token");
    }

    /**
     * @param TagName     标签名称
     * @param AccessToken 微信接口凭证
     * @return JSONObject
     * @Description:创建标签
     * @author AnYanSen
     * @date 2018年10月12日 下午3:38:03
     */
    public static JSONObject addUserTag(String AccessToken, String TagName) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject jso2 = new JSONObject();
        jso2.put("name", TagName);
        json.put("tag", jso2);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_ADDTAG + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param TagID       微信标签的id值
     * @param AccessToken 微信接口凭证
     * @return JSONObject
     * @Description:编辑标签
     * @author AnYanSen
     * @date 2018年10月12日 下午3:38:03
     */
    public static JSONObject changeUserTag(String AccessToken, String TagID) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject jso2 = new JSONObject();
        jso2.put("name", TagID);
        json.put("tag", jso2);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_CHANGETAG + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;

    }

    /**
     * @param @param  AccessToken
     * @param @return 设定文件
     * @return JSONObject    返回类型
     * @throws
     * @Title: getWeiXinTag
     * @author wangqin
     * @Description: 获取公众号已创建的标签
     */
    public static JSONObject getWeiXinTag(String AccessToken) {
        String res = HttpClientUtil.doGet(WeChatUtils.WECHAT_GETTAG + AccessToken);
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param TagID       微信标签的id值
     * @param AccessToken 微信接口凭证
     * @return JSONObject
     * @Description:删除标签
     * @author AnYanSen
     * @date 2018年10月12日 下午3:38:03
     */
    public static JSONObject deleteUserTag(String AccessToken, String TagID) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject jso2 = new JSONObject();
        jso2.put("id", TagID);
        json.put("tag", jso2);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_DELETETAG + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;

    }

    /**
     * @param TagID       微信标签的id值
     * @param openidLists openid集合
     * @param AccessToken 微信接口凭证
     * @return JSONObject
     * @Description:批量为用户打标签
     * @author AnYanSen
     * @date 2018年10月12日 下午3:38:03
     */
    public static JSONObject fromTagAddUser(String AccessToken, String TagID, List<String> openidLists) throws Exception {
        JSONObject json = new JSONObject();
        json.put("tagid", TagID);
        json.put("openid_list", openidLists);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_ADDUSERANDTAG + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;

    }

    /**
     * @param TagID       微信标签的id值
     * @param openidLists openid集合
     * @param AccessToken 微信接口凭证
     * @return JSONObject
     * @Description:fromTagAddUser
     * @author AnYanSen
     * @date 2018年10月12日 下午3:38:03
     */
    public static JSONObject fromTagDeleteUser(String AccessToken, String TagID, List<String> openidLists) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject jso2 = new JSONObject();
        json.put("tagid", TagID);
        json.put("openid_list", openidLists);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_DELETEUSERANDTAG + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;

    }

    /**
     * @param AccessToken 微信接口凭证
     * @param mediaId     媒体文件ID
     * @param path        图片保存路径
     * @param imgName     图片名称
     * @throws URISyntaxException
     * @throws IOException
     * @Description:
     * @author AnYanSen
     * @date 2018年9月25日 下午5:31:45
     */
    public static void downloadAndImg(String AccessToken, String mediaId, String path, String imgName) throws IOException, URISyntaxException {
        Map<String, String> maps = new HashMap<>();
        maps.put("access_token", AccessToken);
        maps.put("media_id", mediaId);
        HttpClientUtil.doGetAndImg(WeChatUtils.WECHAT_MATERIAL, maps, path, imgName);
    }

    /**
     * @param AppID              第三方平台appid
     * @param authorization_code 授权code
     * @return JSONObject
     * @Description: 该API用于使用授权码换取授权公众号或小程序的授权信息，
     * 并换取authorizer_access_token和authorizer_refresh_token
     * @author AnYanSen
     * @date 2018年9月20日 上午11:14:23
     */
    public static JSONObject getAuthorizerAccessToken(String AppID, String authorization_code) {
        JSONObject json = new JSONObject();
        json.put("component_appid", AppID);
        json.put("authorization_code", authorization_code);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_AUTHORIZERACCESSTOKEN, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param EncryptCode 加密的卡券code
     * @return JSONObject
     * @Description: code解码接口支持两种场景:
     * 1.商家获取choos_card_info后，将card_id和encrypt_code字段通过解码接口，获取真实code。
     * 2.卡券内跳转外链的签名中会对code进行加密处理，通过调用解码接口获取真实code。
     * @author AnYanSen
     * @date 2018年9月21日 上午9:55:36
     */
    public static JSONObject WeChatDecode(String EncryptCode) {
        JSONObject json = new JSONObject();
        json.put("encrypt_code", EncryptCode);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_DECODE, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken  接口凭证
     * @param cardId       卡券种类id
     * @param code         卡券序列号
     * @param checkConsume 是否校验code核销状态，填入true和false时的code异常状态返回数据不同。
     * @return JSONObject
     * @Description:查询code接口可以查询当前code是否可以被核销并检查code状态
     * @author AnYanSen
     * @date 2018年10月24日 下午9:27:31
     */
    public static JSONObject WeChatcheckCode(String AccessToken, String cardId, String code, boolean checkConsume) {
        JSONObject json = new JSONObject();
        if (cardId != null) {
            json.put("card_id", cardId);
        }
        json.put("code", code);
        json.put("check_consume", checkConsume);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_CHECKCODE + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param ActivateTicket 获取微信用户填写注册信息凭证
     * @return JSONObject
     * @Description:根据activate_ticket获取到用户填写的信息。
     * @author AnYanSen
     * @date 2018年9月20日 下午6:07:54
     */
    public static JSONObject getRegisterInfo(String ActivateTicket, String AccessToken) {
        JSONObject json = new JSONObject();
        json.put("activate_ticket", ActivateTicket);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_ACTIVATE_TICKET + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param ExpireSeconds  指定二维码的有效时间，范围是60 ~ 1800秒。不填默认为365天有效
     * @param CardId         卡券ID。
     * @param code           卡券Code码,use_custom_code字段为true的卡券必须填写，非自定义code和导入code模式的卡券不必填写。
     * @param openid         指定领取者的openid，只有该用户能领取。bind_openid字段为true的卡券必须填写，非指定openid不必填写。
     * @param is_unique_code 指定下发二维码，生成的二维码随机分配一个code，领取后不可再次扫描。填写true或false。默认false，
     * @param outerStr       领取场景值，用于领取渠道的数据统计，
     * @return JSONObject
     * @Description:创建单张微信卡券二维码
     * @author AnYanSen
     * @date 2018年12月18日 下午3:59:23
     */
    public static JSONObject createQRCode(String AccessToken, Integer ExpireSeconds, String CardId, Integer code, String openid, Boolean is_unique_code, String outerStr) {
        JSONObject json = new JSONObject();
        JSONObject actionInfo = new JSONObject();
        JSONObject card = new JSONObject();
        JSONObject cardParam = new JSONObject();
        json.put("action_name", "QR_CARD");
        if (ExpireSeconds != null) {
            json.put("expire_seconds", ExpireSeconds);
        }
        if (code != null) {
            cardParam.put("code", code);
        }
        if (StringUtils.isNotBlank(openid)) {
            cardParam.put("openid", openid);
        }
        cardParam.put("card_id", CardId);
        cardParam.put("is_unique_code", is_unique_code);
        cardParam.put("outer_str", outerStr);
        actionInfo.put("card", cardParam);
        json.put("action_info", actionInfo);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_CREATEQRCODE + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * 生成关注公众号二维码(带参数)
     *
     * @param accessToken 微信接口凭证
     * @param sceneStr    场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return java.lang.String
     * @author anYanSen
     * @since 2019/5/20 17:32
     */
    public static String createQRCodeByCGI(String accessToken, String sceneStr) {
        JSONObject json = new JSONObject();
        JSONObject actionInfo = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_str", sceneStr);
        json.put("action_name", "QR_LIMIT_STR_SCENE");
        actionInfo.put("scene", scene);
        json.put("action_info", actionInfo);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_CREATEQRCODEBYCGI + accessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        // 二维码链接
        String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + js.getString("ticket");
        return url;
    }

    /**
     * @param componentAppid        第三方平台appid
     * @param componentAppsecret    第三方平台appsecret
     * @param componentVerifyTicket 微信后台推送的ticket，此ticket会定时推送
     * @return JSONObject
     * @Description:第三方平台component_access_token是第三方平台的下文中接口的调用凭据，也叫做令牌（component_access_token）
     * @author AnYanSen
     * @date 2018年9月20日 下午2:18:27
     */
    public static JSONObject getComponentAccessToken(String componentAppid, String componentAppsecret, String componentVerifyTicket) {
        JSONObject json = new JSONObject();
        json.put("component_appid", componentAppid);
        json.put("component_appsecret", componentAppsecret);
        json.put("component_verify_ticket", componentVerifyTicket);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_COMPONENTACCTESSTOKEN, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken 微信接口凭证
     * @return String
     * @Description:jsapi_ticket是公众号用于调用微信JS接口的临时票据
     * @author AnYanSen
     * @date 2018年9月19日 下午4:55:34
     */
    public static String getJsapiTicket(String AccessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", AccessToken);
        params.put("type", "jsapi");
        String res = HttpClientUtil.doGet(WeChatUtils.WECHAT_JSAPITICKEY, params);
        JSONObject json = JSONObject.parseObject(res);
        return json.getString("ticket");
    }

    /**
     * @param AccessToken 微信接口凭证
     * @return String
     * @Description:api_ticket 是用于调用微信卡券JS API的临时票据
     * @author AnYanSen
     * @date 2018年11月21日 下午2:26:45
     */
    public static String getApiTicket(String AccessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", AccessToken);
        params.put("type", "wx_card");
        String res = HttpClientUtil.doGet(WeChatUtils.WECHAT_JSAPITICKEY, params);
        JSONObject json = JSONObject.parseObject(res);
        return json.getString("ticket");
    }

    /**
     * @param AccessToken 接口凭证
     * @param cardId      卡券种类ID
     * @param Code        卡券序列号
     * @return String
     * @Description:获取注册微信电子会员卡的注册信息(一键开发类型)
     * @author AnYanSen
     * @date 2018年9月5日 下午3:37:53
     */
    public static JSONObject getWeChatMemberCardInfo(String AccessToken, String cardId, String Code) {
        JSONObject json = new JSONObject();
        json.put("card_id", cardId);
        json.put("code", Code);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_MEMBERCARDINFO + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken
     * @param cardId      卡券ID。创建卡券时use_custom_code填写true时必填。非自定义Code不必填写。
     * @param Code
     * @return JSONObject
     * @Description:核销卡券
     * @author AnYanSen
     * @date 2018年9月14日 下午4:39:48
     */
    public static JSONObject deleteCard(String AccessToken, String cardId, String Code) {
        JSONObject json = new JSONObject();
        if (cardId != null) {
            json.put("card_id", cardId);
        }
        json.put("code", Code);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_DELETECODE + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param appid  公众号的唯一标识
     * @param secret 公众号的appsecret
     * @param code   ode作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     * @return JSONObject
     * @Description:通过code换取网页授权access_token
     * @author AnYanSen
     * @date 2018年9月15日 下午3:45:39
     */
    public static JSONObject getWebAccessToken(String appid, String secret, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("secret", secret);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        String res = HttpClientUtil.doGet(WeChatUtils.WECHAT_WEBACCESSTOKEN, map);
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken 接口凭证
     * @param cardId      卡券种类ID
     * @return JSONObject
     * @Description:查看卡券详情
     * @author AnYanSen
     * @date 2018年9月5日 下午6:33:19
     */
    public static JSONObject getWeChatCardInfo(String AccessToken, String cardId) {
        JSONObject json = new JSONObject();
        json.put("card_id", cardId);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_CHATCARDINFO + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken 微信接口凭证
     * @param cardId      卡券种类id,不填写时默认查询当前appid下的卡券(填写了cardid可以查询到已经核销了的code)
     * @param openid      微信用户openid
     * @Description:用于获取用户卡包里的，属于该appid下所有可用卡券，包括正常状态和异常状态。
     * @author AnYanSen
     * @date 2018年9月14日 上午11:45:01
     */
    public static JSONObject getUserReceivedCard(String AccessToken, String cardId, String openid) {
        JSONObject json = new JSONObject();
        if (cardId != null) {
            json.put("card_id", cardId);
        }
        json.put("openid", openid);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_RECEIVEDCARD + AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken 微信接口凭证
     * @param touser      openid列表
     * @param cardId      卡券id
     * @return JSONObject
     * @Description:根据openid群发卡券
     * @author AnYanSen
     * @date 2018年9月8日 下午2:37:55
     */
    public static JSONObject MassCard(String AccessToken, List<Object> touser, String cardId) {
        JSONObject json1 = new JSONObject();
        JSONObject json2 = new JSONObject();
        json2.put("card_id", cardId);
        json1.put("touser", touser);
        json1.put("wxcard", json2);
        json1.put("msgtype", "wxcard");
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_CHATCARDINFO + AccessToken, json1.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken 微信接口凭证
     * @param openid      用户openid
     * @return JSONObject
     * @Description:获取用户基本信息（包括UnionID机制）
     * @author AnYanSen
     * @date 2018年9月8日 上午11:21:29
     */
    public static JSONObject getWeChatUserInfo(String AccessToken, String openid) {
        Map<String, String> maps = new HashMap<>();
        maps.put("access_token", AccessToken);
        maps.put("openid", openid);
        maps.put("lang", "zh_CN");
        String res = HttpClientUtil.doGet(WeChatUtils.WECHAT_USERINFO, maps);
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    public static JSONObject batchGetUserInfo(String AccessToken, List<Map<String, String>>list) {
        JSONObject json = new JSONObject();
        json.put("user_list", list);
        String res = HttpClientUtil.doPostJson(WeChatUtils.BATCH_GET_USERINFO+ AccessToken, json.toString());
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @return JSONObject
     * @Description: 获取用户的openId
     * @author wangqin
     * @date 2018年11月6日
     */
    public static JSONObject getwechatUserOpenId(String AccessToken, String next_openid) {
        Map<String, String> maps = new HashMap<>();
        maps.put("access_token", AccessToken);
        maps.put("next_openid", next_openid);
        String res = HttpClientUtil.doGet(WeChatUtils.WECHAT_OPENID, maps);
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken 接口凭证
     * @param openid      微信用户openid
     * @param msgtype     消息类型
     * @param paramJson   消息数据json串
     * @return JSONObject
     * @Description:客服消息,可在48小时内发送信息给微信用户
     * @author AnYanSen
     * @date 2018年9月13日 下午6:14:24
     */
    public static JSONObject sendInfoAndSubscribe(String AccessToken, String openid, String msgtype, JSONObject paramJson) {
        JSONObject json = new JSONObject();
        json.put("touser", openid);
        json.put("msgtype", msgtype);
        json.put(msgtype, paramJson);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_SENDSUB + AccessToken, json.toString());
        logger.info("方法sendInfoAndSubscribe() ,出参=" + res);
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }

    /**
     * @param AccessToken 微信接口凭证
     * @return JSONObject
     * @Description:开发者可以通过该接口获取开卡组件的链接，配置于公众号关注回复、自定义菜单等领卡入口内。
     * @author AnYanSen
     * @date 2019年4月15日 下午8:41:37
     */
    public static JSONObject getActivateGetUrl(String AccessToken, String cardId, String outerStr) {
        JSONObject json = new JSONObject();
        json.put("card_id", cardId);
        json.put("outer_str", outerStr);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_ACTIVATE_GETURL + AccessToken, json.toString());
        logger.info("方法getActivateGetUrl() ,出参=" + res);
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }
    /**
     * @param
     * @author bing
     * @Description // 发送模板消息
     * @date 11:08 2019/10/29
     * @return
     **/
    public static JSONObject sendTemplate(String AccessToken, String touser, String templateId, JSONObject jsonObject) {
        JSONObject json = new JSONObject();
        json.put("touser", touser);
        json.put("template_id", templateId);
        json.put("data",jsonObject);
        String res = HttpClientUtil.doPostJson(WeChatUtils.WECHAT_TEMPLATE_SEND + AccessToken, json.toString());
        logger.info("方法sendTemplate() ,出参=" + res);
        JSONObject js = JSONObject.parseObject(res);
        return js;
    }
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            logger.debug("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(),
                    strXML);
            throw ex;
        }

    }

    /**
     * String 转 org.dom4j.Document
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document strToDocument(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }

    /**
     * org.dom4j.Element 转  com.alibaba.fastjson.JSONObject
     *
     * @param node
     * @return
     */
    public static JSONObject elementToJSONObject(Element node) {
        JSONObject result = new JSONObject();
        // 当前节点的名称、文本内容和属性
        List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
        for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
            result.put(attr.getName(), attr.getValue());
        }
        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();// 所有一级子节点的list
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {// 遍历所有一级子节点
                if (e.attributes().isEmpty() && e.elements().isEmpty()) // 判断一级节点是否有属性和子节点
                    result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
                else {
                    if (!result.containsKey(e.getName())) // 判断父节点是否存在该一级节点名称的属性
                        result.put(e.getName(), new JSONArray());// 没有则创建
                    ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));// 将该一级节点放入该节点名称的属性对应的值中
                }
            }
        }
        return result;
    }
}
