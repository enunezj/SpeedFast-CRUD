package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;
import model.TipoPedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ventana principal del sistema SpeedFast.
 * Permite gestionar repartidores, pedidos y entregas
 * mediante una interfaz Swing conectada a los DAO.
 */
public class VentanaPrincipal extends JFrame {

    private static final int MAX_CARACTERES = 100;

    private final RepartidorDAO repartidorDAO;
    private final PedidoDAO pedidoDAO;
    private final EntregaDAO entregaDAO;

    private JTextField txtNombreRepartidor;
    private JTable tablaRepartidores;
    private DefaultTableModel modeloRepartidores;

    private JTextField txtDireccionPedido;
    private JComboBox<TipoPedido> comboTipoPedido;
    private JComboBox<EstadoPedido> comboEstadoPedido;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;

    private JComboBox<Pedido> comboPedidoEntrega;
    private JComboBox<Repartidor> comboRepartidorEntrega;
    private JTextField txtFechaEntrega;
    private JTextField txtHoraEntrega;
    private JTable tablaEntregas;
    private DefaultTableModel modeloEntregas;

    public VentanaPrincipal() {

        repartidorDAO = new RepartidorDAO();
        pedidoDAO = new PedidoDAO();
        entregaDAO = new EntregaDAO();

        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {

        setTitle("SpeedFast CRUD");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void crearInterfaz() {

        JTabbedPane pestanas =
                new JTabbedPane();

        pestanas.addTab(
                "Repartidores",
                crearPanelRepartidores()
        );

        pestanas.addTab(
                "Pedidos",
                crearPanelPedidos()
        );

        pestanas.addTab(
                "Entregas",
                crearPanelEntregas()
        );

        add(pestanas);
    }

    private JPanel crearPanelRepartidores() {

        JPanel panel =
                crearPanelBase();

        JPanel formulario =
                new JPanel(new FlowLayout());

        txtNombreRepartidor =
                new JTextField(20);

        JButton btnAgregar =
                new JButton("Agregar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        formulario.add(
                new JLabel("Nombre:")
        );

        formulario.add(
                txtNombreRepartidor
        );

        formulario.add(
                btnAgregar
        );

        formulario.add(
                btnEditar
        );

        formulario.add(
                btnEliminar
        );

        modeloRepartidores =
                crearModeloTabla(
                        "ID",
                        "Nombre"
                );

        tablaRepartidores =
                new JTable(modeloRepartidores);

        panel.add(
                formulario,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(tablaRepartidores),
                BorderLayout.CENTER
        );

        btnAgregar.addActionListener(
                _ -> agregarRepartidor()
        );

        btnEditar.addActionListener(
                _ -> editarRepartidor()
        );

        btnEliminar.addActionListener(
                _ -> eliminarRepartidor()
        );

        tablaRepartidores
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {

                                cargarRepartidorSeleccionado();
                            }
                        }
                );

        cargarRepartidores();

        return panel;
    }

    private void agregarRepartidor() {

        Repartidor repartidor =
                obtenerRepartidorFormulario(null);

        if (repartidor == null) {

            return;
        }

        if (repartidorDAO.create(repartidor)) {

            mostrarExito(
                    "Repartidor registrado correctamente."
            );

            limpiarFormularioRepartidor();
            cargarRepartidores();

            return;
        }

        mostrarError(
                "No se pudo registrar el repartidor."
        );
    }

    private void editarRepartidor() {

        Integer id =
                obtenerIdSeleccionado(
                        tablaRepartidores,
                        modeloRepartidores,
                        "Debe seleccionar un repartidor."
                );

        if (id == null) {

            return;
        }

        Repartidor repartidor =
                obtenerRepartidorFormulario(id);

        if (repartidor == null) {

            return;
        }

        if (repartidorDAO.update(repartidor)) {

            mostrarExito(
                    "Repartidor actualizado correctamente."
            );

            limpiarFormularioRepartidor();
            cargarRepartidores();

            return;
        }

        mostrarError(
                "No se pudo actualizar el repartidor."
        );
    }

