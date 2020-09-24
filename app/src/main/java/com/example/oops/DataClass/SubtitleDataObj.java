package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class    SubtitleDataObj {
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("audioFileLink")
    @Expose
    private Object audioFileLink;

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Object getAudioFileLink() {
        return audioFileLink;
    }

    public void setAudioFileLink(Object audioFileLink) {
        this.audioFileLink = audioFileLink;
    }
}
