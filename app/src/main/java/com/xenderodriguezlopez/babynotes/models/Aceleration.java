package com.xenderodriguezlopez.babynotes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "aceleration_data")
public class Aceleration {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String startTimestamp;   // Formato "YYYY-MM-DD HH:mm:ss"
    public long durationMillis;     // Duración del movimiento en milisegundos
    public float avgX;             // Aceleración media en el eje X
    public float avgY;             // Aceleración media en el eje Y
    public float avgZ;             // Aceleración media en el eje Z

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public float getAvgX() {
        return avgX;
    }

    public void setAvgX(float avgX) {
        this.avgX = avgX;
    }

    public float getAvgY() {
        return avgY;
    }

    public void setAvgY(float avgY) {
        this.avgY = avgY;
    }

    public float getAvgZ() {
        return avgZ;
    }

    public void setAvgZ(float avgZ) {
        this.avgZ = avgZ;
    }

}

