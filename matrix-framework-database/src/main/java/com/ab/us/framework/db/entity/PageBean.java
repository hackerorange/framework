package com.ab.us.framework.db.entity;

/**
* @ClassName: PageBean
* @Description: TODO(分页相关bean)
* @author xusisheng
* @date 2017年06月05日15:23:36
*
*/
public class PageBean {

	private Integer page;

	private Integer page_size;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}

}
