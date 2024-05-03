package com.android.viyobrowse;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WebModel.class},version = 1)
public abstract class WebDatabase extends RoomDatabase {
    public static WebDao webDao() {
        return null;
    }
}
