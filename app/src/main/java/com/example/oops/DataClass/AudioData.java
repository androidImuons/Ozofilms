package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioData {
    @SerializedName("language")
    @Expose
    private Integer language;
    @SerializedName("subtitle_fileLink")
    @Expose
    private String subtitleFileLink;
}
