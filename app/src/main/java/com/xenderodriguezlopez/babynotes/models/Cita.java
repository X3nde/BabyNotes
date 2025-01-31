package com.xenderodriguezlopez.babynotes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "citas")
public class Cita {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titulo;
    private String fecha;
    private String hora;
    private String especialidad;
    private String prioridad;

    // Constructor vacío requerido por Room
    public Cita() {
    }

    // Constructor con parámetros
    public Cita(String titulo, String fecha, String hora, String especialidad, String prioridad) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
        this.especialidad = especialidad;
        this.prioridad = prioridad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
