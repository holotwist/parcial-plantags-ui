package co.edu.uniquindio.poo.parcial_gsplanta_ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;


/**
 * Clase utilitaria para analizar productos en el registro.
 */
public class AnalizadorProductos {

    /**
     * Filtra productos que contienen un componente específico.
     * @param componente nombre del componente
     * @param registro Instancia del RegistroGlobal
     * @return lista de productos filtrados
     */
    public static List<Producto> filtrarPorComponente(String componente, RegistroGlobal registro) {
        List<Producto> resultado = new ArrayList<>();
        List<Producto> productos = registro.getProductos();

        if (componente == null || componente.trim().isEmpty()) {
            return new ArrayList<>(productos);
        }

        for (Producto p : productos) {
            if (p.contiene(componente)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /**
     * Ordena productos por precio ascendente.
     * @param registro Instancia del RegistroGlobal
     * @return lista de productos ordenados
     */
    public static List<Producto> ordenarPorPrecio(RegistroGlobal registro) {
        List<Producto> productos = new ArrayList<>(registro.getProductos());
        // Se usa la función de ordenado interna de Java para mejor eficiencia
        Collections.sort(productos, Comparator.comparingDouble(Producto::getPrecio));
        return productos;
    }

    /**
     * Cuenta cuántos productos hay por nombre.
     * @param registro Instancia del RegistroGlobal
     * @return mapa de nombre de producto a cantidad
     */
    public static Map<String, Integer> contarPorNombre(RegistroGlobal registro) {
        Map<String, Integer> conteo = new HashMap<>();
        List<Producto> productos = registro.getProductos();

        for (Producto p : productos) {
            conteo.put(p.getNombre(), conteo.getOrDefault(p.getNombre(), 0) + 1);
        }
        return conteo;
    }
}