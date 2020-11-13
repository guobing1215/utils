package com.springboot.core.weChatUtils;

import com.springboot.core.domain.DuplicateMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: DuplicateMessageKit
 * @Description:微信消息排重
 * @author AnYanSen
 * @date 2018年9月3日 下午7:31:04
 *
 */
@Configuration
@EnableScheduling
public class DuplicateMessageKit {
	private Logger logger = LoggerFactory.getLogger(DuplicateMessageKit.class);

	private static final List<DuplicateMessage> MSGG = new ArrayList<DuplicateMessage>();

	public static boolean checkWeChatMessage(Map<String, String> Maps) {
		DuplicateMessage DuplicateMessage = null;
		String MsgType = Maps.get("MsgType");
		if (StringUtils.isNotBlank(MsgType)) {
			if (MsgType.equals("event")) { // 推送事件
				String fromUserName = Maps.get("FromUserName");
				String createTime = Maps.get("CreateTime");
				String event = Maps.get("Event");
				if (StringUtils.isNotBlank(fromUserName) && StringUtils.isNotBlank(createTime)
						&& StringUtils.isNotBlank(event)) {
					DuplicateMessage = new DuplicateMessage(fromUserName, createTime, MsgType, null, event);
				}
			} else { // 普通消息
				String MsgId = Maps.get("MsgId");
				if (StringUtils.isNotBlank(MsgId)) {
					DuplicateMessage = new DuplicateMessage(null, null, MsgType, MsgId, null);
				}
			}
			if (DuplicateMessage != null) {
				// 判断是否有重复数据
				if (MSGG.contains(DuplicateMessage)) { // 有重复数据
					return true;
				} else { // 新增数据不重复
					MSGG.add(DuplicateMessage);
					return false;
				}
			}
		}
		return false;
	}

	@Scheduled(cron = "*/60 * * * * ?")
	public void cleaWeChatMeassage() {
		DuplicateMessage DuplicateMessage = null;
		// 系统毫秒值
		Long SYSTime = System.currentTimeMillis();
		// 微信返回时间
		Long CreateTime = null;
		if (MSGG.size() > 0) {
			for (int i = 0; i < MSGG.size(); i++) {
				DuplicateMessage = MSGG.get(i);
				if (DuplicateMessage.getCreateTime() != null) {
					logger.info("清除-" + i + "-" + DuplicateMessage.toString());
					CreateTime = Long.valueOf(DuplicateMessage.getCreateTime());
					// 微信会重复发送3次，每次5秒，这边表示,该数据已经过了冲发时间可以清除了
					if (((SYSTime - CreateTime) / 1000) > 30) {
						MSGG.remove(i);
					}
				}
			}
		}
	}
}
