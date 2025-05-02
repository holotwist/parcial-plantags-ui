package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

public class Firewall extends ModuloDecorator {

    public Firewall(Dispositivo decorado) {
        super(decorado);
    }

    @Override
    public String estado() {
        // Añadir información del módulo al estado decorado
        return decorado.estado() + " + [Firewall]";
    }

    @Override
    public int prioridad() {
        // Incrementar prioridad
        return decorado.prioridad() + 2;
    }

    @Override
    public String tipo() {
        return decorado.tipo() + " (Firewall)";
    }
}