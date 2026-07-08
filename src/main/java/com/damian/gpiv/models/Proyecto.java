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
    private int porcentajeAvance; // NUEVO ATRIBUTO: Guarda el avance del 1 al 100

    // Constructor para registrar un proyecto nuevo (por defecto el avance iniciará en 0 en la BD)
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
        this.porcentajeAvance = 0;
    }

    // MODIFICADO: Ahora este constructor también mapea el porcentaje_avance que viene de la BD
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
        this.porcentajeAvance = 0; // Se inicializa, y ProyectoService se encargará de asignarle el valor real con el setter
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

    // NUEVO GETTER
    public int getPorcentajeAvance() {
        return porcentajeAvance;
    }

    // NUEVO SETTER
    public void setPorcentajeAvance(int porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    @Override
    public String toString() {
        String nombreEmpresa = (empresa != null) ? empresa.getNombre() : "Desconocida";
        int idEmpresa = (empresa != null) ? empresa.getId() : (empresaId != null ? empresaId : 0);

        return "Proyecto ID: " + id +
                " | Nombre: " + nombre.toUpperCase() +
                " | Estado: " + estado.toUpperCase() +
                " | Empresa: " + nombreEmpresa + " (ID: " + idEmpresa + ") | Avance: " + porcentajeAvance + "%";
    }
}