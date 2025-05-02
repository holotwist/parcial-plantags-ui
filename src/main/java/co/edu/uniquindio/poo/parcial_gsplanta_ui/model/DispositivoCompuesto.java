package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DispositivoCompuesto implements Dispositivo {

    private List<Dispositivo> hijos = new ArrayList<>();
    @Getter
    private String nombre;

    public DispositivoCompuesto(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(Dispositivo d) {
        hijos.add(d);
    }

    @Override
    public String estado() {
        // Representación más sencilla para TableView
        String hijosTipos = hijos.stream()
                .map(Dispositivo::tipo)
                .limit(3) // Limitar los tipos de hijos mostrados por brevedad
                .collect(Collectors.joining(", "));
        if (hijos.size() > 3) hijosTipos += "...";

        return "[" + nombre + "] (" + hijos.size() + " hijos: " + hijosTipos + "), Prioridad Max: " + prioridad();
    }

    @Override
    public int prioridad() {
        return hijos.stream().mapToInt(Dispositivo::prioridad).max().orElse(0);
    }

    @Override
    public String tipo() {
        return nombre + " (Compuesto)";
    }

    public String estadoDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append("Compuesto: ").append(nombre).append(" (Prioridad Max: ").append(prioridad()).append(")");
        for (Dispositivo d : hijos) {
            sb.append("\n -> ").append(d.estado());
        }
        return sb.toString();
    }

    public List<Dispositivo> getHijos() { return new ArrayList<>(hijos); }

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