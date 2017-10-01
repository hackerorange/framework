package com.ab.us.framework.service.entity;

import java.util.Date;
import java.util.Map;

import com.ab.us.framework.db.annotation.FieldAnnotation;
import com.ab.us.framework.db.annotation.FieldType;
import com.ab.us.framework.db.annotation.TableAnnotation;
import com.ab.us.framework.db.entity.BaseEntity;
import com.ab.us.framework.db.entity.OrderBy;
import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;


/**
 * LFA_MESSAGE_NOTICE
 * MESSAGE_ID    VARCHAR2(64)
 * CATEGORY    VARCHAR2(64)
 * SUB_CATEGORY    VARCHAR2(64)
 * MESSAGE_TAG    CHAR(3)
 * IMAGE_ID    VARCHAR2(64)
 * SUBJECT    VARCHAR2(100)
 * CONTENT    VARCHAR2(300)
 * MESSAGE_URL    VARCHAR2(300)
 * SOURCE    VARCHAR2(50)
 * ACHIEVE    VARCHAR2(20)
 * AUTHORITY    VARCHAR2(2)
 * RANK_TOP    CHAR(1)
 * STATUS    CHAR(2)
 * RCDSTS    CHAR(1)
 * VERSION    VARCHAR2(15)
 * SEQNO    NUMBER(38)
 * MESSAGE_TYPE    CHAR(1)
 * TEMPLATE_ID    VARCHAR2(64)
 * CREATE_ACCOUNTID    VARCHAR2(64)
 * CREATE_DATE    DATE
 * UPDATE_ACCOUNTID    VARCHAR2(64)
 * UPDATE_DATE    DATE
 * PUBLISH_DATE    DATE
 */
@TableAnnotation(tableName = "LFA_MESSAGE_NOTICE")
public class MessageNotice extends OrderBy implements BaseEntity {
    @FieldAnnotation(fieldName = "MESSAGE_ID", fieldType = FieldType.STRING, pk = false)
    private String messageId;
    @FieldAnnotation(fieldName = "CATEGORY", fieldType = FieldType.STRING, pk = false)
    private String category;
    @FieldAnnotation(fieldName = "SUB_CATEGORY", fieldType = FieldType.STRING, pk = false)
    private String subCategory;
    @FieldAnnotation(fieldName = "MESSAGE_TAG", fieldType = FieldType.STRING, pk = false)
    private String messageTag;
    @FieldAnnotation(fieldName = "IMAGE_ID", fieldType = FieldType.STRING, pk = false)
    private String imageId;
    @FieldAnnotation(fieldName = "SUBJECT", fieldType = FieldType.STRING, pk = false)
    private String subject;
    @FieldAnnotation(fieldName = "CONTENT", fieldType = FieldType.STRING, pk = false)
    private String content;
    @FieldAnnotation(fieldName = "MESSAGE_URL", fieldType = FieldType.STRING, pk = false)
    private String messageUrl;
    @FieldAnnotation(fieldName = "SOURCE", fieldType = FieldType.STRING, pk = false)
    private String source;
    @FieldAnnotation(fieldName = "ACHIEVE", fieldType = FieldType.STRING, pk = false)
    private String achieve;
    @FieldAnnotation(fieldName = "AUTHORITY", fieldType = FieldType.STRING, pk = false)
    private String authority;
    @FieldAnnotation(fieldName = "RANK_TOP", fieldType = FieldType.STRING, pk = false)
    private String rankTop;
    @FieldAnnotation(fieldName = "STATUS", fieldType = FieldType.STRING, pk = false)
    private String status;
    @FieldAnnotation(fieldName = "RCDSTS", fieldType = FieldType.STRING, pk = false)
    private String rcdsts;
    @FieldAnnotation(fieldName = "VERSION", fieldType = FieldType.STRING, pk = false)
    private String version;
    @FieldAnnotation(fieldName = "SEQNO", fieldType = FieldType.NUMBER, pk = false)
    private Long seqno;
    @FieldAnnotation(fieldName = "MESSAGE_TYPE", fieldType = FieldType.STRING, pk = false)
    private String messageType;
    @FieldAnnotation(fieldName = "TEMPLATE_ID", fieldType = FieldType.STRING, pk = false)
    private String templateId;
    @FieldAnnotation(fieldName = "CREATE_ACCOUNTID", fieldType = FieldType.STRING, pk = false)
    private String createAccountid;
    @FieldAnnotation(fieldName = "CREATE_DATE", fieldType = FieldType.DATE, pk = false)
    private Date createDate;
    @FieldAnnotation(fieldName = "UPDATE_ACCOUNTID", fieldType = FieldType.STRING, pk = false)
    private String updateAccountid;
    @FieldAnnotation(fieldName = "UPDATE_DATE", fieldType = FieldType.DATE, pk = false)
    private Date updateDate;
    @FieldAnnotation(fieldName = "PUBLISH_DATE", fieldType = FieldType.DATE, pk = false)
    private Date publishDate;

    public MessageNotice(MessageNoticeTemplate template, Map<String, String> extraParam) {
        if (!TypeChecker.isNull(template)) {
//            MessageNoticeTranslator messageNoticeTranslator = new MessageNoticeTranslator();
//            messageNoticeTranslator.setExtraParam(extraParam);
            this.category = template.getCategory();
            this.subCategory = template.getSubCategory();
            this.messageTag = template.getMessageTag();
            this.imageId = template.getImageId();
            this.subject = template.getSubject();
            this.content = template.getContent();
            // 替换 content
            this.content = template.getContent();//messageNoticeTranslator.translate(template.getContent());
            // 替换 URL
            this.messageUrl = template.getMessageUrl();//messageNoticeTranslator.translate(template.getMessageUrl());
            this.source = template.getSource();
            this.achieve = template.getAchieve();
            this.authority = template.getAuthority();
            this.rankTop = template.getRankTop();
            this.status = template.getStatus();
            this.rcdsts = template.getRcdsts();
            this.version = template.getVersion();
            this.seqno = template.getSeqno();
            this.messageType = template.getMessageType();
            this.templateId = template.getTemplateId();
            this.createAccountid = StringUtil.getValueByDef(template.getCreateAccountid(), "universeSun");
            this.createDate = new Date();
            this.publishDate = new Date();
        }
    }

    public MessageNotice() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getMessageTag() {
        return messageTag;
    }

    public void setMessageTag(String messageTag) {
        this.messageTag = messageTag;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAchieve() {
        return achieve;
    }

    public void setAchieve(String achieve) {
        this.achieve = achieve;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getRankTop() {
        return rankTop;
    }

    public void setRankTop(String rankTop) {
        this.rankTop = rankTop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRcdsts() {
        return rcdsts;
    }

    public void setRcdsts(String rcdsts) {
        this.rcdsts = rcdsts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getSeqno() {
        return seqno;
    }

    public void setSeqno(Long seqno) {
        this.seqno = seqno;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCreateAccountid() {
        return createAccountid;
    }

    public void setCreateAccountid(String createAccountid) {
        this.createAccountid = createAccountid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateAccountid() {
        return updateAccountid;
    }

    public void setUpdateAccountid(String updateAccountid) {
        this.updateAccountid = updateAccountid;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}