    private void eliminarRepartidor() {

        Integer id =
                obtenerIdSeleccionado(
                        tablaRepartidores,
                        modeloRepartidores,
                        "Debe seleccionar un repartidor."
                );

        if (id == null) {

            return;
        }

        if (eliminacionCancelada(
                "Desea eliminar este repartidor?"
        )) {

            return;
        }

        if (repartidorDAO.delete(id)) {

            mostrarExito(
                    "Repartidor eliminado correctamente."
            );

            limpiarFormularioRepartidor();
            cargarRepartidores();

            return;
        }

        mostrarError(
                "No se pudo eliminar el repartidor.\n"
                        + "Puede tener entregas asociadas."
        );
    }

    private Repartidor obtenerRepartidorFormulario(
            Integer id) {

        String nombre =
                txtNombreRepartidor
                        .getText()
                        .trim();

        String error =
                validarTexto(
                        nombre,
                        "El nombre"
                );

        if (error != null) {

            mostrarAdvertencia(error);

            return null;
        }

        if (id == null) {

            return new Repartidor(nombre);
        }

        return new Repartidor(
                id,
                nombre
        );
    }

    private void cargarRepartidores() {

        modeloRepartidores.setRowCount(0);

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {

            modeloRepartidores.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }

        cargarCombosEntrega();
    }

    private void cargarRepartidorSeleccionado() {

        int fila =
                tablaRepartidores
                        .getSelectedRow();

        if (fila == -1) {

            return;
        }

        txtNombreRepartidor.setText(
                modeloRepartidores
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString()
        );
    }

    private void limpiarFormularioRepartidor() {

        txtNombreRepartidor.setText("");

        tablaRepartidores.clearSelection();
    }

