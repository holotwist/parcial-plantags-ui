package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

public abstract class ModuloDecorator implements Dispositivo {

    protected Dispositivo decorado;

    public ModuloDecorator(Dispositivo decorado) {
        this.decorado = decorado;
    }

    @Override
    public String estado() {
        return decorado.estado();
    }

    @Override
    public int prioridad() {
        return decorado.prioridad();
    }

    @Override
    public String tipo() {
        return decorado.tipo();
    }

    @Override
    public String getEstado() {
        return this.estado();
    }

    @Override
    public int getPrioridad() {
        return this.prioridad();
    }

    @Override
    public String getTipo() {
        return this.tipo();
    }

    public String getNombre() {
        if (decorado instanceof DispositivoSimple) {
            return ((DispositivoSimple) decorado).getNombre();
        }
        if (decorado instanceof DispositivoCompuesto) {
            return ((DispositivoCompuesto) decorado).getNombre();
        }
        return this.tipo();
    }
}