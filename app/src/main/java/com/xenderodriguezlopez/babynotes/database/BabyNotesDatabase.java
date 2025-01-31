package com.xenderodriguezlopez.babynotes.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.xenderodriguezlopez.babynotes.models.Cita;
import com.xenderodriguezlopez.babynotes.models.Medicamento;
import com.xenderodriguezlopez.babynotes.models.Sueno;
import com.xenderodriguezlopez.babynotes.models.Deposicion;
import com.xenderodriguezlopez.babynotes.models.Aceleration;

@Database(entities = {Cita.class, Medicamento.class, Sueno.class, Deposicion.class, Aceleration.class}, version = 4, exportSchema = false)
public abstract class BabyNotesDatabase extends RoomDatabase {

    private static BabyNotesDatabase instance;

    public abstract CitaDao citaDao();
    public abstract MedicamentoDao medicamentoDao();
    public abstract SuenoDao suenoDao();
    public abstract DeposicionDao deposicionDao();
    public abstract AcelerationDao acelerationDao();

    public static synchronized BabyNotesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BabyNotesDatabase.class, "babynotes_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
