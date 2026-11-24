package view;

import controller.GameController;
import view.buttons.ClaimDrawButton;
import view.buttons.DrawButton;
import view.buttons.ResignButton;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Representa el panel lateral de la interfaz gráfica encargado de mostrar el historial
 * de movimientos y los controles de estado de la partida.
 * <p>
 * Esta clase extiende {@link JPanel} y presenta una tabla desplazable ({@link JTable})
 * que organiza las jugadas por número de turno, movimiento de las piezas blancas y
 * movimiento de las piezas negras. Además, integra una etiqueta de estado de la partida
 * y los botones de acción para abandonar ({@link ResignButton}), proponer tablas
 * ({@link DrawButton}) o reclamar tablas ({@link ClaimDrawButton}).
 * </p>
 *
 * <p>El historial alterna blancas y negras según el orden de inserción; no interpreta
 * ni valida la notación recibida. La vinculación de controles se realiza durante
 * la construcción del tablero y añade nuevos oyentes en cada llamada.</p>
 *
 * @author brysonfl
 * @see Board
 * @see GameController
 * @see ResignButton
 * @see DrawButton
 * @see ClaimDrawButton
 */
public class MovementsPanel extends JPanel {

    /** Etiqueta que muestra el estado actual o el resultado final de la partida. */
    private final JLabel gameStatus = new JLabel("Partida en curso");

    /** Modelo de datos no editable que respalda la tabla de historial de jugadas. */
    private final DefaultTableModel tableModel;

    /** Tabla visual donde se listan los turnos y movimientos de blancas y negras. */
    private final JTable movementsTable;

    /** Botón para que el jugador en turno abandone la partida. */
    private final ResignButton resignButton;

    /** Botón para proponer tablas por mutuo acuerdo al oponente. */
    private final DrawButton drawButton;

    /** Botón para reclamar tablas cuando se cumple una regla aplicable. */
    private final ClaimDrawButton claimDrawButton;

    /**
     * Construye e inicializa el panel lateral de historial de movimientos.
     * <p>
     * Instancia los botones de acción de la partida, configura las dimensiones preferidas,
     * el diseño ({@link BorderLayout}), los colores y márgenes del panel, añade el título
     * superior e inicializa la tabla de jugadas dentro de un {@link JScrollPane}.
     * </p>
     */
    public MovementsPanel() {
        resignButton = new ResignButton("Abandonar");
        drawButton = new DrawButton("Proponer Tablas");
        claimDrawButton = new ClaimDrawButton("Reclamar tablas");

        setPreferredSize(new Dimension(360, 1000));
        setLayout(new BorderLayout());
        setBackground(new Color(0x2B2926));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 16, 12));

        JLabel titleLabel = new JLabel("Historial de Movimientos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0xF0D9B5));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(16, 10, 16, 10));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"N.º", "Blancas", "Negras"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        movementsTable = new JTable(tableModel);
        configureTableStyle();

        JScrollPane scrollPane = new JScrollPane(movementsTable);
        scrollPane.getViewport().setBackground(new Color(0x363330));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Dibuja el fondo y el contorno con esquinas redondeadas del panel de historial.
     * <p>
     * Utiliza suavizado de bordes (<i>antialiasing</i>) para pintar un rectángulo
     * redondeado con el color de fondo del componente, manteniendo la tabla y los
     * controles dentro de los márgenes interiores.
     * </p>
     *
     * @param g el contexto gráfico {@link Graphics} utilizado para renderizar el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(getBackground());
        g2D.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        g2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        g2D.dispose();
    }

    /**
     * Vincula los botones de finalización de partida al controlador principal y los
     * añade en la sección inferior del panel.
     * <p>
     * Configura las acciones de {@link ResignButton}, {@link DrawButton} y
     * {@link ClaimDrawButton} con el {@link GameController} indicado y agrupa dichos
     * botones junto a la etiqueta de estado de la partida en la región {@link BorderLayout#SOUTH}.
     * </p>
     *
     * @param controller el controlador de la partida ({@link GameController}) que gestionará
     *                   los eventos de abandono y tablas.
     */
    public void configureGameActions(GameController controller) {
        JPanel actions = new JPanel(new GridLayout(0, 1, 4, 4));
        actions.setOpaque(false);
        gameStatus.setForeground(new Color(0xF0D9B5));

        resignButton.configureAction(controller);
        drawButton.configureAction(controller);
        claimDrawButton.configureAction(controller);

        actions.add(gameStatus);

        actions.add(claimDrawButton);
        actions.add(drawButton);
        actions.add(resignButton);
        add(actions, BorderLayout.SOUTH);
    }

    /**
     * Actualiza el texto descriptivo del estado o resultado de la partida en el panel lateral.
     *
     * @param status la cadena de texto con el nuevo estado de la partida
     *               (por ejemplo, {@code "Partida en curso"} o el motivo de finalización).
     */
    public void setGameStatus(String status) {
        gameStatus.setText(status);
    }

    /**
     * Configura la apariencia visual y el comportamiento de interacción de la tabla de movimientos.
     * <p>
     * Define la tipografía, la altura de las filas y desactiva el foco y la selección de celdas,
     * además de bloquear el reordenamiento y redimensionamiento de las columnas en el encabezado
     * ({@link JTableHeader}).
     * </p>
     */
    private void configureTableStyle() {
        movementsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        movementsTable.setRowHeight(28);
        movementsTable.setFocusable(false);
        movementsTable.setRowSelectionAllowed(false);

        JTableHeader header = movementsTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
    }

    /**
     * Registra un nuevo movimiento en notación de texto dentro de la tabla de historial.
     * <p>
     * Si la tabla está vacía o la última fila ya contiene la jugada de las piezas negras,
     * inserta una nueva fila incrementando el número de turno y colocando la jugada en la
     * columna de las blancas. En caso contrario, completa la fila actual asignando el
     * movimiento a la columna de las negras. Finalmente, desplaza la vista hasta el final.
     * </p>
     *
     * @param movement la cadena que representa el movimiento realizado en notación algebraica
     *                 (por ejemplo, {@code "e4"}, {@code "Nf3"} o {@code "O-O"}).
     */
    public void addMovement(String movement) {
        int rowCount = tableModel.getRowCount();

        if (rowCount == 0 || tableModel.getValueAt(rowCount - 1, 2) != null) {
            rowCount++;
            tableModel.addRow(new Object[]{rowCount + ".", movement, null});
        } else {
            rowCount--;
            tableModel.setValueAt(movement, rowCount, 2);
        }

        scrollToBottom();
    }

    /**
     * Desplaza automáticamente la vista de la tabla hacia la última fila para mantener
     * visible el movimiento más reciente.
     */
    private void scrollToBottom() {
        int lastRow = movementsTable.getRowCount() - 1;

         if (lastRow >= 0) {
            movementsTable.scrollRectToVisible(movementsTable.getCellRect(lastRow, 0, true));
        }
    }

}
