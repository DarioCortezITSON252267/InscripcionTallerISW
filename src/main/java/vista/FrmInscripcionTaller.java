package vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import control.ControlInscripcion;
import modelo.Alumno;
import modelo.Inscripcion;
import modelo.Taller;

/**
 * Vista (la "V" de MVC) del caso de uso "Inscribirse en Taller de Semana de
 * ISW". Solo muestra información y reenvía las acciones del usuario al
 * control; no contiene reglas de negocio.
 *
 * Zonas de la pantalla:
 * <ul>
 * <li>Izquierda: lista de talleres disponibles.</li>
 * <li>Derecha: detalle del taller seleccionado.</li>
 * <li>Debajo del detalle: captura del ID y datos del alumno, que se
 * reemplazan por el ticket al confirmar la inscripción.</li>
 * </ul>
 */
public class FrmInscripcionTaller extends JFrame {

    private static final Color AZUL_ITSON = new Color(0, 75, 135);
    private static final Color FONDO = new Color(242, 245, 249);
    private static final Color BORDE = new Color(214, 220, 229);
    private static final Color TEXTO_SECUNDARIO = new Color(96, 105, 117);
    private static final DateTimeFormatter FORMATO_FECHA_CORTA =
            DateTimeFormatter.ofPattern("EEE d 'de' MMM", Locale.forLanguageTag("es-MX"));

    private static final String DETALLE_VACIO = "detalleVacio";
    private static final String DETALLE_TALLER = "detalleTaller";
    private static final String ZONA_VACIA = "zonaVacia";
    private static final String ZONA_CAPTURA = "zonaCaptura";
    private static final String ZONA_TICKET = "zonaTicket";

    private ControlInscripcion control;

    // Zona izquierda: lista de talleres
    private final DefaultListModel<Taller> modeloLista = new DefaultListModel<>();
    private final JList<Taller> lstTalleres = new JList<>(modeloLista);

    // Zona derecha: detalle del taller
    private final CardLayout tarjetasDetalle = new CardLayout();
    private final JPanel pnlDetalle = new JPanel(tarjetasDetalle);
    private final JLabel lblNombreTaller = new JLabel();
    private final JLabel lblInstructor = new JLabel();
    private final JLabel lblFecha = new JLabel();
    private final JLabel lblHorario = new JLabel();
    private final JLabel lblLugar = new JLabel();
    private final JLabel lblCupo = new JLabel();
    private final JTextArea txtDescripcion = new JTextArea(2, 20);
    private final JButton btnInscribirse = new JButton("Inscribirse");

    // Zona inferior: datos del alumno y ticket de inscripción
    private final JLabel lblTituloZonaAlumno = new JLabel("Inscripción");
    private final CardLayout tarjetasZonaAlumno = new CardLayout();
    private final JPanel pnlZonaAlumno = new JPanel(tarjetasZonaAlumno);
    private final JTextField txtIdAlumno = new JTextField(14);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JPanel pnlDatosAlumno = new JPanel(new GridBagLayout());
    private final JLabel lblIdAlumno = new JLabel();
    private final JLabel lblNombreAlumno = new JLabel();
    private final JLabel lblSemestre = new JLabel();
    private final JLabel lblPrograma = new JLabel();
    private final JLabel lblCorreo = new JLabel();
    private final JButton btnCancelar = new JButton("Cancelar");
    private final JButton btnConfirmar = new JButton("Confirmar inscripción");
    private final JLabel lblTicket = new JLabel();
    private final JButton btnNuevaInscripcion = new JButton("Nueva inscripción");

    public FrmInscripcionTaller() {
        super("Inscripción a Talleres - Semana de ISW");
        initComponents();
    }

    public void setControl(ControlInscripcion control) {
        this.control = control;
    }

    // ------------------------------------------------------------------
    // Métodos que invoca el control para actualizar la pantalla
    // ------------------------------------------------------------------

    /**
     * Paso a: muestra la lista de talleres disponibles.
     */
    public void mostrarTalleres(List<Taller> talleres) {
        modeloLista.clear();
        for (Taller taller : talleres) {
            modeloLista.addElement(taller);
        }
    }

    /**
     * Paso b: muestra a la derecha los detalles del taller.
     */
    public void mostrarDetalleTaller(Taller taller) {
        lblNombreTaller.setText("<html>" + taller.getNombre() + "</html>");
        lblInstructor.setText(taller.getInstructor());
        lblFecha.setText(capitalizar(taller.getFechaTexto()));
        lblHorario.setText(taller.getHorario());
        lblLugar.setText(taller.getLugar());
        mostrarCupo(taller);
        txtDescripcion.setText(taller.getDescripcion());
        btnInscribirse.setEnabled(taller.hayCupo());
        tarjetasDetalle.show(pnlDetalle, DETALLE_TALLER);
        limpiarZonaAlumno();
    }

