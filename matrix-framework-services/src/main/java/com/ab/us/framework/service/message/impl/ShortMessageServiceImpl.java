package com.ab.us.framework.service.message.impl;

import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.service.BaseService;
import com.ab.us.framework.service.push.constant.ShortMessageCategory;
import com.ab.us.framework.service.dictionray.SystemDictionaryCacheService;
import com.ab.us.framework.service.message.MessageProperties;
import com.ab.us.framework.service.message.ShortMessageService;
import com.ab.us.framework.service.message.SmsValidateCodeProvider;
import com.ab.us.framework.service.message.ValidateSmsInfo;
import com.ab.us.framework.service.push.PushService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhongchongtao
 */
@Service
@EnableConfigurationProperties(MessageProperties.class)
public class ShortMessageServiceImpl extends BaseService implements ShortMessageService {


    private static final String SHORT_MESSAGE_VALIDATE_CODE_CACHE_LOCATION = "short-message:validate:{category}:{did}:{mobile}";
    private final MessageProperties messageProperties;
    private final PushService pushService;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public ShortMessageServiceImpl(MessageProperties messageProperties, SystemDictionaryCacheService systemDictionaryCacheService, PushService pushService, StringRedisTemplate redisTemplate) {
        this.messageProperties = messageProperties;
        this.pushService = pushService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean sendValidateShortMessage(SmsValidateCodeProvider provider, Long did, String mobile, Map<String, String> params) {

        Map<String, ValidateSmsInfo> validateInfoMap = messageProperties.getValidateInfoMap();
        if (!validateInfoMap.containsKey(provider.getConfigName())) {
            return false;
        }
        ValidateSmsInfo validateSmsInfo = validateInfoMap.get(provider.getConfigName());

        if (provider.isAllowSendValidateCode(did, mobile)) {
            if (TypeChecker.isNull(params)) {
                params = new HashMap<>(1);
            }
            String validateCode = provider.getValidateCode();
            params.put("validateCode", validateCode);
            if (!params.containsKey("validateCode")) {
                params.put("validateCode", validateCode);
            }
            ExpressionParser parser = new SpelExpressionParser();
            EvaluationContext context = new StandardEvaluationContext();
            context.setVariable("params", params);
            Expression expression = parser.parseExpression(validateSmsInfo.getMessageTemplate(), new TemplateParserContext());
            String message = expression.getValue(context, String.class);

            boolean b = pushService.sendShortMessageByMobileNumber(mobile, message, 0, 0, ShortMessageCategory.NOTICE_MESSAGE, validateSmsInfo.getBusiness());
            if (b) {
                BoundValueOperations<String, String> stringStringBoundValueOperations = redisTemplate.boundValueOps(SHORT_MESSAGE_VALIDATE_CODE_CACHE_LOCATION.replace("{category}", provider.getConfigName()).replace("{mobile}", mobile).replace("{did}", did.toString()));
                stringStringBoundValueOperations.set(validateCode);
                stringStringBoundValueOperations.expire(validateSmsInfo.getExpireTime(), TimeUnit.MILLISECONDS);
                return true;
            } else {
                logger.warn("验证码短消息发送失败！");
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean validateShortMessageCheckCode(SmsValidateCodeProvider provider, String phoneNumber, Long did, String validateCode) {
        String s = redisTemplate.boundValueOps(SHORT_MESSAGE_VALIDATE_CODE_CACHE_LOCATION
                .replace("{category}", provider.getConfigName())//
                .replace("{mobile}", phoneNumber)//
                .replace("{did}", did.toString()))//
                .get();
        if (StringUtils.isEmpty(s)) {
            logger.warn("验证码已经失效！");
            return false;
        }
        if (!StringUtils.equals(validateCode, s)) {
            logger.warn("短信校验码与发送的短信校验码不符！");
            return false;
        }
        //清理此缓存
        return true;
    }


    @Override
    public void Init() {
        logger = LoggerFactory.getLogger(getClass());
    }
}
