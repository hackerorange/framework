package com.ab.us.framework.service.entity;

import com.ab.us.framework.db.annotation.FieldAnnotation;
import com.ab.us.framework.db.annotation.FieldType;
import com.ab.us.framework.db.annotation.TableAnnotation;
import com.ab.us.framework.db.entity.BaseEntity;
import com.ab.us.framework.db.entity.OrderBy;

import java.util.Date;

/**
 * T_SYSTEM_DICTIONARY DICT_ID VARCHAR2(64) DICT_TYPE VARCHAR2(20) DICT_CODE
 * VARCHAR2(100) DICT_NAME VARCHAR2(300) PARENT_DICT_ID VARCHAR2(64) DICT_ORDER
 * NUMBER(38) STATUS VARCHAR2(64) CREATE_DATE DATE CREATE_ACCOUNT_ID
 * VARCHAR2(64) UPDATE_DATE DATE UPDATE_ACCOUNT_ID VARCHAR2(64) REMARK1
 * VARCHAR2(100) REMARK2 VARCHAR2(100) REMARK3 VARCHAR2(100)
 */
@TableAnnotation(tableName = "T_SYSTEM_DICTIONARY")
public class SystemDictionary extends OrderBy implements BaseEntity {
	@FieldAnnotation(fieldName = "DICT_ID", fieldType = FieldType.STRING, pk = true)
	private String dictId;
	@FieldAnnotation(fieldName = "DICT_TYPE", fieldType = FieldType.STRING, pk = false)
	private String dictType;
	@FieldAnnotation(fieldName = "DICT_CODE", fieldType = FieldType.STRING, pk = false)
	private String dictCode;
	@FieldAnnotation(fieldName = "DICT_NAME", fieldType = FieldType.STRING, pk = false)
	private String dictName;
	@FieldAnnotation(fieldName = "PARENT_DICT_ID", fieldType = FieldType.STRING, pk = false)
	private String parentDictId;
	@FieldAnnotation(fieldName = "DICT_ORDER", fieldType = FieldType.NUMBER, pk = false)
	private Long dictOrder;
	@FieldAnnotation(fieldName = "STATUS", fieldType = FieldType.STRING, pk = false)
	private String status;
	@FieldAnnotation(fieldName = "CREATE_DATE", fieldType = FieldType.DATE, pk = false)
	private Date createDate;
	@FieldAnnotation(fieldName = "CREATE_ACCOUNT_ID", fieldType = FieldType.STRING, pk = false)
	private String createAccountId;
	@FieldAnnotation(fieldName = "UPDATE_DATE", fieldType = FieldType.DATE, pk = false)
	private Date updateDate;
	@FieldAnnotation(fieldName = "UPDATE_ACCOUNT_ID", fieldType = FieldType.STRING, pk = false)
	private String updateAccountId;
	@FieldAnnotation(fieldName = "REMARK1", fieldType = FieldType.STRING, pk = false)
	private String remark1;
	@FieldAnnotation(fieldName = "REMARK2", fieldType = FieldType.STRING, pk = false)
	private String remark2;
	@FieldAnnotation(fieldName = "REMARK3", fieldType = FieldType.STRING, pk = false)
	private String remark3;

	public SystemDictionary() {
	}

	public SystemDictionary(String dictType) {
		this.dictType = dictType;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getParentDictId() {
		return parentDictId;
	}

	public void setParentDictId(String parentDictId) {
		this.parentDictId = parentDictId;
	}

	public Long getDictOrder() {
		return dictOrder;
	}

	public void setDictOrder(Long dictOrder) {
		this.dictOrder = dictOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateAccountId() {
		return createAccountId;
	}

	public void setCreateAccountId(String createAccountId) {
		this.createAccountId = createAccountId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateAccountId() {
		return updateAccountId;
	}

	public void setUpdateAccountId(String updateAccountId) {
		this.updateAccountId = updateAccountId;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
}
