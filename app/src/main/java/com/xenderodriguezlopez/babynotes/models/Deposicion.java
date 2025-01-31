package com.xenderodriguezlopez.babynotes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "deposiciones")
public class Deposicion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fecha;
    private String hora;
    private String color;
    private String textura;
    private String comentarios;

    // Constructor vacío requerido por Room
    public Deposicion() {
    }

    // Constructor adicional, ignorado por Room
    @Ignore
    public Deposicion(String fecha, String hora, String color, String textura, String comentarios) {
        this.fecha = fecha;
        this.hora = hora;
        this.color = color;
        this.textura = textura;
        this.comentarios = comentarios;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextura() {
        return textura;
    }

    public void setTextura(String textura) {
        this.textura = textura;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
