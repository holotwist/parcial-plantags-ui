package co.edu.uniquindio.poo.parcial_gsplanta_ui;

import co.edu.uniquindio.poo.parcial_gsplanta_ui.model.*;
import co.edu.uniquindio.poo.parcial_gsplanta_ui.model.command.AddDeviceCommand; // Importar Command
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;


import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    // --- Barra lateral y área de contenido ---
    @FXML private VBox sidebar;
    @FXML private StackPane contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnProducts;
    @FXML private Button btnSecurity;
    @FXML private Button btnLogout;

    // --- Páneles de contenido ---
    @FXML private AnchorPane dashboardPane;
    @FXML private AnchorPane productPane;
    @FXML private AnchorPane securityPane;

    // --- Elementos del dashboard ---
    @FXML private Label lblTotalProducts;
    @FXML private Label lblTotalDevices;
    @FXML private TextFlow deviceTypeCountsTextFlow;

    // --- Elementos de gestión de productos ---
    @FXML private TextField txtProductName;
    @FXML private TextField txtProductPrice;
    @FXML private TextField txtProductComponents;
    @FXML private Button btnAddProduct;
    @FXML private TextField txtFilterComponent;
    @FXML private Button btnFilterProduct;
    @FXML private Button btnSortProductPrice;
    @FXML private Button btnShowAllProducts;
    @FXML private Button btnCountProductNames;
    @FXML private TableView<Producto> tvProducts;
    @FXML private TableColumn<Producto, String> colProductName;
    @FXML private TableColumn<Producto, Double> colProductPrice;
    @FXML private TableColumn<Producto, String> colProductComponents;
    @FXML private TextArea taProductOutput;

    // --- Elementos Monitoreo Seguridad ---
    // Tablas
    @FXML private TableView<Dispositivo> tvDevices;
    @FXML private TableColumn<Dispositivo, String> colDeviceType;
    @FXML private TableColumn<Dispositivo, String> colDeviceState;
    @FXML private TableColumn<Dispositivo, Integer> colDevicePriority;
    @FXML private TextArea taDeviceOutput;
    // Botones de acción
    @FXML private Button btnShowDeviceStates;
    @FXML private Button btnSortDevicePriority;
    @FXML private Button btnFilterDeviceModules;
    @FXML private Button btnShowAllDevices;
    @FXML private Button btnCountDeviceTypes;
    // Formulario añadir dispositivo
    @FXML private ComboBox<String> cmbDeviceType;
    @FXML private TextField txtDeviceName;
    @FXML private TextField txtDevicePriority;
    @FXML private CheckBox chkDeviceActive;
    @FXML private ComboBox<Dispositivo> cmbDeviceToDecorate;
    @FXML private Button btnAddDevice;
    @FXML private Label lblDeviceInitialState;
    @FXML private Label lblDeviceToDecorate;


    // --- Datos ---
    private RegistroGlobal registroGlobal;
    private CentralMonitoreo centralMonitoreo;
    private ObservableList<Producto> productObservableList;
    private ObservableList<Dispositivo> deviceObservableList;
    private ObservableList<Dispositivo> decoratableDevicesList; // Esto es para el comboBox


    private Button currentActiveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registroGlobal = RegistroGlobal.getInstancia();
        setupSampleData();

        configureProductTable();
        configureDeviceTable();
        configureAddDeviceForm();

        // Cargar datos iniciales (hardcoded)
        loadProductTable();
        loadDeviceTable();

        showDashboard(); // Mostrar primero el dashboard
    }

    // --- Ver navegación y actualizaciones ---

    @FXML
    private void showDashboard() {
        activatePane(dashboardPane);
        updateActiveButton(btnDashboard);
        updateDashboardData(); // Actualizar conteo al mostrar
    }

    @FXML
    private void showProductManagement() {
        activatePane(productPane);
        updateActiveButton(btnProducts);
    }

    @FXML
    private void showSecurityMonitoring() {
        activatePane(securityPane);
        updateActiveButton(btnSecurity);
        updateDecoratableDevicesCombo(); // Asegurarse de que combo está actualizado
    }

    private void activatePane(Node paneToShow) {
        contentArea.getChildren().forEach(pane -> pane.setVisible(false));
        paneToShow.setVisible(true);
    }

    private void updateActiveButton(Button newlyActiveButton) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("sidebar-button-active");
            if (!currentActiveButton.getStyleClass().contains("sidebar-button")) {
                currentActiveButton.getStyleClass().add("sidebar-button");
            }
        }
        if (newlyActiveButton != null) {
            newlyActiveButton.getStyleClass().remove("sidebar-button");
            newlyActiveButton.getStyleClass().add("sidebar-button-active");
            currentActiveButton = newlyActiveButton;
        } else {
            currentActiveButton = null;
        }
    }

    // --- Lógica del dashboard ---
    private void updateDashboardData() {
        // Conteo productos
        int productCount = registroGlobal.getProductos().size();
        lblTotalProducts.setText(String.valueOf(productCount));

        // Conteo dispositivos
        int deviceCount = centralMonitoreo.getAllOriginalDevices().size();
        lblTotalDevices.setText(String.valueOf(deviceCount));

        // Conteo dispositivos por tipo
        Map<String, Integer> typeCounts = centralMonitoreo.contarPorTipo();
        deviceTypeCountsTextFlow.getChildren().clear(); // Limpiar conteos anteriores
        if (typeCounts.isEmpty()) {
            deviceTypeCountsTextFlow.getChildren().add(new Text("No hay dispositivos registrados."));
        } else {
            List<Node> countNodes = new ArrayList<>();
            typeCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey()) // Ordenar alfabéticamente por tipo
                    .forEach(entry -> {
                        Text typeText = new Text(entry.getKey() + ": ");
                        typeText.getStyleClass().add("device-type-name");

                        Text countText = new Text(String.valueOf(entry.getValue()));
                        countText.getStyleClass().add("device-type-count");

                        Text newline = new Text("\n"); // Nueva línea entre entradas

                        countNodes.add(typeText);
                        countNodes.add(countText);
                        countNodes.add(newline);
                    });
            // Eliminar la última línea nueva si existe
            if (!countNodes.isEmpty()) {
                countNodes.remove(countNodes.size() - 1);
            }
            deviceTypeCountsTextFlow.getChildren().addAll(countNodes);
        }
    }


    // --- Configuraciones de tablas y carga de datos ---

    private void configureProductTable() {
        colProductName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colProductComponents.setCellValueFactory(new PropertyValueFactory<>("componentesAsString"));
        colProductPrice.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        productObservableList = FXCollections.observableArrayList();
        tvProducts.setItems(productObservableList);
        tvProducts.setPlaceholder(new Label("No hay productos para mostrar."));
    }

    private void configureDeviceTable() {
        colDeviceType.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDeviceState.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDevicePriority.setCellValueFactory(new PropertyValueFactory<>("prioridad"));

        colDeviceState.setCellFactory(tc -> {
            TableCell<Dispositivo, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(colDeviceState.widthProperty().subtract(15));
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });

        deviceObservableList = FXCollections.observableArrayList();
        tvDevices.setItems(deviceObservableList);
        tvDevices.setPlaceholder(new Label("No hay dispositivos para mostrar."));
    }

    private void configureAddDeviceForm() {
        cmbDeviceType.getItems().addAll("Simple", "Firewall", "Detector de Intrusos");

        decoratableDevicesList = FXCollections.observableArrayList();
        cmbDeviceToDecorate.setItems(decoratableDevicesList);

        cmbDeviceToDecorate.setConverter(new StringConverter<Dispositivo>() {
            @Override
            public String toString(Dispositivo dispositivo) {
                // Mostrar (Tipo) [Prioridad]
                return (dispositivo != null) ?
                        dispositivo.getTipo() + " [P:" + dispositivo.getPrioridad() + "]"
                        : null;
            }
            @Override
            public Dispositivo fromString(String string) {
                return null;
            }
        });

        // Añadir listener a Device Type ComboBox para controlar la visibilidad de otros campos
        cmbDeviceType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isDecorator = "Firewall".equals(newVal) || "Detector de Intrusos".equals(newVal);
            boolean isSimple = "Simple".equals(newVal);

            // Gestionar la visibilidad/estado gestionado
            lblDeviceToDecorate.setVisible(isDecorator);
            cmbDeviceToDecorate.setVisible(isDecorator);
            lblDeviceToDecorate.setManaged(isDecorator); // Solo ocupa espacio si es visible
            cmbDeviceToDecorate.setManaged(isDecorator);

            // Campos específicos de Simple
            txtDevicePriority.setDisable(isDecorator); // Prioridad es del decorador, no de la base
            txtDevicePriority.setManaged(!isDecorator);
            txtDevicePriority.setVisible(!isDecorator);
            // El Label de prioridad es genérico, siempre visible/managed
            ((Label)txtDevicePriority.getParent().getChildrenUnmodifiable().get(4)).setVisible(true); // Asumiendo posición del label
            ((Label)txtDevicePriority.getParent().getChildrenUnmodifiable().get(4)).setManaged(true);


            lblDeviceInitialState.setVisible(isSimple);
            chkDeviceActive.setVisible(isSimple);
            lblDeviceInitialState.setManaged(isSimple);
            chkDeviceActive.setManaged(isSimple);


            // Borrar combo decorator si el tipo cambia de decorator
            if (!isDecorator) {
                cmbDeviceToDecorate.getSelectionModel().clearSelection();
            } else {
                txtDevicePriority.clear(); // Borrar el campo de prioridad para decorators
            }
            if (isSimple) {
                chkDeviceActive.setSelected(true);
            } else {
                chkDeviceActive.setSelected(false); // Borrar checkbox para no simples
            }
        });

        // Estado inicial: ocultar los campos específicos del decorador/simple hasta que se seleccione el tipo
        lblDeviceToDecorate.setVisible(false);
        cmbDeviceToDecorate.setVisible(false);
        lblDeviceToDecorate.setManaged(false);
        cmbDeviceToDecorate.setManaged(false);
        lblDeviceInitialState.setVisible(false);
        chkDeviceActive.setVisible(false);
        lblDeviceInitialState.setManaged(false);
        chkDeviceActive.setManaged(false);
        txtDevicePriority.setVisible(false); // Empezar oculto hasta seleccionar tipo
        txtDevicePriority.setManaged(false);


        updateDecoratableDevicesCombo(); // Poblado de datos inicial
    }


    private void updateDecoratableDevicesCombo() {
        List<Dispositivo> candidates = centralMonitoreo.getPotentialDecoratables();
        Dispositivo selected = cmbDeviceToDecorate.getValue();
        decoratableDevicesList.setAll(candidates);
        // Intenta volver a seleccionar si el elemento seleccionado anteriormente sigue siendo válido
        if (selected != null && candidates.contains(selected)) {
            cmbDeviceToDecorate.setValue(selected);
        } else {
            cmbDeviceToDecorate.getSelectionModel().clearSelection(); // Borrar si el anterior no es válido
        }
    }


    private void loadProductTable() {
        productObservableList.setAll(registroGlobal.getProductos());
        clearProductOutput();
    }

    private void loadDeviceTable() {
        centralMonitoreo.resetDispositivos();
        deviceObservableList.setAll(centralMonitoreo.getDispositivosActuales());
        clearDeviceOutput();
        // Actualiza también la lista de dispositivos disponibles para la decoración
        updateDecoratableDevicesCombo();
    }

    private void setupSampleData() {
        if (registroGlobal.getProductos().isEmpty()) {
            Producto p1 = new Producto.Builder().setNombre("Jugo Verde").addComponente("Limón").addComponente("Espinaca").setPrecio(12).build();
            Producto p2 = new Producto.Builder().setNombre("Jugo Rojo").addComponente("Fresa").addComponente("Remolacha").setPrecio(15).build();
            Producto p3 = new Producto.Builder().setNombre("Ensalada Mix").addComponente("Tomate").addComponente("Queso").setPrecio(10).build();
            registroGlobal.agregarProducto(p1);
            registroGlobal.agregarProducto(p2);
            registroGlobal.agregarProducto(p3);
        }

        // Crear dispositivos de muestra sólo si la lista está vacía para evitar duplicados al volver a ejecutarla.
        if (centralMonitoreo == null || centralMonitoreo.getAllOriginalDevices().isEmpty()) {
            Dispositivo camara = new DispositivoSimple("Cámara Vestíbulo", true, 3);
            Dispositivo sensor = new DispositivoSimple("Sensor Puerta", false, 2);
            Dispositivo servidor = new DispositivoSimple("Servidor Acceso", true, 5);
            Dispositivo firewallSensor = new Firewall(sensor);
            Dispositivo intrusosCamara = new DetectorIntrusos(camara);
            Dispositivo firewallServidor = new Firewall(servidor);
            Dispositivo intrusosServidor = new DetectorIntrusos(firewallServidor);
            DispositivoCompuesto central = new DispositivoCompuesto("Central Seguridad");
            central.agregar(firewallSensor);
            central.agregar(intrusosCamara);
            DispositivoCompuesto red = new DispositivoCompuesto("Segmento Red");
            red.agregar(intrusosServidor);

            List<Dispositivo> listaDispositivos = new ArrayList<>(Arrays.asList(
                    central, red, camara, sensor, servidor, firewallSensor, intrusosCamara, intrusosServidor
            ));
            centralMonitoreo = new CentralMonitoreo(listaDispositivos);
        }
    }

    // --- Handlers de productos ---
    @FXML private void handleAddProduct() {
        String name = txtProductName.getText().trim();
        String priceStr = txtProductPrice.getText().trim();
        String componentsStr = txtProductComponents.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || componentsStr.isEmpty()) {
            showError("Entrada Inválida", "Nombre, Precio y Componentes son requeridos.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) throw new NumberFormatException("Precio no puede ser negativo.");
        } catch (NumberFormatException e) {
            showError("Formato Inválido", "El precio debe ser un número positivo válido.");
            return;
        }

        Producto.Builder builder = new Producto.Builder().setNombre(name).setPrecio(price);
        String[] components = componentsStr.split(",");
        for (String comp : components) {
            if (!comp.trim().isEmpty()) {
                builder.addComponente(comp.trim());
            }
        }

        Producto newProduct = builder.build();
        registroGlobal.agregarProducto(newProduct);
        loadProductTable(); // Actualizar

        txtProductName.clear();
        txtProductPrice.clear();
        txtProductComponents.clear();
        taProductOutput.setText("Producto '" + name + "' agregado exitosamente.");
        txtProductName.requestFocus();
    }
    @FXML private void handleFilterProduct() {
        String filter = txtFilterComponent.getText().trim();
        List<Producto> filteredList = AnalizadorProductos.filtrarPorComponente(filter, registroGlobal);
        productObservableList.setAll(filteredList);
        if (filter.isEmpty()){
            taProductOutput.setText("Mostrando todos los productos.");
        } else {
            taProductOutput.setText("Filtrado por componente: '" + filter + "'.");
        }
    }
    @FXML private void handleSortProductPrice() {
        List<Producto> sortedList = AnalizadorProductos.ordenarPorPrecio(registroGlobal);
        productObservableList.setAll(sortedList);
        taProductOutput.setText("Productos ordenados por precio (ascendente).");
    }
    @FXML private void handleShowAllProducts() {
        txtFilterComponent.clear();
        loadProductTable();
    }
    @FXML private void handleCountProductNames() {
        Map<String, Integer> counts = AnalizadorProductos.contarPorNombre(registroGlobal);
        StringBuilder sb = new StringBuilder("Conteo por Nombre de Producto:\n");
        if (counts.isEmpty()) {
            sb.append(" (No hay productos)");
        } else {
            counts.forEach((name, count) -> sb.append(" - ").append(name).append(": ").append(count).append("\n"));
        }
        taProductOutput.setText(sb.toString());
    }

    @FXML private void handleShowDeviceStates() {
        loadDeviceTable();
        taDeviceOutput.setText("Mostrando estado actual de todos los dispositivos.");
    }
    @FXML private void handleSortDevicePriority() {
        centralMonitoreo.ordenarPorPrioridad();
        deviceObservableList.setAll(centralMonitoreo.getDispositivosActuales());
        taDeviceOutput.setText("Dispositivos ordenados por prioridad (descendente).");
    }
    @FXML private void handleFilterDeviceModules() {
        // Filtrar desde la lista original para evitar filtrar una lista ya filtrada
        deviceObservableList.setAll(centralMonitoreo.filtrarConModulos());
        taDeviceOutput.setText("Mostrando solo dispositivos con módulos adicionales.");
    }
    @FXML private void handleShowAllDevices() {
        loadDeviceTable();
    }
    @FXML private void handleCountDeviceTypes() {
        Map<String, Integer> counts = centralMonitoreo.contarPorTipo();
        StringBuilder sb = new StringBuilder("Conteo por Tipo de Dispositivo:\n");
        if (counts.isEmpty()) {
            sb.append(" (No hay dispositivos)");
        } else {
            counts.forEach((type, count) -> sb.append(" - ").append(type).append(": ").append(count).append("\n"));
        }
        taDeviceOutput.setText(sb.toString());
    }

    // --- Handler de Dispositivos (Usando Command Pattern) ---
    @FXML
    private void handleAddDevice() {
        String deviceType = cmbDeviceType.getValue();
        String name = txtDeviceName.getText().trim();
        String priorityStr = txtDevicePriority.getText().trim(); // Solo relevante para Simple
        boolean isActive = chkDeviceActive.isSelected(); // Solo relevante para Simple
        Dispositivo baseDevice = cmbDeviceToDecorate.getValue(); // Solo relevante para Decorators

        // --- Inicio Validación ---
        if (deviceType == null || deviceType.isEmpty()) {
            showError("Entrada Inválida", "Seleccione un tipo de dispositivo.");
            return;
        }
        if (name.isEmpty()) {
            showError("Entrada Inválida", "Ingrese un nombre para el dispositivo.");
            return;
        }

        int priority = 0; // Valor por defecto, solo procesado para Simple
        boolean isSimple = "Simple".equals(deviceType);
        boolean isDecorator = "Firewall".equals(deviceType) || "Detector de Intrusos".equals(deviceType);

        if (isSimple) {
            try {
                priority = Integer.parseInt(priorityStr);
                if (priority < 0) throw new NumberFormatException("Prioridad no puede ser negativa.");
            } catch (NumberFormatException e) {
                showError("Formato Inválido", "La prioridad para un dispositivo Simple debe ser un número entero no negativo.");
                return;
            }
        } else if (isDecorator) {
            if (baseDevice == null) {
                showError("Entrada Inválida", "Seleccione un dispositivo base para decorar.");
                return;
            }
            // La prioridad y el estado activo vienen determinados por el dispositivo base y la lógica del decorador, no por la entrada del usuario.
            priority = -1; // Indica que la prioridad no se establece directamente para los decorators
            isActive = false; // Indica que el estado activo no se establece directamente para los decorators
        } else {
            showError("Error Interno", "Tipo de dispositivo no soportado: " + deviceType);
            return;
        }
        // --- Fin Validación ---


        // --- Crea y ejecuta Command ---
        // Nota: Pasa los parámetros relevantes en función del tipo. El constructor de Command se encarga de esto.
        AddDeviceCommand command = new AddDeviceCommand(
                centralMonitoreo,
                deviceType,
                name,
                priority, // Será ignorado por Command si Decorator
                isActive, // Será ignorado por Command si Decorator
                baseDevice // Será ignorado por Command si simple
        );

        boolean success = command.execute();

        // --- Manejar resultado de Command ---
        if (success) {
            String createdDeviceName = command.getCreatedDeviceName(); // Obtener nombre/tipo tras la creación
            loadDeviceTable(); // Actualizar la tabla (esto también actualiza el combo decorador)
            clearAddDeviceForm();
            taDeviceOutput.setText("Dispositivo '" + createdDeviceName + "' agregado exitosamente.");
            updateDashboardData(); // Actualizar conteo del dashboard
        } else {
            // Lo ideal es que los mensajes de error se generen dentro del comando o validación
            // Si execute devuelve false inesperadamente, muestra un error genérico
            // Quizá esto lo mejore en el futuro, quizá no
            showError("Error al Agregar", "No se pudo agregar el dispositivo.");
        }
    }


    private void clearAddDeviceForm() {
        cmbDeviceType.getSelectionModel().clearSelection(); // Esto hace que el listener restablezca la visibilidad
        txtDeviceName.clear();
        txtDevicePriority.clear(); // Borrar aunque esté oculto/desactivado
        chkDeviceActive.setSelected(false); // Restablecer el valor por defecto (el listener quizá lo cambie)
        cmbDeviceToDecorate.getSelectionModel().clearSelection(); // Limpiar la selección de decorator
    }

    // --- Otros (Misc) ---
    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro que desea salir?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar Salida");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                Platform.exit();
            }
        });
    }

    // --- Métodos de utilidad (también Misc) ---
    private void clearProductOutput() { taProductOutput.clear(); }
    private void clearDeviceOutput() { taDeviceOutput.clear(); }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}