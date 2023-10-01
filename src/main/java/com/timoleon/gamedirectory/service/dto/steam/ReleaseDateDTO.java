package com.timoleon.gamedirectory.service.dto.steam;

public class ReleaseDateDTO {

    private String date;
    private Boolean coming_soon;

    public ReleaseDateDTO() {}

    public ReleaseDateDTO(String date, Boolean coming_soon) {
        this.date = date;
        this.coming_soon = coming_soon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getComing_soon() {
        return coming_soon;
    }

    public void setComing_soon(Boolean coming_soon) {
        this.coming_soon = coming_soon;
    }
}
