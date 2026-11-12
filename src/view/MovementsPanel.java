package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Historial de la partida, agrupado por turnos de blancas y negras. */
public class MovementsPanel extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"N.º", "Blancas", "Negras"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public MovementsPanel() {
        setLayout(new BorderLayout(0, 12));
        setPreferredSize(new Dimension(360, 600));
        setMinimumSize(new Dimension(300, 200));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(new JLabel("Jugadas realizadas", SwingConstants.CENTER), BorderLayout.NORTH);

        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, renderer);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /** Añade una jugada confirmada y mantiene visible el último turno. */
    public void addMovement(boolean white, String movement) {
        if (white || model.getRowCount() == 0
                || !model.getValueAt(model.getRowCount() - 1, 2).equals("")) {
            model.addRow(new Object[]{model.getRowCount() + 1, "", ""});
        }
        int row = model.getRowCount() - 1;
        model.setValueAt(movement, row, white ? 1 : 2);
        table.scrollRectToVisible(table.getCellRect(row, white ? 1 : 2, true));
    }
}
