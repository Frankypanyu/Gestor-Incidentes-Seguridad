package ec.edu.taller;

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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/** Interfaz hecha a mano con Java Swing, inspirada en los dos bocetos del ejercicio. */
public class InterfazIncidentes extends JFrame {

    private static final Color NEGRO = Color.BLACK;
    private static final Color BLANCO = Color.WHITE;
    private static final Color ACTIVO = new Color(235, 235, 235);
    private static final Font LETRA = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font TITULO = new Font("SansSerif", Font.BOLD, 16);
    private static final int CAPACIDAD = 10;

    private final GestorIncidentes gestor = new GestorIncidentes(CAPACIDAD);


    private final JTextField campoCodigo = new JTextField();
    private final JComboBox<String> campoTipo = new JComboBox<>(new String[] {
        "MALWARE", "PHISHING", "ACCESO_NO_AUTORIZADO", "FUGA_DATOS", "OTRO"
    });
    private final JTextArea campoDescripcion = new JTextArea(3, 22);
    private final JSpinner campoPrioridad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JLabel avisoRegistro = new JLabel(" ");


    private final JLabel resumen = new JLabel("", SwingConstants.CENTER);
    private final JLabel datoCodigo = new JLabel("CÓDIGO: -");
    private final JLabel datoTipo = new JLabel("TIPO: -");
    private final JLabel datoPrioridad = new JLabel("PRIORIDAD: -");
    private final JLabel datoEstado = new JLabel("ESTADO: -");
    private final JTextArea datoDescripcion = new JTextArea(3, 24);
    private final JTextArea areaLista = new JTextArea();
    private final JLabel avisoGestion = new JLabel(" ");


    private final CardLayout tarjetas = new CardLayout();
    private final JPanel paginas = new JPanel(tarjetas);
    private JButton botonIngreso;
    private JButton botonGestion;

