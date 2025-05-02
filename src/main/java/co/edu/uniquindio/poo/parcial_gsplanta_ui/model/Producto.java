package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Producto {
    @Getter
    private String nombre;
    private List<String> componentes;
    @Getter
    private double precio;

    private Producto(Builder builder) {
        this.nombre = builder.nombre;
        this.componentes = builder.componentes;
        this.precio = builder.precio;
    }

    public List<String> getComponentes() {
        // Devolver una copia para evitar modificaciones externas
        return new ArrayList<>(componentes);
    }

    // Helper para mostrar componentes en una sola columna
    public String getComponentesAsString() {
        return String.join(", ", componentes);
    }

    public boolean contiene(String componente) {
        // La comparación no distingue entre mayúsculas y minúsculas y recorta los espacios en blanco
        String lowerComp = componente.trim().toLowerCase();
        return componentes.stream().anyMatch(c -> c.equals(lowerComp));
    }

    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.2f", precio) + " - Componentes: " + getComponentesAsString();
    }

    public static class Builder {
        private String nombre = "N/A";
        private List<String> componentes = new ArrayList<>();
        private double precio = 0.0;

        public Builder setNombre(String nombre) {
            this.nombre = (nombre != null && !nombre.trim().isEmpty()) ? nombre : "N/A";
            return this;
        }

        public Builder addComponente(String nombre) {
            if (nombre != null && !nombre.trim().isEmpty()) {
                componentes.add(nombre.trim().toLowerCase());
            }
            return this;
        }

        public Builder setPrecio(double precio) {
            this.precio = Math.max(0.0, precio);
            return this;
        }

        public Producto build() {
            if ("N/A".equals(nombre)) {
                System.err.println("Warning: Building Producto without a proper name.");
            }
            if (componentes.isEmpty()) {
                System.err.println("Warning: Building Producto " + nombre + " without components.");
            }
            return new Producto(this);
        }
    }
}