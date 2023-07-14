package com.timoleon.gamedirectory.domain.search;

import java.io.Serializable;

public class SearchSortItem implements Serializable {

    private String field;

    private String dir;

    public SearchSortItem() {}

    public SearchSortItem(String field, String dir) {
        this.field = field;
        this.dir = dir;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
