package com.timoleon.gamedirectory.service.dto.steam;

public class ContentDescriptorsDTO {

    private String notes;

    public ContentDescriptorsDTO() {}

    public ContentDescriptorsDTO(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
