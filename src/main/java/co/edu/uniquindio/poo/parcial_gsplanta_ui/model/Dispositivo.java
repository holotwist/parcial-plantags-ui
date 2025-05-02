package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

/**
 * Interfaz común para dispositivos de seguridad.
 */
public interface Dispositivo {
    String estado();
    int prioridad();
    String tipo();

    default String getEstado() {
        return estado();
    }
    default int getPrioridad() {
        return prioridad();
    }
    default String getTipo() {
        return tipo();
    }
}