    /**
     * Paso c: solicita el ID del alumno en la zona inferior.
     */
    public void solicitarIdAlumno() {
        lblTituloZonaAlumno.setText("Datos del alumno");
        txtIdAlumno.setText("");
        pnlDatosAlumno.setVisible(false);
        btnConfirmar.setEnabled(false);
        tarjetasZonaAlumno.show(pnlZonaAlumno, ZONA_CAPTURA);
        txtIdAlumno.requestFocusInWindow();
    }

    /**
     * Paso c: muestra los datos completos del alumno debajo del detalle.
     */
    public void mostrarDatosAlumno(Alumno alumno) {
        lblIdAlumno.setText(alumno.getId());
        lblNombreAlumno.setText(alumno.getNombre());
        lblSemestre.setText(alumno.getSemestre() + "° semestre");
        lblPrograma.setText(alumno.getPrograma());
        lblCorreo.setText(alumno.getCorreo());
        pnlDatosAlumno.setVisible(true);
        btnConfirmar.setEnabled(true);
        getRootPane().setDefaultButton(btnConfirmar);
    }

    /**
     * Paso d: muestra el ticket en la zona donde estaban los datos del alumno.
     */
    public void mostrarTicket(Inscripcion inscripcion) {
        lblTituloZonaAlumno.setText("Ticket de inscripción");
        lblTicket.setText(generarTicketHtml(inscripcion));
        mostrarCupo(inscripcion.getTaller());
        btnInscribirse.setEnabled(inscripcion.getTaller().hayCupo());
        lstTalleres.repaint();
        tarjetasZonaAlumno.show(pnlZonaAlumno, ZONA_TICKET);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void limpiarZonaAlumno() {
        lblTituloZonaAlumno.setText("Inscripción");
        txtIdAlumno.setText("");
        pnlDatosAlumno.setVisible(false);
        btnConfirmar.setEnabled(false);
        getRootPane().setDefaultButton(null);
        tarjetasZonaAlumno.show(pnlZonaAlumno, ZONA_VACIA);
    }

    // ------------------------------------------------------------------
    // Eventos de la interfaz: se delegan al control
    // ------------------------------------------------------------------

    private void formWindowOpened() {
        control.cargarTalleres();
    }

    private void lstTalleresValueChanged(ListSelectionEvent evt) {
        Taller taller = lstTalleres.getSelectedValue();
        if (!evt.getValueIsAdjusting() && taller != null) {
            control.seleccionarTaller(taller.getClave());
        }
    }

    private void btnInscribirseActionPerformed() {
        control.solicitarInscripcion();
    }

    private void btnBuscarActionPerformed() {
        pnlDatosAlumno.setVisible(false);
        btnConfirmar.setEnabled(false);
        control.consultarAlumno(txtIdAlumno.getText().trim());
    }

    private void btnConfirmarActionPerformed() {
        control.confirmarInscripcion();
    }

    private void btnCancelarActionPerformed() {
        control.reiniciarInscripcion();
    }

    private void btnNuevaInscripcionActionPerformed() {
        control.reiniciarInscripcion();
    }

    // ------------------------------------------------------------------
    // Construcción de la interfaz
    // ------------------------------------------------------------------

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setSize(1100, 760);
        setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new BorderLayout(14, 14));
        contenido.setBackground(FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        contenido.add(crearPanelTalleres(), BorderLayout.WEST);
        JPanel derecha = new JPanel(new GridLayout(2, 1, 0, 14));
        derecha.setOpaque(false);
        derecha.add(crearPanelDetalle());
        derecha.add(crearZonaAlumno());
        contenido.add(derecha, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(crearEncabezado(), BorderLayout.NORTH);
        getContentPane().add(contenido, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                formWindowOpened();
            }
        });
        lstTalleres.addListSelectionListener(this::lstTalleresValueChanged);
        btnInscribirse.addActionListener(e -> btnInscribirseActionPerformed());
        btnBuscar.addActionListener(e -> btnBuscarActionPerformed());
        txtIdAlumno.addActionListener(e -> btnBuscarActionPerformed());
        btnConfirmar.addActionListener(e -> btnConfirmarActionPerformed());
        btnCancelar.addActionListener(e -> btnCancelarActionPerformed());
        btnNuevaInscripcion.addActionListener(e -> btnNuevaInscripcionActionPerformed());
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(AZUL_ITSON);
        encabezado.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel("Semana de Ingeniería en Software 2026");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 21f));
        JLabel subtitulo = new JLabel("Inscripción a talleres  ·  Del 9 al 13 de noviembre");
        subtitulo.setForeground(new Color(205, 222, 240));
        subtitulo.setFont(subtitulo.getFont().deriveFont(14f));
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        return encabezado;
    }

    private JPanel crearPanelTalleres() {
        lstTalleres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstTalleres.setSelectionBackground(new Color(220, 234, 247));
        lstTalleres.setSelectionForeground(Color.BLACK);
        lstTalleres.setCellRenderer(new TallerRenderer());
        JScrollPane scroll = new JScrollPane(lstTalleres);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        JPanel tarjeta = crearTarjeta(new JLabel("Talleres disponibles"), scroll);
        tarjeta.setPreferredSize(new Dimension(340, 0));
        return tarjeta;
    }

    private JPanel crearPanelDetalle() {
        JLabel vacio = crearMensajeGuia("Selecciona un taller de la lista para ver sus detalles.");

        lblNombreTaller.setFont(lblNombreTaller.getFont().deriveFont(Font.BOLD, 17f));
        lblNombreTaller.setForeground(AZUL_ITSON);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFocusable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(lblInstructor.getFont());
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder());

        JPanel datos = new JPanel(new GridBagLayout());
        datos.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 10, 0);
        datos.add(lblNombreTaller, c);
        agregarFila(datos, 1, "Instructor:", lblInstructor);
        agregarFila(datos, 2, "Fecha:", lblFecha);
        agregarFila(datos, 3, "Horario:", lblHorario);
        agregarFila(datos, 4, "Lugar:", lblLugar);
        agregarFila(datos, 5, "Cupo:", lblCupo);
        agregarFila(datos, 6, "Descripción:", txtDescripcion);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acciones.setOpaque(false);
        acciones.add(btnInscribirse);

        JPanel detalle = new JPanel(new BorderLayout());
        detalle.setOpaque(false);
        detalle.add(datos, BorderLayout.NORTH);
        detalle.add(acciones, BorderLayout.SOUTH);

        pnlDetalle.setOpaque(false);
        pnlDetalle.add(vacio, DETALLE_VACIO);
        pnlDetalle.add(detalle, DETALLE_TALLER);
        return crearTarjeta(new JLabel("Detalle del taller"), pnlDetalle);
    }

    private JPanel crearZonaAlumno() {
        JLabel vacio = crearMensajeGuia("Presiona \"Inscribirse\" para capturar el ID del alumno.");

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        busqueda.setOpaque(false);
        busqueda.add(new JLabel("ID del alumno:"));
        busqueda.add(txtIdAlumno);
        busqueda.add(btnBuscar);

        pnlDatosAlumno.setOpaque(false);
        pnlDatosAlumno.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        agregarFila(pnlDatosAlumno, 0, "ID:", lblIdAlumno);
        agregarFila(pnlDatosAlumno, 1, "Nombre:", lblNombreAlumno);
        agregarFila(pnlDatosAlumno, 2, "Semestre:", lblSemestre);
        agregarFila(pnlDatosAlumno, 3, "Programa:", lblPrograma);
        agregarFila(pnlDatosAlumno, 4, "Correo:", lblCorreo);
        pnlDatosAlumno.setVisible(false);

        JPanel accionesCaptura = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesCaptura.setOpaque(false);
        btnConfirmar.setEnabled(false);
        accionesCaptura.add(btnCancelar);
        accionesCaptura.add(btnConfirmar);

        JPanel busquedaYDatos = new JPanel(new BorderLayout());
        busquedaYDatos.setOpaque(false);
        busquedaYDatos.add(busqueda, BorderLayout.NORTH);
        busquedaYDatos.add(pnlDatosAlumno, BorderLayout.CENTER);

        JPanel captura = new JPanel(new BorderLayout());
        captura.setOpaque(false);
        captura.add(busquedaYDatos, BorderLayout.NORTH);
        captura.add(accionesCaptura, BorderLayout.SOUTH);

        lblTicket.setVerticalAlignment(SwingConstants.TOP);
        lblTicket.setOpaque(true);
        lblTicket.setBackground(new Color(255, 253, 244));
        lblTicket.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createDashedBorder(TEXTO_SECUNDARIO, 4, 3),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JPanel accionesTicket = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        accionesTicket.setOpaque(false);
        accionesTicket.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        accionesTicket.add(btnNuevaInscripcion);
        JPanel ticket = new JPanel(new BorderLayout());
        ticket.setOpaque(false);
        ticket.add(lblTicket, BorderLayout.CENTER);
        ticket.add(accionesTicket, BorderLayout.SOUTH);

        pnlZonaAlumno.setOpaque(false);
        pnlZonaAlumno.add(vacio, ZONA_VACIA);
        pnlZonaAlumno.add(captura, ZONA_CAPTURA);
        pnlZonaAlumno.add(ticket, ZONA_TICKET);
        return crearTarjeta(lblTituloZonaAlumno, pnlZonaAlumno);
    }

    private JPanel crearTarjeta(JLabel titulo, JComponent contenido) {
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
        titulo.setForeground(AZUL_ITSON);
        titulo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                BorderFactory.createEmptyBorder(0, 0, 8, 0)));
        JPanel tarjeta = new JPanel(new BorderLayout(0, 12));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        tarjeta.add(titulo, BorderLayout.NORTH);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private JLabel crearMensajeGuia(String texto) {
        JLabel mensaje = new JLabel(texto, SwingConstants.CENTER);
        mensaje.setForeground(TEXTO_SECUNDARIO);
        return mensaje;
    }

    private void agregarFila(JPanel panel, int fila, String etiqueta, JComponent valor) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(3, 0, 3, 12);
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setForeground(TEXTO_SECUNDARIO);
        lblEtiqueta.setPreferredSize(new Dimension(90, lblEtiqueta.getPreferredSize().height));
        c.gridx = 0;
        panel.add(lblEtiqueta, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 0, 3, 0);
        panel.add(valor, c);
    }

    private void mostrarCupo(Taller taller) {
        if (taller.hayCupo()) {
            lblCupo.setText(taller.getCupoDisponible() + " de " + taller.getCupoMaximo() + " lugares disponibles");
            lblCupo.setForeground(new Color(30, 123, 52));
        } else {
            lblCupo.setText("Sin cupo (" + taller.getCupoMaximo() + " de " + taller.getCupoMaximo() + " ocupados)");
            lblCupo.setForeground(new Color(179, 38, 30));
        }
    }

    private String generarTicketHtml(Inscripcion inscripcion) {
        Alumno alumno = inscripcion.getAlumno();
        Taller taller = inscripcion.getTaller();
        return "<html><div style='text-align:center'>"
                + "<b style='font-size:13px'>TICKET DE INSCRIPCIÓN</b><br>"
                + "Semana de Ingeniería en Software 2026</div><br>"
                + "<table cellpadding='2' cellspacing='0'>"
                + fila("Folio:", "<b>" + inscripcion.getFolio() + "</b>", "Registro:", inscripcion.getFechaRegistroTexto())
                + fila("Alumno:", alumno.getNombre(), "Taller:", taller.getNombre())
                + fila("ID:", alumno.getId(), "Instructor:", taller.getInstructor())
                + fila("Semestre:", alumno.getSemestre() + "°", "Fecha:", capitalizar(taller.getFechaTexto()))
                + fila("Programa:", alumno.getPrograma(), "Horario:", taller.getHorario())
                + fila("Correo:", alumno.getCorreo(), "Lugar:", taller.getLugar())
                + "</table><br><div style='text-align:center; color:#5f6975'>"
                + "Presenta este ticket (impreso o en tu celular) el día del taller.</div></html>";
    }

    private String fila(String etiqueta1, String valor1, String etiqueta2, String valor2) {
        return "<tr><td valign='top'><font color='#606975'>" + etiqueta1 + "</font></td>"
                + "<td valign='top' width='215'>" + valor1 + "</td>"
                + "<td valign='top'><font color='#606975'>" + etiqueta2 + "</font></td>"
                + "<td valign='top' width='215'>" + valor2 + "</td></tr>";
    }

    private static String capitalizar(String texto) {
        return texto.isEmpty() ? texto : Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    /**
     * Dibuja cada taller de la lista con su nombre, fecha y cupo disponible.
     */
    private static class TallerRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, false);
            Taller taller = (Taller) value;
            String cupo = taller.hayCupo()
                    ? "<font color='#1e7b34'>" + taller.getCupoDisponible()
                    + (taller.getCupoDisponible() == 1 ? " lugar disponible" : " lugares disponibles") + "</font>"
                    : "<font color='#b3261e'>Sin cupo</font>";
            setText("<html><div style='width:240px'><b>" + taller.getNombre() + "</b><br>"
                    + "<font color='#606975'>" + capitalizar(taller.getFecha().format(FORMATO_FECHA_CORTA))
                    + "  ·  " + taller.getHorario() + "</font><br>" + cupo + "</div></html>");
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            return this;
        }
    }
}
