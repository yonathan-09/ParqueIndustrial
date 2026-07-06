package com.damian.gpiv.models;

public class Usuario {

    private int id;
    private String nombre;
    private String rol;
    private String password;
    private Integer empresaId; // NUEVO: Permite enlazar el usuario a una empresa aprobada (puede ser null)

    // Constructor sin ID (para crear nuevos usuarios sin empresa_id)
    public Usuario(String nombre, String rol, String password) {
        this.nombre = nombre;
        this.rol = rol;
        this.password = password;
        this.empresaId = null;
    }

    // Constructor con ID (para cargar administradores u otros roles desde la BD sin empresa)
    public Usuario(int id, String nombre, String rol, String password) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.password = password;
        this.empresaId = null;
    }

    // NUEVO: Constructor completo con empresaId (para el flujo del representante de empresa)
    public Usuario(int id, String nombre, String rol, String password, Integer empresaId) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.password = password;
        this.empresaId = empresaId;
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

    // NUEVOS: Métodos Getter y Setter para la empresa asociada
    public Integer getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }
}