package com.xenderodriguezlopez.babynotes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xenderodriguezlopez.babynotes.models.Cita;

import java.util.List;

@Dao
public interface CitaDao {
    @Insert
    void insert(Cita cita);

    @Update
    void update(Cita cita);

    @Delete
    void delete(Cita cita);

    @Query("SELECT * FROM citas ORDER BY fecha ASC")
    List<Cita> getAllCitas();

    @Query("SELECT * FROM citas WHERE fecha = :date ORDER BY hora ASC")
    List<Cita> getCitasByDate(String date);

    @Query("SELECT * FROM citas WHERE prioridad = :priority ORDER BY fecha ASC, hora ASC")
    List<Cita> getCitasByPriority(String priority);

}
