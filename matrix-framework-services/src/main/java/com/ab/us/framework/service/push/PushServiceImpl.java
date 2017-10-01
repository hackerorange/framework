package com.ab.us.framework.service.push;

import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.service.BaseService;
import com.ab.us.framework.service.configuration.properties.UsPushProperties;
import com.ab.us.framework.service.push.constant.*;
import com.ab.us.framework.service.push.domain.PushTarget;
import com.ab.us.framework.service.push.domain.ShortMessageTarget;
import com.ab.us.framework.service.push.request.PushMessageRequest;
import com.ab.us.framework.service.push.request.ShortMessageRequest;
import com.ab.us.framework.service.push.response.MessageResponse;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by ZhongChongtao on 2017/5/8.
 */
public class PushServiceImpl extends BaseService implements PushService {

    private final UsPushProperties pushProperties;

    public PushServiceImpl(UsPushProperties pushProperties) {
        this.pushProperties = pushProperties;
    }

    @Override
    public boolean pushByTokenId(String deviceTokenId, DevicePlatform devicePlatform, String title, String description, Object customContent, MessageType messageType, PushCategory pushCategory, String biz, int timeout, int level) {
        PushTarget pushUserInfo = PushTarget.getDevoceTarget(devicePlatform, deviceTokenId);
        PushMessageRequest pushRequestParam = new PushMessageRequest();
        //初始化请求参数信息
        pushRequestParam.setCustom_content(customContent)
                .setDescription(description)
                .setTitle(title)
                .setType(messageType.getCode())
                .setTimeout(timeout)
                .setBiz(biz)
                .setCategory(pushCategory.getCode())
                .setLevel(level)
                .setIus(new ArrayList<>(1))
                .getIus().add(pushUserInfo);
        MessageResponse messageResponse = postPush(pushRequestParam, "push");
        return TypeChecker.notNull(messageResponse) && messageResponse.getCode() == 200;
    }


    @Override
    public boolean pushByAccountId(String accountId, String title, String description, Object customContent, MessageType messageType, PushCategory pushCategory, String biz, int timeout, int level) {
        PushTarget pushUserInfo = PushTarget.getAccountTarget(accountId);
        PushMessageRequest pushRequestParam = new PushMessageRequest();
        //初始化请求参数信息
        pushRequestParam.setCustom_content(customContent)
                .setDescription(description)
                .setTitle(title)
                .setType(messageType.getCode())
                .setTimeout(timeout)
                .setBiz(biz)
                .setCategory(pushCategory.getCode())
                .setLevel(level)
                .setIus(new ArrayList<>(1))
                .getIus().add(pushUserInfo);
        MessageResponse messageResponse = postPush(pushRequestParam, "push");
        return TypeChecker.notNull(messageResponse) && messageResponse.getCode() == 200;
    }

    @Override
    public boolean pushByUserInfo(String name, String mobile, CertType certType, String certCode, String title, String description, Object customContent, MessageType messageType, PushCategory pushCategory, String biz, int timeout, int level) {
        PushTarget pushUserInfo = PushTarget.getUserTarget(name, mobile, certType, certCode);
        PushMessageRequest pushRequestParam = new PushMessageRequest();
        //初始化请求参数信息
        pushRequestParam.setCustom_content(customContent)
                .setDescription(description)
                .setTitle(title)
                .setType(messageType.getCode())
                .setTimeout(timeout)
                .setBiz(biz)
                .setCategory(pushCategory.getCode())
                .setLevel(level)
                .setIus(new ArrayList<>(1))
                .getIus().add(pushUserInfo);
        MessageResponse messageResponse = postPush(pushRequestParam, "push");
        return TypeChecker.notNull(messageResponse) && messageResponse.getCode() == 200;
    }


    @Override
    public boolean sendShortMessageByMobileNumber(String mobileNumber, String message, int timeout, int level, ShortMessageCategory messageCategory, String biz) {

        //创建新的请求参数对象
        ShortMessageTarget shortMessageTarget = new ShortMessageTarget();
        shortMessageTarget.setMobile(mobileNumber);
        ShortMessageRequest shortMessageRequest = new ShortMessageRequest();
        //设置各个参数
        shortMessageRequest
                .setContent(message)
                .setBiz(biz)
                .setCategory(messageCategory.getCode())
                .setLevel(level)
                .setTimeout(timeout)
                .setIus(new ArrayList<>())
                .getIus().add(shortMessageTarget);
        MessageResponse messageResponse = postPush(shortMessageRequest, "short-message");
        return TypeChecker.notNull(messageResponse) && messageResponse.getCode() == 200;
    }

    /**
     * 发送Post请求
     *
     * @param requestBody 请求体对象
     * @param method      请求的方法
     * @return 请求的结果
     */
    private MessageResponse postPush(Object requestBody, String method) {
        String requestEntityString = JSONObject.toJSONString(requestBody);
        String url = pushProperties.getUrl().get(method);
        if (StringUtils.isEmpty(url)) {
            logger.error("配置文件中没有找到[{}]的URL",method);
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<String> formEntity = new HttpEntity<>(requestEntityString, headers);
        try {
            MessageResponse result = restTemplate.postForObject(url, formEntity, MessageResponse.class);
            String resultJson = JSONObject.toJSONString(result);
            logger.info("\n\n == 向[消息中心]发起请求:\n |- 请求的URL:{}\n |- 请求的实体:{}\n |- 响应的结果:{}\n", url, requestEntityString, resultJson);
            return result;
        } catch (NestedRuntimeException e) {
            logger.error("\n\n == 向[消息中心]发送请求时网络超时");

            return null;
        }
    }

    @Override
    public void Init() {
        logger = LoggerFactory.getLogger(getClass());
    }


}
