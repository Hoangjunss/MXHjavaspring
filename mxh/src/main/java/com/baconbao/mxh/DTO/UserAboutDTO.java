package com.baconbao.mxh.DTO;

public class UserAboutDTO {
    private Long aboutId;
    private String description;

    public UserAboutDTO() {
    }

    public UserAboutDTO(Long aboutId, String description) {
        this.aboutId = aboutId;
        this.description = description;
    }

    public Long getAboutId() {
        return aboutId;
    }

    public void setAboutId(Long aboutId) {
        this.aboutId = aboutId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

