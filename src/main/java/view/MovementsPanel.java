package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import view.pieces.Bishop;
import view.pieces.King;
import view.pieces.Knight;
import view.pieces.Piece;
import view.pieces.Queen;
import view.pieces.Rook;

/**
 * Panel lateral encargado de mostrar y registrar el historial de movimientos
 * realizados durante la partida de ajedrez, organizados por número de turno,
 * jugadas de las piezas blancas y jugadas de las piezas negras.
 *
 * @author brysonfl
 */
public class MovementsPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable movementsTable;

    /**
     * Construye e inicializa el panel de historial de movimientos,
     * configurando su tamaño, diseño, encabezado y la tabla de jugadas.
     */
    public MovementsPanel() {
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

    /**
     * Registra un movimiento a partir de la casilla de origen y la casilla de destino,
     * generando automáticamente la notación algebraica correspondiente.
     *
     * @param fromSquare Casilla de origen antes de efectuar el movimiento.
     * @param toSquare   Casilla de destino a la que se desplaza la pieza.
     */
    public void addMovement(Square fromSquare, Square toSquare) {
        if (fromSquare == null || toSquare == null) {
            return;
        }

        Piece movedPiece = fromSquare.getPiece();
        boolean isCapture = toSquare.getPiece() != null;
        String piecePrefix = getPieceSymbol(movedPiece);
        String fromCoord = toAlgebraic(fromSquare.getRow(), fromSquare.getCol());
        String toCoord = toAlgebraic(toSquare.getRow(), toSquare.getCol());
        String separator = isCapture ? "x" : "-";

        String notation = piecePrefix + fromCoord + separator + toCoord;
        addMovement(notation);
    }

    /**
     * Limpia todo el historial de movimientos registrado en la tabla.
     */
    public void clearHistory() {
        tableModel.setRowCount(0);
    }

    /**
     * Convierte las coordenadas de fila y columna de la matriz del tablero
     * a notación algebraica estándar de ajedrez (columnas 'a'-'h', filas '1'-'8').
     *
     * @param row Fila del tablero (0 a 7).
     * @param col Columna del tablero (0 a 7).
     * @return Coordenada en notación algebraica (ej. "e4").
     */
    private String toAlgebraic(int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    /**
     * Obtiene la letra representativa de la pieza para la notación del movimiento.
     *
     * @param piece La pieza que realiza el movimiento.
     * @return El símbolo de la pieza ("K", "Q", "R", "B", "N") o cadena vacía para peones.
     */
    private String getPieceSymbol(Piece piece) {
        if (piece instanceof King) {
            return "K";
        } else if (piece instanceof Queen) {
            return "Q";
        } else if (piece instanceof Rook) {
            return "R";
        } else if (piece instanceof Bishop) {
            return "B";
        } else if (piece instanceof Knight) {
            return "N";
        }
        return "";
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
