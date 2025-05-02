package co.edu.uniquindio.poo.parcial_gsplanta_ui.model.command;

import co.edu.uniquindio.poo.parcial_gsplanta_ui.model.*;
import lombok.Getter;

/**
 * Comando concreto para añadir un nuevo dispositivo al CentralMonitoreo.
 */
public class AddDeviceCommand implements Command {

    private final CentralMonitoreo centralMonitoreo;
    private final String deviceType;
    private final String name;
    private final int priority;
    private final boolean isActive; // Solo para dispositivos Simples
    private final Dispositivo baseDevice; // Solo para Decorators

    /**
     * -- GETTER --
     *  Obtiene el dispositivo creado por el método execute.
     *  Devuelve null si execute() no ha sido llamada o ha fallado.
     *
     * @return El dispositivo creado o Null.
     */
    // Realiza un seguimiento del dispositivo creado por execute()
    @Getter
    private Dispositivo createdDevice = null;

    public AddDeviceCommand(CentralMonitoreo centralMonitoreo, String deviceType, String name, int priority, boolean isActive, Dispositivo baseDevice) {
        this.centralMonitoreo = centralMonitoreo;
        this.deviceType = deviceType;
        this.name = name;
        this.priority = priority;
        this.isActive = isActive;
        this.baseDevice = baseDevice;
    }

    @Override
    public boolean execute() {
        Dispositivo newDevice = null;

        try {
            switch (deviceType) {
                case "Simple":
                    newDevice = new DispositivoSimple(name, isActive, priority);
                    break;
                case "Firewall":
                    if (baseDevice == null) {
                        System.err.println("Error: Base device cannot be null for Firewall.");
                        return false; // Debería detectarse antes mediante validación, pero es una buena práctica.
                    }
                    newDevice = new Firewall(baseDevice);
                    break;
                case "Detector de Intrusos":
                    if (baseDevice == null) {
                        System.err.println("Error: Base device cannot be null for DetectorIntrusos.");
                        return false;
                    }
                    newDevice = new DetectorIntrusos(baseDevice);
                    break;
                default:
                    // No debería ocurrir si la validación es correcta
                    System.err.println("Error Interno: Tipo de dispositivo no reconocido en comando: " + deviceType);
                    return false;
            }

            if (newDevice != null) {
                centralMonitoreo.addDevice(newDevice); // Añadir al modelo
                this.createdDevice = newDevice; // Almacenar para quizá una funcionalidad en el futuro
                return true;
            }
        } catch (Exception e) {
            // Registrar errores inesperados durante la creación o adición de dispositivos
            System.err.println("Error executing AddDeviceCommand: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false; // No debería llegar aquí normalmente
    }

    /**
     * Helper para obtener un nombre fácil de usar para el dispositivo creado,
     * especialmente para decorators en los que la entrada 'nombre' puede ser ignorada.
     * @return El nombre/tipo del dispositivo creado, o una cadena por defecto.
     */
    public String getCreatedDeviceName() {
        if (createdDevice != null) {
            // Para decorators, getTipo() proporciona un nombre más descriptivo
            if (createdDevice instanceof ModuloDecorator) {
                return createdDevice.getTipo();
            }
            // Para dispositivos simples, se usa el nombre proporcionado
            if (createdDevice instanceof DispositivoSimple) {
                return ((DispositivoSimple) createdDevice).getNombre();
            }
            // Fallback
            return createdDevice.getTipo();
        }
        // Si la creación falla o no se ejecuta, utiliza el nombre de entrada original como fallback
        return this.name != null ? this.name : "Dispositivo Desconocido";
    }
}