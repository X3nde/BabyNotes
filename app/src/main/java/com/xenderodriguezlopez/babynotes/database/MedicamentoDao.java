package com.xenderodriguezlopez.babynotes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xenderodriguezlopez.babynotes.models.Medicamento;

import java.util.List;

@Dao
public interface MedicamentoDao {
    @Insert
    void insert(Medicamento medicamento);

    @Update
    void update(Medicamento medicamento);

    @Delete
    void delete(Medicamento medicamento);

    @Query("SELECT * FROM Medicamento")
    List<Medicamento> getAllMedicamentos();
}
