package com.ab.us.framework.web.response;

/**
 * @author Zhongchongtao
 */
public class PaginationInfo {

    /**
     * 总的页数
     */
    private int pageTotal=1;
    /**
     * 当前页的记录数量
     */
    private int pageCount=1;
    /**
     * 当前页页码
     */
    private int currentPage=1;
    /**
     * 每一页的记录数量（前台给定或者写死）
     */
    private int pageSize;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}
