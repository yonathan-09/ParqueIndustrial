package com.damian.gpiv.models;

public class Proyecto {
    private Integer solicitudId;
    private int id;
    private String nombre;
    private String descripcion;
    private String estado;
    private Integer empresaId;
    private double superficieLote;
    private Empresa empresa;

    public Proyecto(
            String nombre,
            String descripcion,
            String estado,
            Integer empresaId,
            double superficieLote) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.empresaId = empresaId;
        this.superficieLote = superficieLote;
    }
    public Proyecto(
            int id,
            String nombre,
            String descripcion,
            String estado,
            Integer empresaId,
            Integer solicitudId,
            double superficieLote) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.empresaId = empresaId;
        this.solicitudId = solicitudId;
        this.superficieLote = superficieLote;
    }



    public Integer getSolicitudId() {
        return solicitudId;
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

    public double getSuperficieLote() {
        return superficieLote;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        String nombreEmpresa = (empresa != null) ? empresa.getNombre() : "Desconocida";
        int idEmpresa = (empresa != null) ? empresa.getId() : empresaId;

        return "Proyecto ID: " + id +
                " | Nombre: " + nombre.toUpperCase() +
                " | Estado: " + estado.toUpperCase() +
                " | Empresa: " + nombreEmpresa + " (ID: " + idEmpresa + ")";
    }
}