    private JPanel crearPanelPedidos() {

        JPanel panel =
                crearPanelBase();

        JPanel formulario =
                new JPanel(new FlowLayout());

        txtDireccionPedido =
                new JTextField(15);

        comboTipoPedido =
                new JComboBox<>(
                        TipoPedido.values()
                );

        comboEstadoPedido =
                new JComboBox<>(
                        EstadoPedido.values()
                );

        JButton btnAgregar =
                new JButton("Agregar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        formulario.add(
                new JLabel("Direccion:")
        );

        formulario.add(
                txtDireccionPedido
        );

        formulario.add(
                new JLabel("Tipo:")
        );

        formulario.add(
                comboTipoPedido
        );

        formulario.add(
                new JLabel("Estado:")
        );

        formulario.add(
                comboEstadoPedido
        );

        formulario.add(
                btnAgregar
        );

        formulario.add(
                btnEditar
        );

        formulario.add(
                btnEliminar
        );

        modeloPedidos =
                crearModeloTabla(
                        "ID",
                        "Direccion",
                        "Tipo",
                        "Estado"
                );

        tablaPedidos =
                new JTable(modeloPedidos);

        panel.add(
                formulario,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(tablaPedidos),
                BorderLayout.CENTER
        );

        btnAgregar.addActionListener(
                _ -> agregarPedido()
        );

        btnEditar.addActionListener(
                _ -> editarPedido()
        );

        btnEliminar.addActionListener(
                _ -> eliminarPedido()
        );

        tablaPedidos
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {

                                cargarPedidoSeleccionado();
                            }
                        }
                );

        cargarPedidos();

        return panel;
    }

    private void agregarPedido() {

        Pedido pedido =
                obtenerPedidoFormulario(null);

        if (pedido == null) {

            return;
        }

        if (pedidoDAO.create(pedido)) {

            mostrarExito(
                    "Pedido registrado correctamente."
            );

            limpiarFormularioPedido();
            cargarPedidos();

            return;
        }

        mostrarError(
                "No se pudo registrar el pedido."
        );
    }

    private void editarPedido() {

        Integer id =
                obtenerIdSeleccionado(
                        tablaPedidos,
                        modeloPedidos,
                        "Debe seleccionar un pedido."
                );

        if (id == null) {

            return;
        }

        Pedido pedido =
                obtenerPedidoFormulario(id);

        if (pedido == null) {

            return;
        }

        if (pedidoDAO.update(pedido)) {

            mostrarExito(
                    "Pedido actualizado correctamente."
            );

            limpiarFormularioPedido();
            cargarPedidos();

            return;
        }

        mostrarError(
                "No se pudo actualizar el pedido."
        );
    }

    private void eliminarPedido() {

        Integer id =
                obtenerIdSeleccionado(
                        tablaPedidos,
                        modeloPedidos,
                        "Debe seleccionar un pedido."
                );

        if (id == null) {

            return;
        }

        if (eliminacionCancelada(
                "Desea eliminar este pedido?"
        )) {

            return;
        }

        if (pedidoDAO.delete(id)) {

            mostrarExito(
                    "Pedido eliminado correctamente."
            );

            limpiarFormularioPedido();
            cargarPedidos();

            return;
        }

        mostrarError(
                "No se pudo eliminar el pedido.\n"
                        + "Puede tener entregas asociadas."
        );
    }

    private Pedido obtenerPedidoFormulario(
            Integer id) {

        String direccion =
                txtDireccionPedido
                        .getText()
                        .trim();

        String error =
                validarTexto(
                        direccion,
                        "La direccion"
                );

        if (error != null) {

            mostrarAdvertencia(error);

            return null;
        }

        TipoPedido tipo =
                (TipoPedido)
                        comboTipoPedido
                                .getSelectedItem();

        EstadoPedido estado =
                (EstadoPedido)
                        comboEstadoPedido
                                .getSelectedItem();

        if (tipo == null || estado == null) {

            mostrarAdvertencia(
                    "Debe seleccionar tipo y estado."
            );

            return null;
        }

        if (id == null) {

            return new Pedido(
                    direccion,
                    tipo,
                    estado
            );
        }

        return new Pedido(
                id,
                direccion,
                tipo,
                estado
        );
    }

    private void cargarPedidos() {

        modeloPedidos.setRowCount(0);

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            modeloPedidos.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccion(),
                            pedido.getTipo(),
                            pedido.getEstado()
                    }
            );
        }

        cargarCombosEntrega();
    }

    private void cargarPedidoSeleccionado() {

        int fila =
                tablaPedidos
                        .getSelectedRow();

        if (fila == -1) {

            return;
        }

        txtDireccionPedido.setText(
                modeloPedidos
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString()
        );

        comboTipoPedido.setSelectedItem(
                TipoPedido.valueOf(
                        modeloPedidos
                                .getValueAt(
                                        fila,
                                        2
                                )
                                .toString()
                )
        );

        comboEstadoPedido.setSelectedItem(
                EstadoPedido.valueOf(
                        modeloPedidos
                                .getValueAt(
                                        fila,
                                        3
                                )
                                .toString()
                )
        );
    }

    private void limpiarFormularioPedido() {

        txtDireccionPedido.setText("");

        comboTipoPedido.setSelectedIndex(0);

        comboEstadoPedido.setSelectedIndex(0);

        tablaPedidos.clearSelection();
    }

    private JPanel crearPanelEntregas() {

        JPanel panel =
                crearPanelBase();

        JPanel formulario =
                new JPanel(new FlowLayout());

        comboPedidoEntrega =
                new JComboBox<>();

        comboRepartidorEntrega =
                new JComboBox<>();

        txtFechaEntrega =
                new JTextField(10);

        txtHoraEntrega =
                new JTextField(8);

        JButton btnAgregar =
                new JButton("Agregar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        formulario.add(
                new JLabel("Pedido:")
        );

        formulario.add(
                comboPedidoEntrega
        );

        formulario.add(
                new JLabel("Repartidor:")
        );

        formulario.add(
                comboRepartidorEntrega
        );

        formulario.add(
                new JLabel("Fecha:")
        );

        formulario.add(
                txtFechaEntrega
        );

        formulario.add(
                new JLabel("Hora:")
        );

        formulario.add(
                txtHoraEntrega
        );

        formulario.add(
                btnAgregar
        );

        formulario.add(
                btnEditar
        );

        formulario.add(
                btnEliminar
        );

        modeloEntregas =
                crearModeloTabla(
                        "ID",
                        "Pedido",
                        "Repartidor",
                        "Fecha",
                        "Hora"
                );

        tablaEntregas =
                new JTable(modeloEntregas);

        panel.add(
                formulario,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(tablaEntregas),
                BorderLayout.CENTER
        );

        btnAgregar.addActionListener(
                _ -> agregarEntrega()
        );

        btnEditar.addActionListener(
                _ -> editarEntrega()
        );

        btnEliminar.addActionListener(
                _ -> eliminarEntrega()
        );

        tablaEntregas
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {

                                cargarEntregaSeleccionada();
                            }
                        }
                );

        establecerFechaHoraActual();

        cargarCombosEntrega();
        cargarEntregas();

        return panel;
    }

    private void agregarEntrega() {

        Entrega entrega =
                obtenerEntregaFormulario(null);

        if (entrega == null) {

            return;
        }

        if (entregaDAO.create(entrega)) {

            mostrarExito(
                    "Entrega registrada correctamente."
            );

            limpiarFormularioEntrega();
            cargarEntregas();

            return;
        }

        mostrarError(
                "No se pudo registrar la entrega."
        );
    }

    private void editarEntrega() {

        Integer id =
                obtenerIdSeleccionado(
                        tablaEntregas,
                        modeloEntregas,
                        "Debe seleccionar una entrega."
                );

        if (id == null) {

            return;
        }

        Entrega entrega =
                obtenerEntregaFormulario(id);

        if (entrega == null) {

            return;
        }

        if (entregaDAO.update(entrega)) {

            mostrarExito(
                    "Entrega actualizada correctamente."
            );

            limpiarFormularioEntrega();
            cargarEntregas();

            return;
        }

        mostrarError(
                "No se pudo actualizar la entrega."
        );
    }

    private void eliminarEntrega() {

        Integer id =
                obtenerIdSeleccionado(
                        tablaEntregas,
                        modeloEntregas,
                        "Debe seleccionar una entrega."
                );

        if (id == null) {

            return;
        }

        if (eliminacionCancelada(
                "Desea eliminar esta entrega?"
        )) {

            return;
        }

        if (entregaDAO.delete(id)) {

            mostrarExito(
                    "Entrega eliminada correctamente."
            );

            limpiarFormularioEntrega();
            cargarEntregas();

            return;
        }

        mostrarError(
                "No se pudo eliminar la entrega."
        );
    }

    private Entrega obtenerEntregaFormulario(
            Integer id) {

        Pedido pedido =
                obtenerPedidoSeleccionado();

        Repartidor repartidor =
                obtenerRepartidorSeleccionado();

        if (pedido == null) {

            mostrarAdvertencia(
                    "Debe seleccionar un pedido."
            );

            return null;
        }

        if (repartidor == null) {

            mostrarAdvertencia(
                    "Debe seleccionar un repartidor."
            );

            return null;
        }

        LocalDate fecha =
                obtenerFechaValida();

        if (fecha == null) {

            return null;
        }

        LocalTime hora =
                obtenerHoraValida();

        if (hora == null) {

            return null;
        }

        if (id == null) {

            return new Entrega(
                    pedido.getId(),
                    repartidor.getId(),
                    fecha,
                    hora
            );
        }

        return new Entrega(
                id,
                pedido.getId(),
                repartidor.getId(),
                fecha,
                hora
        );
    }

    private void cargarCombosEntrega() {

        if (comboPedidoEntrega == null
                || comboRepartidorEntrega == null) {

            return;
        }

        comboPedidoEntrega.removeAllItems();
        comboRepartidorEntrega.removeAllItems();

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            comboPedidoEntrega.addItem(
                    pedido
            );
        }

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {

            comboRepartidorEntrega.addItem(
                    repartidor
            );
        }
    }

    private void cargarEntregas() {

        modeloEntregas.setRowCount(0);

        List<Entrega> entregas =
                entregaDAO.readAll();

        for (Entrega entrega : entregas) {

            modeloEntregas.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getIdPedido(),
                            entrega.getIdRepartidor(),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    private void cargarEntregaSeleccionada() {

        int fila =
                tablaEntregas
                        .getSelectedRow();

        if (fila == -1) {

            return;
        }

        int idPedido =
                (int) modeloEntregas
                        .getValueAt(
                                fila,
                                1
                        );

        int idRepartidor =
                (int) modeloEntregas
                        .getValueAt(
                                fila,
                                2
                        );

        seleccionarPedidoPorId(
                idPedido
        );

        seleccionarRepartidorPorId(
                idRepartidor
        );

        txtFechaEntrega.setText(
                modeloEntregas
                        .getValueAt(
                                fila,
                                3
                        )
                        .toString()
        );

        txtHoraEntrega.setText(
                modeloEntregas
                        .getValueAt(
                                fila,
                                4
                        )
                        .toString()
        );
    }

    private void seleccionarPedidoPorId(
            int idPedido) {

        for (int i = 0;
             i < comboPedidoEntrega.getItemCount();
             i++) {

            Pedido pedido =
                    comboPedidoEntrega
                            .getItemAt(i);

            if (pedido.getId()
                    == idPedido) {

                comboPedidoEntrega
                        .setSelectedIndex(i);

                return;
            }
        }
    }

    private void seleccionarRepartidorPorId(
            int idRepartidor) {

        for (int i = 0;
             i < comboRepartidorEntrega.getItemCount();
             i++) {

            Repartidor repartidor =
                    comboRepartidorEntrega
                            .getItemAt(i);

            if (repartidor.getId()
                    == idRepartidor) {

                comboRepartidorEntrega
                        .setSelectedIndex(i);

                return;
            }
        }
    }

    private Pedido obtenerPedidoSeleccionado() {

        return (Pedido)
                comboPedidoEntrega
                        .getSelectedItem();
    }

    private Repartidor obtenerRepartidorSeleccionado() {

        return (Repartidor)
                comboRepartidorEntrega
                        .getSelectedItem();
    }

    private LocalDate obtenerFechaValida() {

        String texto =
                txtFechaEntrega
                        .getText()
                        .trim();

        if (texto.isEmpty()) {

            mostrarAdvertencia(
                    "La fecha es obligatoria."
            );

            return null;
        }

        try {

            return LocalDate.parse(texto);

        } catch (DateTimeParseException ex) {

            mostrarAdvertencia(
                    "Formato de fecha invalido.\n"
                            + "Use AAAA-MM-DD."
            );

            return null;
        }
    }

    private LocalTime obtenerHoraValida() {

        String texto =
                txtHoraEntrega
                        .getText()
                        .trim();

        if (texto.isEmpty()) {

            mostrarAdvertencia(
                    "La hora es obligatoria."
            );

            return null;
        }

        try {

            return LocalTime.parse(texto);

        } catch (DateTimeParseException ex) {

            mostrarAdvertencia(
                    "Formato de hora invalido.\n"
                            + "Use HH:MM o HH:MM:SS."
            );

            return null;
        }
    }

    private void limpiarFormularioEntrega() {

        establecerFechaHoraActual();

        tablaEntregas.clearSelection();

        if (comboPedidoEntrega.getItemCount()
                > 0) {

            comboPedidoEntrega
                    .setSelectedIndex(0);
        }

        if (comboRepartidorEntrega.getItemCount()
                > 0) {

            comboRepartidorEntrega
                    .setSelectedIndex(0);
        }
    }

    private void establecerFechaHoraActual() {

        txtFechaEntrega.setText(
                LocalDate.now().toString()
        );

        txtHoraEntrega.setText(
                LocalTime.now()
                        .withNano(0)
                        .toString()
        );
    }

    private JPanel crearPanelBase() {

        return new JPanel(
                new BorderLayout(10, 10)
        );
    }

    private DefaultTableModel crearModeloTabla(
            String... columnas) {

        return new DefaultTableModel(
                columnas,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };
    }

    private String validarTexto(
            String valor,
            String nombreCampo) {

        if (valor.isEmpty()) {

            return nombreCampo
                    + " es obligatorio.";
        }

        if (valor.length()
                > MAX_CARACTERES) {

            return nombreCampo
                    + " no puede superar los "
                    + MAX_CARACTERES
                    + " caracteres.";
        }

        return null;
    }

    private Integer obtenerIdSeleccionado(
            JTable tabla,
            DefaultTableModel modelo,
            String mensaje) {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            mostrarAdvertencia(mensaje);

            return null;
        }

        return (int) modelo
                .getValueAt(
                        fila,
                        0
                );
    }

    private boolean eliminacionCancelada(
            String mensaje) {

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        mensaje,
                        "Confirmar eliminacion",
                        JOptionPane.YES_NO_OPTION
                );

        return respuesta
                != JOptionPane.YES_OPTION;
    }

    private void mostrarExito(
            String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Exito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarAdvertencia(
            String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validacion",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(
            String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}