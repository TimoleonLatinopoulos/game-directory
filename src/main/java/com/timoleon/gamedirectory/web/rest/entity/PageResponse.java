package com.timoleon.gamedirectory.web.rest.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.data.domain.Page;

@XmlRootElement(name = "Page")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PageResponse<T> extends ListResponse<T> {

    private long totalEntries;

    private int totalPages;

    private boolean firstPage;

    private boolean lastPage;

    public PageResponse() {
        super();
    }

    public PageResponse(Page<T> page) {
        this();
        this.totalEntries = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        if (this.totalEntries != 0 && page.getNumber() > this.totalPages - 1) {
            this.lastPage = false;
        } else {
            this.lastPage = page.isLast();
        }
        this.firstPage = page.isFirst();
        setContent(page.getContent());
        setCount(page.getContent().size());
    }

    @XmlElement(name = "totalEntries")
    public long getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(long totalEntries) {
        this.totalEntries = totalEntries;
    }

    @XmlElement(name = "totalPages")
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @XmlElement(name = "firstPage")
    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    @XmlElement(name = "lastPage")
    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
}
