package com.damian.gpiv.models;

public class Empresa {

    private String nombre;
    private int id;
    private String tipo;
    private String email;
    private String estado; // nuevo campo

    public Empresa(String nombre, int id, String tipo, String email, String estado) {
        this.nombre = nombre;
        this.id = id;
        this.tipo = tipo;
        this.email = email;
        this.estado = estado;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getId() {
        return this.id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getEmail() {
        return this.email;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", email='" + email + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}