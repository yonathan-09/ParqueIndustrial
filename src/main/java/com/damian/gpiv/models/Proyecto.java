package com.damian.gpiv.models;

public class Proyecto {

    private int id;
    private String nombre;
    private String descripcion;
    private String estado;
    private Integer empresaId;

    public Proyecto(String nombre, String descripcion, String estado, Integer empresaId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.empresaId = empresaId;
    }

    public Proyecto(int id, String nombre, String descripcion, String estado, Integer empresaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.empresaId = empresaId;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getEmpresaId() {
        return empresaId;
    }

}
