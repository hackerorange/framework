package com.ab.us.framework.service.entity;

import com.ab.us.framework.db.annotation.FieldAnnotation;
import com.ab.us.framework.db.annotation.FieldType;
import com.ab.us.framework.db.annotation.TableAnnotation;
import com.ab.us.framework.db.entity.BaseEntity;
import com.ab.us.framework.db.entity.OrderBy;

import java.util.Date;

/** LFA_MESSAGE_NOTICE_TEMPLATE
	TEMPLATE_ID    VARCHAR2(64)
	CATEGORY    VARCHAR2(64)
	SUB_CATEGORY    VARCHAR2(64)
	MESSAGE_TAG    CHAR(3)
	IMAGE_ID    VARCHAR2(64)
	SUBJECT    VARCHAR2(100)
	CONTENT    VARCHAR2(300)
	MESSAGE_URL    VARCHAR2(300)
	SOURCE    VARCHAR2(50)
	ACHIEVE    VARCHAR2(20)
	AUTHORITY    VARCHAR2(2)
	RANK_TOP    CHAR(1)
	STATUS    CHAR(2)
	RCDSTS    CHAR(1)
	VERSION    VARCHAR2(15)
	SEQNO    NUMBER(38)
	MESSAGE_TYPE    CHAR(1)
	CREATE_ACCOUNTID    VARCHAR2(64)
	CREATE_DATE    DATE
	UPDATE_ACCOUNTID    VARCHAR2(64)
	UPDATE_DATE    DATE
	TEMPLATE_NAME    VARCHAR2(64)
	TEMPLATE_LINKAGE_CLASS_NAME    VARCHAR2(64)
	TEMPLATE_ASSIGN_CLASS_NAME    VARCHAR2(64)
*/
@TableAnnotation(tableName="LFA_MESSAGE_NOTICE_TEMPLATE")
public class MessageNoticeTemplate extends OrderBy implements BaseEntity {
	@FieldAnnotation(fieldName = "TEMPLATE_ID", fieldType = FieldType.STRING, pk = false)
	private String templateId;
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
	@FieldAnnotation(fieldName = "CREATE_ACCOUNTID", fieldType = FieldType.STRING, pk = false)
	private String createAccountid;
	@FieldAnnotation(fieldName = "CREATE_DATE", fieldType = FieldType.DATE, pk = false)
	private Date createDate;
	@FieldAnnotation(fieldName = "UPDATE_ACCOUNTID", fieldType = FieldType.STRING, pk = false)
	private String updateAccountid;
	@FieldAnnotation(fieldName = "UPDATE_DATE", fieldType = FieldType.DATE, pk = false)
	private Date updateDate;
	@FieldAnnotation(fieldName = "TEMPLATE_NAME", fieldType = FieldType.STRING, pk = false)
	private String templateName;
	@FieldAnnotation(fieldName = "TEMPLATE_LINKAGE_CLASS_NAME", fieldType = FieldType.STRING, pk = false)
	private String templateLinkageClassName;
	@FieldAnnotation(fieldName = "TEMPLATE_ASSIGN_CLASS_NAME", fieldType = FieldType.STRING, pk = false)
	private String templateAssignClassName;

	public void setTemplateId(String templateId){
		this.templateId = templateId;
	}
	public String getTemplateId(){
		return templateId;
	}
	public void setCategory(String category){
		this.category = category;
	}
	public String getCategory(){
		return category;
	}
	public void setSubCategory(String subCategory){
		this.subCategory = subCategory;
	}
	public String getSubCategory(){
		return subCategory;
	}
	public void setMessageTag(String messageTag){
		this.messageTag = messageTag;
	}
	public String getMessageTag(){
		return messageTag;
	}
	public void setImageId(String imageId){
		this.imageId = imageId;
	}
	public String getImageId(){
		return imageId;
	}
	public void setSubject(String subject){
		this.subject = subject;
	}
	public String getSubject(){
		return subject;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getContent(){
		return content;
	}
	public void setMessageUrl(String messageUrl){
		this.messageUrl = messageUrl;
	}
	public String getMessageUrl(){
		return messageUrl;
	}
	public void setSource(String source){
		this.source = source;
	}
	public String getSource(){
		return source;
	}
	public void setAchieve(String achieve){
		this.achieve = achieve;
	}
	public String getAchieve(){
		return achieve;
	}
	public void setAuthority(String authority){
		this.authority = authority;
	}
	public String getAuthority(){
		return authority;
	}
	public void setRankTop(String rankTop){
		this.rankTop = rankTop;
	}
	public String getRankTop(){
		return rankTop;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getStatus(){
		return status;
	}
	public void setRcdsts(String rcdsts){
		this.rcdsts = rcdsts;
	}
	public String getRcdsts(){
		return rcdsts;
	}
	public void setVersion(String version){
		this.version = version;
	}
	public String getVersion(){
		return version;
	}
	public void setSeqno(Long seqno){
		this.seqno = seqno;
	}
	public Long getSeqno(){
		return seqno;
	}
	public void setMessageType(String messageType){
		this.messageType = messageType;
	}
	public String getMessageType(){
		return messageType;
	}
	public void setCreateAccountid(String createAccountid){
		this.createAccountid = createAccountid;
	}
	public String getCreateAccountid(){
		return createAccountid;
	}
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public void setUpdateAccountid(String updateAccountid){
		this.updateAccountid = updateAccountid;
	}
	public String getUpdateAccountid(){
		return updateAccountid;
	}
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	public Date getUpdateDate(){
		return updateDate;
	}
	public void setTemplateName(String templateName){
		this.templateName = templateName;
	}
	public String getTemplateName(){
		return templateName;
	}
	public void setTemplateLinkageClassName(String templateLinkageClassName){
		this.templateLinkageClassName = templateLinkageClassName;
	}
	public String getTemplateLinkageClassName(){
		return templateLinkageClassName;
	}
	public void setTemplateAssignClassName(String templateAssignClassName){
		this.templateAssignClassName = templateAssignClassName;
	}
	public String getTemplateAssignClassName(){
		return templateAssignClassName;
	}
}

