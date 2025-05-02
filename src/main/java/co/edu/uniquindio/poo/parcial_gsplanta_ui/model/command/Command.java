package co.edu.uniquindio.poo.parcial_gsplanta_ui.model.command;

/**
 * Interfaz para el patrón Command.
 * Representa una acción a ser ejecutada.
 */
public interface Command {
    /**
     * Ejecuta el comando.
     * @return verdadero si el comando se ejecutó exitosamente, falso si no.
     */
    boolean execute();
}