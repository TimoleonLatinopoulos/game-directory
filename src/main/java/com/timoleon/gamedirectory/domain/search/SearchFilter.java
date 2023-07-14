package com.timoleon.gamedirectory.domain.search;

import java.io.Serializable;
import java.util.List;

public class SearchFilter implements Serializable {

    private String logic;

    private List<SearchFilterItem> filters;

    public SearchFilter() {}

    public SearchFilter(String logic, List<SearchFilterItem> filters) {
        this.logic = logic;
        this.filters = filters;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public List<SearchFilterItem> getFilters() {
        return filters;
    }

    public void setFilters(List<SearchFilterItem> filters) {
        this.filters = filters;
    }
}
