package com.ab.us.framework.db.util;

import java.util.List;

public class PageInfo<T> {
	private List<T> lists;// 结果集
	private int page;// 结果页数
	private int total;// 结果条数
	private String dataCutoffTime;// 数据截止日期

	public String getDataCutoffTime() {
		return dataCutoffTime;
	}

	public void setDataCutoffTime(String dataCutoffTime) {
		this.dataCutoffTime = dataCutoffTime;
	}

	public PageInfo() {

	}

	/**
	 * Constructor.
	 *
	 * @param list the list of paginate result
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param page the total page of paginate
	 * @param total the total row of paginate
	 */
	public PageInfo(List<T> lists, int page, int total) {
		this.lists = lists;
		this.page = page;
		this.total = total;
	}

	/**
	 * Return list of this page.
	 */
	public List<T> getLists() {
		return lists;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setLists(List<T> lists) {
		this.lists = lists;
	}
}
