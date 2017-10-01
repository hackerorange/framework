package com.ab.us.framework.web.response;

import java.util.List;

/**
 * @author Zhongchongtao
 */
public class PaginationModel<T> {

    private PaginationInfo page;

    private List<T> details;

    public PaginationInfo getPage() {
        return page;
    }

    public PaginationModel<T> setPage(PaginationInfo page) {
        this.page = page;
        return this;
    }

    public List<T> getDetails() {
        return details;
    }

    public PaginationModel setDetails(List<T> details) {
        this.details = details;
        return this;
    }
}
