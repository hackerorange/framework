package com.ab.us.framework.service.entity;

import com.ab.us.framework.db.annotation.FieldAnnotation;
import com.ab.us.framework.db.annotation.FieldType;
import com.ab.us.framework.db.annotation.TableAnnotation;
import com.ab.us.framework.db.entity.BaseEntity;
import com.ab.us.framework.db.entity.OrderBy;

import java.util.Date;

/** LFA_MESSAGE_ACCOUNT
	MESSAGE_ID    VARCHAR2(64)
	ACCOUNT_ID    VARCHAR2(64)
	CREATE_DATE    DATE
*/
@TableAnnotation(tableName="LFA_MESSAGE_ACCOUNT")
public class MessageAccount extends OrderBy implements BaseEntity {
	@FieldAnnotation(fieldName = "MESSAGE_ID", fieldType = FieldType.STRING, pk = false)
	private String messageId;
	@FieldAnnotation(fieldName = "ACCOUNT_ID", fieldType = FieldType.STRING, pk = false)
	private String accountId;
	@FieldAnnotation(fieldName = "CREATE_DATE", fieldType = FieldType.DATE, pk = false)
	private Date createDate;

	public void setMessageId(String messageId){
		this.messageId = messageId;
	}
	public String getMessageId(){
		return messageId;
	}
	public void setAccountId(String accountId){
		this.accountId = accountId;
	}
	public String getAccountId(){
		return accountId;
	}
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	public Date getCreateDate(){
		return createDate;
	}
}

