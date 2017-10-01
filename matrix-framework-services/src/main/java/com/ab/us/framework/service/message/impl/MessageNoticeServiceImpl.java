package com.ab.us.framework.service.message.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ab.us.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.core.utils.UUIDUtils;
import com.ab.us.framework.service.dao.MessageNoticeDao;
import com.ab.us.framework.service.entity.MessageAccount;
import com.ab.us.framework.service.entity.MessageNotice;
import com.ab.us.framework.service.entity.MessageNoticeTemplate;
import com.ab.us.framework.service.message.MessageNoticeService;

/**
 * Created by ZhongChongtao on 2017/5/4.<br/>
 * 消息提醒相关接口
 * 如果使用，需要添加注解 {@code @EnableMessageNoticeService}
 *
 * @since 2.11.0
 */
public class MessageNoticeServiceImpl implements MessageNoticeService {
    private final MessageNoticeDao messageNoticeDao;
    private final RedisTemplate<String, String> businessRedisTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MessageNoticeServiceImpl(MessageNoticeDao messageNoticeDao, @Qualifier("businessRedisTemplate") RedisTemplate<String, String> businessRedisTemplate) {
        this.messageNoticeDao = messageNoticeDao;
        this.businessRedisTemplate = businessRedisTemplate;
    }


    @Override
    public boolean createNewMessageNoticeByTemplate(String templateCategory, String templateSubCategory, String accountId, Map<String, String> extraParam) {

        //如果参数中，有一个为NULL，返回false
        if (!StringUtil.isAllNotEmpty(templateCategory, templateCategory, accountId)) {
            return false;
        }
        MessageNoticeTemplate messageNoticeTemplate = new MessageNoticeTemplate();
        //设置查询 Category
        messageNoticeTemplate.setCategory(templateCategory);
        //设置查询 SubCategory
        messageNoticeTemplate.setSubCategory(templateSubCategory);
        //查询对象
        messageNoticeDao.init();
        messageNoticeTemplate = (MessageNoticeTemplate) messageNoticeDao.queryEntity(messageNoticeTemplate);

        if (TypeChecker.isNull(messageNoticeTemplate)) {
            logger.info("没有找到指定的消息提醒模板");
            return false;
        }
        //将替换后的模板保存数据库中
        MessageNotice messageNotice = new MessageNotice(messageNoticeTemplate, extraParam);
        //消息ID
        messageNotice.setMessageId(UUIDUtils.generate());
        //创建时间
        messageNotice.setCreateDate(new Date());
        //插入messageNotice记录
        messageNoticeDao.init();
        messageNoticeDao.insert(messageNotice);
        //用户关联消息
        MessageAccount messageAccount = new MessageAccount();
        messageAccount.setMessageId(messageNotice.getMessageId());
        messageAccount.setAccountId(accountId);
        messageAccount.setCreateDate(new Date());
        messageNoticeDao.init();
        messageNoticeDao.insert(messageAccount);


        //============================================清理Account的消息提醒缓存===============================================

        //清除消息提醒分页缓存
        businessRedisTemplate.delete("BCH_MP_EXP_LifeAppMessageNoticePageout.,getLifeAppMessageNoticePageoutCache_Generic_" + accountId + "_ALL_Size_10_Page1");
        //清除新消息缓存
        businessRedisTemplate.boundHashOps("MapCache_NewMessageAccount").delete("Account_NewMessage_" + accountId);
        //清除小红点
        businessRedisTemplate.boundHashOps("MapCache_MessageAccountVisit").delete("Account_Visit_" + accountId);
        return true;

    }

    @Override
    public boolean createNewMessageNoticeByTemplate(String templateCategory, String templateSubCategory, String accountId) {
        Map<String, String> hashMap = new HashMap<>();
        return createNewMessageNoticeByTemplate(templateCategory, templateSubCategory, accountId, hashMap);
    }


}
