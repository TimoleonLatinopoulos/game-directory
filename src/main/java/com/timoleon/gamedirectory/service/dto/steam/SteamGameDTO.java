package com.timoleon.gamedirectory.service.dto.steam;

public class SteamGameDTO {

    private Boolean success;
    private SteamGameDetailsDTO data;

    public SteamGameDTO() {}

    public SteamGameDTO(Boolean success, SteamGameDetailsDTO data) {
        this.success = success;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public SteamGameDetailsDTO getData() {
        return data;
    }

    public void setData(SteamGameDetailsDTO data) {
        this.data = data;
    }
}
