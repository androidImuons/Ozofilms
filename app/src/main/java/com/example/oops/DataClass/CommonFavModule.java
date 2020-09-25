package com.example.oops.DataClass;

public class CommonFavModule {
    String name ,shortDiscrp , imagUrl ,movSerId, type;
    int id;

    public CommonFavModule(String name, String shortDiscrp, String imagUrl, String movSerId, String type, int id) {
        this.name = name;
        this.shortDiscrp = shortDiscrp;
        this.imagUrl = imagUrl;
        this.movSerId = movSerId;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDiscrp() {
        return shortDiscrp;
    }

    public void setShortDiscrp(String shortDiscrp) {
        this.shortDiscrp = shortDiscrp;
    }

    public String getImagUrl() {
        return imagUrl;
    }

    public void setImagUrl(String imagUrl) {
        this.imagUrl = imagUrl;
    }

    public String getMovSerId() {
        return movSerId;
    }

    public void setMovSerId(String movSerId) {
        this.movSerId = movSerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
