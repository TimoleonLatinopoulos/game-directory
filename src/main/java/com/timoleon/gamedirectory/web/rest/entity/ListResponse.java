package com.timoleon.gamedirectory.web.rest.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "List")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ListResponse<T> {

    private LocalDateTime timestamp;

    private Collection<T> content;

    private int count;

    public ListResponse() {
        timestamp = LocalDateTime.now();
    }

    public ListResponse(Collection<T> content) {
        this();
        this.content = content;
        count = content.size();
    }

    @XmlElement(name = "timestamp")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @XmlElementWrapper(name = "contents")
    @XmlElement(name = "item")
    public Collection<T> getContent() {
        return content;
    }

    public void setContent(Collection<T> content) {
        this.content = content;
    }

    @XmlElement(name = "count")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
