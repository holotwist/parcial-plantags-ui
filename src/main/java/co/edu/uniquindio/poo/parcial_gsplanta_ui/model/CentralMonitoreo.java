package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class CentralMonitoreo {

    private List<Dispositivo> dispositivosOriginales;
    @Getter
    private List<Dispositivo> dispositivosActuales;

    public CentralMonitoreo(List<Dispositivo> dispositivos) {
        this.dispositivosOriginales = new ArrayList<>(dispositivos);
        this.dispositivosActuales = new ArrayList<>(dispositivos);
    }

    public void addDevice(Dispositivo dispositivo) {
        if (dispositivo != null) {
            this.dispositivosOriginales.add(dispositivo);
            this.dispositivosActuales.add(dispositivo);
        }
    }

    public List<Dispositivo> getPotentialDecoratables() {
        return this.dispositivosOriginales.stream()
                //.filter(d -> d instanceof DispositivoSimple) // Estricto: sólo dispositivos Simple
                .filter(d -> !(d instanceof DispositivoCompuesto)) // Más permisivo: todo lo que no sea composite
                .collect(Collectors.toList());
    }

    public List<Dispositivo> getAllOriginalDevices() {
        return new ArrayList<>(this.dispositivosOriginales); // Retornar copia
    }


    public void resetDispositivos() {
        this.dispositivosActuales = new ArrayList<>(this.dispositivosOriginales);
    }

    public void ordenarPorPrioridad() {
        Collections.sort(this.dispositivosActuales, Comparator.comparingInt(Dispositivo::getPrioridad).reversed());
    }

    public List<Dispositivo> filtrarConModulos() {
        this.dispositivosActuales = this.dispositivosOriginales.stream() // Filtrar de lista original
                .filter(d -> d instanceof ModuloDecorator)
                .collect(Collectors.toList());
        return this.dispositivosActuales;
    }

    public List<String> getEstados() {
        return dispositivosActuales.stream().map(Dispositivo::getEstado).collect(Collectors.toList());
    }

    public Map<String, Integer> contarPorTipo() {
        Map<String, Integer> conteo = new HashMap<>();
        for (Dispositivo d : this.dispositivosOriginales) {
            String tipo = d.getTipo() != null ? d.getTipo() : "Desconocido";
            conteo.put(tipo, conteo.getOrDefault(tipo, 0) + 1);
        }
        return conteo;
    }
}