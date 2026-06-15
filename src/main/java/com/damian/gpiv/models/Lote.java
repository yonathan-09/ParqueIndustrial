package com.damian.gpiv.models;

public class Lote {

    private int id;
    private int superficie;
    private String estado;
    private int empresaId;

    public Lote (int superficie, String estado, int empresaId) {
        this.superficie = superficie;
        this.estado = estado;
        this.empresaId = empresaId;
    }

    public Lote (int id, int superficie, String estado, int empresaId) {
        this.id = id;
        this.superficie = superficie;
        this.estado = estado;
        this.empresaId = empresaId;
    }

    class NotificationService {
        public void notify(String titulo, String mensaje) {
            System.out.println("[" + titulo + "] " + mensaje);
        }
    }

    public int getId() {
        return this.id;
    }

    public int getEmpresaId() {
        return this.empresaId;
    }

    public String getEstado() {
        return estado;
    }

    public int getSuperficie() {
        return superficie;
    }

    @Override
    public String toString() {
        return "Lote " + id + " - Superficie " + superficie + " m2";
    }

}
