package com.damian.gpiv.models;

public class Usuario {

    private int id;
    private String nombre;
    private String rol;
    private String password;

    public Usuario(String nombre, String rol, String password) {
        this.nombre = nombre;
        this.rol = rol;
        this.password = password;
    }

    public Usuario(int id, String nombre, String rol, String password) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

}
