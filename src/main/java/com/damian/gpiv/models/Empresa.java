package com.damian.gpiv.models;

public class Empresa {

    private String nombre;
    private int id;
    private String tipo;
    private String email;
    private String estado;
    private String cuit;
    private String actividadPrincipal;
    private String direccion;
    private String referente;
    private String telefono;
    private String rubro;
    private String descripcionServicio;

    public Empresa(String nombre, int id, String tipo, String email, String estado,
                   String cuit, String actividadPrincipal,
                   String direccion, String referente, String telefono,
                   String rubro, String descripcionServicio) {
        this.nombre = nombre;
        this.id = id;
        this.tipo = tipo;
        this.email = email;
        this.estado = estado;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.direccion = direccion;
        this.referente = referente;
        this.telefono = telefono;
        this.rubro = rubro;
        this.descripcionServicio = descripcionServicio;
    }

    public Empresa(String nombre, int id, String tipo, String email, String estado) {
        this.nombre = nombre;
        this.id = id;
        this.tipo = tipo;
        this.email = email;
        this.estado = estado;
    }

    public String getNombre() { return nombre; }
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getEmail() { return email; }
    public String getEstado() { return estado; }
    public String getCuit() { return cuit; }
    public String getActividadPrincipal() { return actividadPrincipal; }
    public String getDireccion() { return direccion; }
    public String getReferente() { return referente; }
    public String getTelefono() { return telefono; }
    public String getRubro() { return rubro; }
    public String getDescripcionServicio() { return descripcionServicio; }

    public void setEstado(String estado) { this.estado = estado; }
    public void setCuit(String cuit) { this.cuit = cuit; }
    public void setActividadPrincipal(String actividadPrincipal) { this.actividadPrincipal = actividadPrincipal; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setReferente(String referente) { this.referente = referente; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRubro(String rubro) { this.rubro = rubro; }
    public void setDescripcionServicio(String descripcionServicio) { this.descripcionServicio = descripcionServicio; }


    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
