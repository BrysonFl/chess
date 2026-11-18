package view;

import controller.GameController;
import view.buttons.ButtonBase;
import view.buttons.ResignButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

    private final ButtonBase resignButton;

    /**
     * Construye e inicializa el panel de historial de movimientos,
     * configurando su tamaño, diseño, encabezado y la tabla de jugadas.
     */
    public MovementsPanel() {
        resignButton = new ResignButton("Abandonar");

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
        g2D.setColor(new Color(0xB58863));
        g2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        g2D.dispose();
    }

    /** Conecta las acciones de finalización al controlador de esta partida. */
    public void configureGameActions(GameController controller) {
        JPanel actions = new JPanel(new java.awt.GridLayout(0, 1, 4, 4));
        actions.setOpaque(false);
        gameStatus.setForeground(new Color(0xF0D9B5));
        javax.swing.JButton claim = new javax.swing.JButton("Reclamar tablas");
        claim.addActionListener(event -> controller.claimDraw());

        resignAction(controller);

        actions.add(gameStatus);
        actions.add(claim);
        javax.swing.JButton offer = new javax.swing.JButton("Proponer tablas");
        offer.addActionListener(event -> controller.offerDraw());
        actions.add(offer);
        actions.add(resignButton);
        add(actions, BorderLayout.SOUTH);
    }

    /** Muestra el resultado de la partida en el panel lateral. */
    public void setGameStatus(String status) {
        gameStatus.setText("<html>" + status + "</html>");
    }

    /**
     * Configura la apariencia visual de la tabla de movimientos, incluyendo
     * colores, fuentes, altura de filas, anchos de columnas y alineación.
     */
    private void configureTableStyle() {
        movementsTable.setBackground(new Color(0x363330));
        movementsTable.setForeground(new Color(0xF0D9B5));
        movementsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        movementsTable.setRowHeight(28);
        movementsTable.setShowGrid(false);
        movementsTable.setFocusable(false);
        movementsTable.setRowSelectionAllowed(false);

        JTableHeader header = movementsTable.getTableHeader();
        header.setBackground(new Color(0xB58863));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < movementsTable.getColumnCount(); i++) {
            movementsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        movementsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        movementsTable.getColumnModel().getColumn(0).setMaxWidth(60);
        movementsTable.getColumnModel().getColumn(1).setPreferredWidth(125);
        movementsTable.getColumnModel().getColumn(2).setPreferredWidth(125);
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
            int turnNumber = rowCount + 1;
            tableModel.addRow(new Object[]{turnNumber + ".", movement, null});
        } else {
            tableModel.setValueAt(movement, rowCount - 1, 2);
        }

        scrollToBottom();
    }

    private void resignAction(GameController controller) {
        resignButton.addActionListener(event -> {
            int action = JOptionPane.showConfirmDialog(this, "¿Quieres abandonar la partida?",
                    "Abandonar", JOptionPane.YES_NO_OPTION);

            if (action == JOptionPane.YES_OPTION) controller.resign();
        });
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
