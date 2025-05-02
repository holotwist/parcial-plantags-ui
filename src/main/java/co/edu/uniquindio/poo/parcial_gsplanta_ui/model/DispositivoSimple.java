package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

import lombok.Getter;

public class DispositivoSimple implements Dispositivo {
    @Getter
    private String nombre;
    @Getter
    private boolean activo;
    private int prioridad;

    public DispositivoSimple(String nombre, boolean activo, int prioridad) {
        this.nombre = nombre;
        this.activo = activo;
        this.prioridad = prioridad;
    }

    @Override
    public String estado() {
        return "[" + nombre + "] Estado: " + (activo ? "Activo" : "Inactivo") + ", Prioridad: " + prioridad;
    }

    @Override
    public int prioridad() {
        return prioridad;
    }

    @Override
    public String tipo() {
        return nombre;
    }

    public String getEstadoActual() { return activo ? "Activo" : "Inactivo"; }

    @Override
    public String getEstado() {
        return estado();
    }

    @Override
    public int getPrioridad() {
        return prioridad();
    }

    @Override
    public String getTipo() {
        return tipo();
    }
}