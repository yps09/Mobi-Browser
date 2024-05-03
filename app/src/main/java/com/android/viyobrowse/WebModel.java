package com.android.viyobrowse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class WebModel {

    @PrimaryKey(autoGenerate = true)
    public  int id;

    public  String webUrl;
    public WebModel(String webUrl)
    {
        this.webUrl = webUrl;

    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}

