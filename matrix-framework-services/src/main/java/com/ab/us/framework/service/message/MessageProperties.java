package com.ab.us.framework.service.message;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "shortMessage.validate")
public class MessageProperties {

    private Map<String, ValidateSmsInfo> validateInfoMap;

    public Map<String, ValidateSmsInfo> getValidateInfoMap() {
        return validateInfoMap;
    }

    public void setValidateInfoMap(Map<String, ValidateSmsInfo> validateInfoMap) {
        this.validateInfoMap = validateInfoMap;
    }
}