    public InterfazIncidentes() {
        super("Gestor de incidentes de seguridad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 590);
        setMinimumSize(new Dimension(740, 510));
        setLocationRelativeTo(null);
        crearVentana();
        actualizarPantallaGestion();
        cambiarPagina("ingreso");
    }


    private void crearVentana() {
        JPanel principal = new JPanel(new BorderLayout(0, 8));
        principal.setBackground(BLANCO);
        principal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(principal);

        JPanel barra = new JPanel(new GridLayout(1, 2, 4, 0));
        barra.setBackground(BLANCO);
        botonIngreso = boton("INGRESO DE DATOS");
        botonGestion = boton("GESTIONAR INCIDENTES");
        botonIngreso.addActionListener(e -> cambiarPagina("ingreso"));
        botonGestion.addActionListener(e -> cambiarPagina("gestion"));
        barra.add(botonIngreso);
        barra.add(botonGestion);
        barra.setPreferredSize(new Dimension(0, 44));
        principal.add(barra, BorderLayout.NORTH);

        paginas.add(crearPaginaIngreso(), "ingreso");
        paginas.add(crearPaginaGestion(), "gestion");
        principal.add(paginas, BorderLayout.CENTER);
    }

    private void cambiarPagina(String pagina) {
        tarjetas.show(paginas, pagina);
        botonIngreso.setBackground("ingreso".equals(pagina) ? ACTIVO : BLANCO);
        botonGestion.setBackground("gestion".equals(pagina) ? ACTIVO : BLANCO);
        if ("gestion".equals(pagina)) {
            actualizarPantallaGestion();
        }
    }


    private JPanel crearPaginaIngreso() {
        JPanel exterior = panelConBorde(new BorderLayout(10, 10));
        exterior.setBorder(BorderFactory.createCompoundBorder(borde(),
                BorderFactory.createEmptyBorder(16, 24, 18, 24)));

        JLabel cabecera = new JLabel("NUEVO INCIDENTE", SwingConstants.CENTER);
        cabecera.setFont(TITULO);
        cabecera.setBorder(borde());
        cabecera.setPreferredSize(new Dimension(0, 38));
        exterior.add(cabecera, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO);
        GridBagConstraints p = new GridBagConstraints();
        p.insets = new Insets(12, 12, 12, 12);
        p.fill = GridBagConstraints.HORIZONTAL;
        p.anchor = GridBagConstraints.CENTER;

        campoCodigo.setFont(LETRA);
        campoTipo.setFont(LETRA);
        campoDescripcion.setFont(LETRA);
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        campoPrioridad.setFont(LETRA);
        campoCodigo.setPreferredSize(new Dimension(320, 33));
        campoTipo.setPreferredSize(new Dimension(320, 33));
        campoPrioridad.setPreferredSize(new Dimension(170, 33));
        JScrollPane scrollDescripcion = new JScrollPane(campoDescripcion);
        scrollDescripcion.setPreferredSize(new Dimension(320, 92));

        agregarFila(formulario, p, 0, "CÓDIGO", campoCodigo);
        agregarFila(formulario, p, 1, "TIPO", campoTipo);
        agregarFila(formulario, p, 2, "DESCRIPCIÓN", scrollDescripcion);
        agregarFila(formulario, p, 3, "PRIORIDAD", campoPrioridad);

        exterior.add(formulario, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(6, 8));
        inferior.setBackground(BLANCO);
        JPanel filaAccion = new JPanel(new BorderLayout());
        filaAccion.setBackground(BLANCO);
        JLabel estadoInicial = new JLabel("ESTADO INICIAL:   PENDIENTE");
        estadoInicial.setFont(LETRA);
        JButton registrar = boton("REGISTRAR");
        registrar.setPreferredSize(new Dimension(160, 40));
        registrar.addActionListener(e -> registrarIncidente());
        filaAccion.add(estadoInicial, BorderLayout.WEST);
        filaAccion.add(registrar, BorderLayout.EAST);
        inferior.add(filaAccion, BorderLayout.NORTH);
        avisoRegistro.setFont(new Font("SansSerif", Font.PLAIN, 13));
        inferior.add(avisoRegistro, BorderLayout.SOUTH);
        exterior.add(inferior, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(registrar);
        return exterior;
    }

    private static void agregarFila(JPanel panel, GridBagConstraints p, int fila,
                                    String etiqueta, Component componente) {
        p.gridy = fila;
        p.gridx = 0;
        p.weightx = 0.25;
        JLabel texto = new JLabel(etiqueta);
        texto.setFont(LETRA);
        panel.add(texto, p);
        p.gridx = 1;
        p.weightx = 0.75;
        panel.add(componente, p);
    }


    private JPanel crearPaginaGestion() {
        JPanel exterior = panelConBorde(new BorderLayout(12, 12));
        exterior.setBorder(BorderFactory.createCompoundBorder(borde(),
                BorderFactory.createEmptyBorder(15, 22, 14, 22)));

        resumen.setFont(TITULO);
        resumen.setPreferredSize(new Dimension(0, 45));
        exterior.add(resumen, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(10, 12));
        contenido.setBackground(BLANCO);
        JPanel detalles = new JPanel(new GridLayout(1, 2, 22, 0));
        detalles.setBackground(BLANCO);
        JPanel izquierda = new JPanel(new GridLayout(3, 1, 0, 8));
        izquierda.setBackground(BLANCO);
        JPanel derecha = new JPanel(new BorderLayout(0, 8));
        derecha.setBackground(BLANCO);
        for (JLabel dato : new JLabel[] {datoCodigo, datoTipo, datoPrioridad, datoEstado}) {
            dato.setFont(LETRA);
        }
        izquierda.add(datoCodigo);
        izquierda.add(datoTipo);
        izquierda.add(datoPrioridad);
        derecha.add(datoEstado, BorderLayout.NORTH);
        datoDescripcion.setFont(LETRA);
        datoDescripcion.setEditable(false);
        datoDescripcion.setOpaque(false);
        datoDescripcion.setLineWrap(true);
        datoDescripcion.setWrapStyleWord(true);
        derecha.add(datoDescripcion, BorderLayout.CENTER);
        detalles.add(izquierda);
        detalles.add(derecha);
        detalles.setPreferredSize(new Dimension(0, 110));
        contenido.add(detalles, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(10, 12));
        centro.setBackground(BLANCO);
        JPanel filaBotones = new JPanel(new GridLayout(1, 3, 20, 0));
        filaBotones.setBackground(BLANCO);
        JButton siguiente = boton("SIGUIENTE");
        JButton atender = boton("ATENDER");
        JButton listar = boton("LISTAR");
        siguiente.addActionListener(e -> consultarSiguiente());
        atender.addActionListener(e -> atenderSiguiente());
        listar.addActionListener(e -> listarPendientes());
        filaBotones.add(siguiente);
        filaBotones.add(atender);
        filaBotones.add(listar);
        filaBotones.setPreferredSize(new Dimension(0, 42));
        centro.add(filaBotones, BorderLayout.NORTH);

        JPanel cajaLista = panelConBorde(new BorderLayout(0, 8));
        cajaLista.setBorder(BorderFactory.createCompoundBorder(borde(),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JLabel tituloLista = new JLabel("LISTA DE INCIDENTES ORDENADOS POR PRIORIDAD", SwingConstants.CENTER);
        tituloLista.setFont(TITULO);
        JLabel notaLista = new JLabel("MENOR VALOR = MAYOR PRIORIDAD", SwingConstants.CENTER);
        notaLista.setFont(LETRA);
        JPanel encabezadoLista = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezadoLista.setBackground(BLANCO);
        encabezadoLista.add(tituloLista);
        encabezadoLista.add(notaLista);
        cajaLista.add(encabezadoLista, BorderLayout.NORTH);
        areaLista.setEditable(false);
        areaLista.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaLista.setLineWrap(true);
        areaLista.setWrapStyleWord(true);
        areaLista.setText("Pulsa LISTAR para ver los incidentes pendientes.");
        cajaLista.add(new JScrollPane(areaLista), BorderLayout.CENTER);
        centro.add(cajaLista, BorderLayout.CENTER);
        contenido.add(centro, BorderLayout.CENTER);
        exterior.add(contenido, BorderLayout.CENTER);

        avisoGestion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        exterior.add(avisoGestion, BorderLayout.SOUTH);
        return exterior;
    }


    private void registrarIncidente() {
        String codigo = campoCodigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un código.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestor.existeIncidente(codigo)) {
            JOptionPane.showMessageDialog(this, "El código ya existe entre los pendientes.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestor.consultarEspaciosDisponibles() == 0) {
            JOptionPane.showMessageDialog(this, "La cola ya tiene 10 incidentes.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        IncidenteSeguridad nuevo = new IncidenteSeguridad(
                codigo, (String) campoTipo.getSelectedItem(),
                campoDescripcion.getText().trim(), (Integer) campoPrioridad.getValue());
        if (gestor.registrarIncidente(nuevo)) {
            avisoRegistro.setText("Incidente " + codigo + " registrado correctamente.");
            campoCodigo.setText("");
            campoDescripcion.setText("");
            actualizarPantallaGestion();
        } else {
            avisoRegistro.setText("No se pudo registrar el incidente.");
        }
    }


    private void consultarSiguiente() {
        mostrarDatos(gestor.consultarSiguienteIncidente()); // peek(): solo consulta
        avisoGestion.setText("Consulta realizada (no elimina el incidente).");
    }

    private void atenderSiguiente() {
        IncidenteSeguridad atendido = gestor.atenderSiguienteIncidente(); // poll(): lo elimina
        if (atendido == null) {
            avisoGestion.setText("No hay incidentes pendientes para atender.");
        } else {
            avisoGestion.setText("Incidente " + atendido.getCodigo() + " atendido correctamente.");
        }
        actualizarPantallaGestion();
        listarPendientes();
    }


    private void listarPendientes() {
        List<IncidenteSeguridad> lista = gestor.listarIncidentes();
        if (lista.isEmpty()) {
            areaLista.setText("No hay incidentes pendientes.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        int puesto = 1;
        for (IncidenteSeguridad incidente : lista) {
            texto.append(puesto++).append(". ")
                    .append(incidente.getCodigo()).append("  |  ")
                    .append(incidente.getTipo()).append("  |  Prioridad ")
                    .append(incidente.getPrioridad()).append("  |  ")
                    .append(incidente.getEstado()).append("\n");
        }
        areaLista.setText(texto.toString());
        areaLista.setCaretPosition(0);
    }

    private void actualizarPantallaGestion() {
        resumen.setText("PENDIENTES: " + gestor.contarIncidentesPendientes()
                + "       DISPONIBLES: " + gestor.consultarEspaciosDisponibles()
                + "       CAPACIDAD: " + CAPACIDAD);
        mostrarDatos(gestor.consultarSiguienteIncidente());
    }

    private void mostrarDatos(IncidenteSeguridad incidente) {
        if (incidente == null) {
            datoCodigo.setText("CÓDIGO: -");
            datoTipo.setText("TIPO: -");
            datoPrioridad.setText("PRIORIDAD: -");
            datoEstado.setText("ESTADO: -");
            datoDescripcion.setText("DESCRIPCIÓN: Sin incidentes pendientes.");
        } else {
            datoCodigo.setText("CÓDIGO: " + incidente.getCodigo());
            datoTipo.setText("TIPO: " + incidente.getTipo());
            datoPrioridad.setText("PRIORIDAD: " + incidente.getPrioridad());
            datoEstado.setText("ESTADO: " + incidente.getEstado());
            datoDescripcion.setText("DESCRIPCIÓN: " + incidente.getDescripcion());
        }
    }

    private static JPanel panelConBorde(java.awt.LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(BLANCO);
        panel.setBorder(borde());
        return panel;
    }

    private static Border borde() {
        return BorderFactory.createLineBorder(NEGRO, 2);
    }

    private static JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setFont(LETRA);
        b.setBackground(BLANCO);
        b.setForeground(NEGRO);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(borde());
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazIncidentes().setVisible(true));
    }
}
