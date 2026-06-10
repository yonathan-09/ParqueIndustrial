package com.damian.gpiv.models;

public class SolicitudRadicacion {

    private int id;
    private String nombre;
    private String email;
    private String cuit;
    private String actividadPrincipal;
    private String direccion;
    private String referente;
    private String telefono;
    private String rubro;
    private String descripcionServicio;
    private String estado;

    public SolicitudRadicacion(
            int id,
            String nombre,
            String email,
            String cuit,
            String actividadPrincipal,
            String direccion,
            String referente,
            String telefono,
            String rubro,
            String descripcionServicio,
            String estado) {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.direccion = direccion;
        this.referente = referente;
        this.telefono = telefono;
        this.rubro = rubro;
        this.descripcionServicio = descripcionServicio;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCuit() {
        return cuit;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getReferente() {
        return referente;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getRubro() {
        return rubro;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + estado + ")";
    }
}
