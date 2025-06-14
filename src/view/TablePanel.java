package view;

import controller.CSVController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel zawierający tabelę do wyświetlania danych CSV.
 */
public class TablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<TableColumn> originalColumns = new ArrayList<>();
    private CSVController controller;

    /**
     * Ustawia kontroler dla panelu tabeli.
     *
     * @param controller kontroler CSV
     */
    public void setController(CSVController controller) {
        this.controller = controller;
    }

    /**
     * Konstruktor panelu tabeli. Inicjalizuje komponenty.
     */
    public TablePanel() {
        initComponents();
    }

    /**
     * Inicjalizuje komponenty panelu.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        initTableModel();
        initTable();
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    public void stopEditing() {
        if (table.isEditing()) {
            TableCellEditor editor = table.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
    }

    /**
     * Inicjalizuje model tabeli.
     */
    private void initTableModel() {
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return true;
            }
        };
    }

    /**
     * Inicjalizuje tabelę.
     */
    private void initTable() {
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        applyTableDesign();

        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (row >= 0 && col >= 0) {
                String value = (String) tableModel.getValueAt(row, col);
                if (controller != null) {
                    controller.updateCell(row, col, value);
                }
            }
        });
    }

    /**
     * Stosuje styl do tabeli.
     */
    private void applyTableDesign() {
        table.setGridColor(new Color(200, 230, 200));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(255, 232, 105));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    /**
     * Aktualizuje tabelę danymi i nagłówkami.
     *
     * @param data    dane do wyświetlenia
     * @param headers nagłówki kolumn
     */
    public void updateTable(List<String[]> data, List<String> headers) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        int max = headers.isEmpty() ?
                data.stream().mapToInt(r -> r.length).max().orElse(0) :
                headers.size();

        if (!headers.isEmpty()) {
            for (String h : headers) {
                tableModel.addColumn(h);
            }
        } else {
            for (int i = 0; i < max; i++) {
                tableModel.addColumn("Kolumna " + (i + 1));
            }
        }

        for (String[] row : data) {
            Object[] newRow = new Object[max];
            System.arraycopy(row, 0, newRow, 0, Math.min(row.length, max));
            tableModel.addRow(newRow);
        }

        storeOriginalColumns();
    }

    /**
     * Przechowuje oryginalne kolumny tabeli.
     */
    private void storeOriginalColumns() {
        originalColumns.clear();
        int count = table.getColumnModel().getColumnCount();
        for (int i = 0; i < count; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            originalColumns.add(col);
        }
    }

    /**
     * Zwraca indeks zaznaczonego wiersza.
     *
     * @return indeks zaznaczonego wiersza lub -1, jeśli nic nie zaznaczono
     */
    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    /**
     * Zwraca tabelę.
     *
     * @return tabela JTable
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Zwraca listę oryginalnych kolumn.
     *
     * @return lista kolumn
     */
    public List<TableColumn> getOriginalColumns() {
        return new ArrayList<>(originalColumns);
    }
}