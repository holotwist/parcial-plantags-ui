package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

public class DetectorIntrusos extends ModuloDecorator {

    public DetectorIntrusos(Dispositivo decorado) {
        super(decorado);
    }

    @Override
    public String estado() {
        // Añadir información del módulo al estado decorado
        return decorado.estado() + " + [Intrusos]";
    }

    @Override
    public int prioridad() {
        // Incrementa prioridad
        return decorado.prioridad() + 3;
    }

    @Override
    public String tipo() {
        return decorado.tipo() + " (Intrusos)";
    }
}