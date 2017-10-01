package com.ab.us.framework.db.annotation;

/**
 * @ClassName: MethodType
 * @Description: TODO(检查数据项对应的方法类型)
 * @author xusisheng
 * @date 2017-02-27 11:15:33
 *
 */
public enum MethodType {
	ALL("0"), // 增加
	ADD("1"), // 增加
	MODIFY("2"), // 修改
	DEL("3"), // 删除
	QRY("4"), // 查询
	ADD_MOD("12"), // 添加修改
	ADD_DEL("13"), // 添加删除
	MOD_DEL("23"), // 修改删除
	ADD_MOD_DEL("123"), // 添加,修改,删除
	ADD_QRY("14"), MOD_QRY("24"), DEL_QRY("34"), ADD_MOD_QRY("124"), MOD_DEL_QRY("234"), ADD_MOD_DEL_QRY("1234");

	private String name;

	private MethodType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
