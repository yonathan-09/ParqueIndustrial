package com.damian.gpiv.models;

public class Empresa {

    private String nombre;
    private int id;
    private String tipo;
    private String estado;
    private String email;

    public Empresa (String nombre, int id, String tipo, String estado, String email) {
        this.nombre = nombre;
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.email = email;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getId() {return this.id;}

    public String getTipo() {
        return this.tipo;
    }

    public String getEstado() {
        return this.estado;
    }

    public String getEmail() {
        return this.email;
    }

}
