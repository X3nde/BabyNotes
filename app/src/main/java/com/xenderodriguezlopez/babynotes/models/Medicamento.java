package com.xenderodriguezlopez.babynotes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Medicamento {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String fechaInicio;
    private String fechaFin;
    private String posologia;
    private String cantidad;
    private String notas;
    private boolean recordatorio;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public boolean isRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(boolean recordatorio) {
        this.recordatorio = recordatorio;
    }
}

