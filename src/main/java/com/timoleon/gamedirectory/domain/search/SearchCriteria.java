package com.timoleon.gamedirectory.domain.search;

import java.io.Serializable;
import java.util.List;

public class SearchCriteria implements Serializable {

    private SearchFilter filter;

    private Integer page;

    private Integer pageSize;

    private List<SearchSortItem> sort;

    public SearchCriteria() {}

    public SearchCriteria(SearchFilter filter, Integer page, Integer pageSize, List<SearchSortItem> sort) {
        this.filter = filter;
        this.page = page;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public SearchFilter getFilter() {
        return filter;
    }

    public void setFilter(SearchFilter filter) {
        this.filter = filter;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<SearchSortItem> getSort() {
        return sort;
    }

    public void setSort(List<SearchSortItem> sort) {
        this.sort = sort;
    }
}
