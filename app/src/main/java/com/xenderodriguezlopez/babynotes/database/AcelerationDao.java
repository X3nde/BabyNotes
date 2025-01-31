package com.xenderodriguezlopez.babynotes.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xenderodriguezlopez.babynotes.models.Aceleration;

import java.util.List;

@Dao
public interface AcelerationDao {

    @Insert
    void insertAceleration(Aceleration acceleration);

    @Query("SELECT * FROM aceleration_data")
    List<Aceleration> getAllAcelerations();

}
