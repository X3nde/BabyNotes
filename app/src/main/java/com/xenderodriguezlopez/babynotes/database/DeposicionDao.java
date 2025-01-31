package com.xenderodriguezlopez.babynotes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xenderodriguezlopez.babynotes.models.Deposicion;

import java.util.List;

@Dao
public interface DeposicionDao {
    @Query("SELECT * FROM deposiciones ORDER BY fecha DESC, hora DESC")
    List<Deposicion> getAllDeposiciones();

    @Insert
    void insert(Deposicion deposicion);

    @Update
    void update(Deposicion deposicion);

    @Delete
    void delete(Deposicion deposicion);
}
