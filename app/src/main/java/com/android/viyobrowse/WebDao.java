package com.android.viyobrowse;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WebDao {

    @Insert
    void insertURL(WebModel viewmodel);

    @Query("DELETE FROM history")
    void deleteAllHistory();

    @Query("SELECT * FROM history")
    List<String> getAllURL();

    @Insert
    void insertWebPage(String webPage);
}
