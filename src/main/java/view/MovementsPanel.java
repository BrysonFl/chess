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
 * Panel lateral encargado de mostrar y registrar el historial de movimientos
 * realizados durante la partida de ajedrez, organizados por número de turno,
 * jugadas de las piezas blancas y jugadas de las piezas negras.
 *
 * @author brysonfl
 */
public class MovementsPanel extends JPanel {

    private final JLabel gameStatus = new JLabel("Partida en curso");
    private final DefaultTableModel tableModel;
    private final JTable movementsTable;

    private final ResignButton resignButton;
    private final DrawButton drawButton;
    private final ClaimDrawButton claimDrawButton;

    /**
     * Construye e inicializa el panel de historial de movimientos,
     * configurando su tamaño, diseño, encabezado y la tabla de jugadas.
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
     * Dibuja el fondo y el borde redondeado del panel de historial.
     * El margen interior mantiene la tabla dentro de las esquinas del panel.
     *
     * @param g El contexto gráfico utilizado para dibujar.
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

    /** Conecta las acciones de finalización al controlador de esta partida. */
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

    /** Muestra el resultado de la partida en el panel lateral. */
    public void setGameStatus(String status) {
        gameStatus.setText(status);
    }

    /**
     * Configura la apariencia visual de la tabla de movimientos, incluyendo
     * colores, fuentes, altura de filas, anchos de columnas y alineación.
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
     * Registra un movimiento en formato de texto dentro de la tabla de historial.
     * Si el último turno registrado tiene pendiente la jugada de las negras, se
     * añade en dicha columna; de lo contrario, se crea una nueva fila para el
     * siguiente turno con la jugada de las blancas.
     *
     * @param movement Cadena que representa el movimiento (ej. "e2 - e4" o "Cf3").
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
     * Desplaza automáticamente la vista de la tabla hacia el último movimiento registrado.
     */
    private void scrollToBottom() {
        int lastRow = movementsTable.getRowCount() - 1;

         if (lastRow >= 0) {
            movementsTable.scrollRectToVisible(movementsTable.getCellRect(lastRow, 0, true));
        }
    }

}
