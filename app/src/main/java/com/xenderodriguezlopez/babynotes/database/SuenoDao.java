package com.xenderodriguezlopez.babynotes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xenderodriguezlopez.babynotes.models.Sueno;

import java.util.List;

@Dao
public interface SuenoDao {

    @Insert
    void insert(Sueno sueno);

    @Update
    void update(Sueno sueno);

    @Delete
    void delete(Sueno sueno);

    @Query("SELECT * FROM Sueno ORDER BY horaInicio ASC")
    List<Sueno> getAllSueno();
}
