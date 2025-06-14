package view;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dialog do zarządzania kolumnami tabeli.
 * Umożliwia ukrywanie i pokazywanie kolumn.
 */
public class ColumnManager extends JDialog {
    private JTable table;
    private List<TableColumn> originalColumns;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> columnList = new JList<>(listModel);

    /**
     * Konstruktor dialogu zarządzania kolumnami.
     *
     * @param parent          ramka nadrzędna
     * @param table           tabela do zarządzania kolumnami
     * @param originalColumns lista oryginalnych kolumn tabeli
     */
    public ColumnManager(JFrame parent, JTable table, List<TableColumn> originalColumns) {
        super(parent, "Zarzadzanie kolumnami", true);
        this.table = table;
        this.originalColumns = new ArrayList<>(originalColumns);
        initUI();
        setDesign();
    }

    /**
     * Inicjalizuje interfejs użytkownika dialogu.
     */
    private void initUI() {
        setSize(300, 400);
        setLocationRelativeTo(getParent());

        updateList();
        columnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton hideButton = new JButton("Ukryj zaznaczone");
        hideButton.addActionListener(e -> hideColumns());

        JButton showButton = new JButton("Pokaz wszystkie");
        showButton.addActionListener(e -> showAll());

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        buttons.add(hideButton);
        buttons.add(showButton);

        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(columnList), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Aktualizuje listę kolumn w dialogu.
     */
    private void updateList() {
        listModel.clear();
        TableColumnModel model = table.getColumnModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
            TableColumn col = model.getColumn(i);
            listModel.addElement(col.getHeaderValue().toString());
        }
    }

    /**
     * Ukrywa zaznaczone kolumny.
     */
    private void hideColumns() {
        int[] selected = columnList.getSelectedIndices();
        Arrays.sort(selected);

        for (int i = selected.length - 1; i >= 0; i--) {
            int index = selected[i];
            table.removeColumn(table.getColumnModel().getColumn(index));
        }

        updateList();
    }

    /**
     * Pokazuje wszystkie kolumny.
     */
    private void showAll() {
        TableColumnModel model = table.getColumnModel();
        while (model.getColumnCount() > 0) {
            model.removeColumn(model.getColumn(0));
        }

        for (TableColumn col : originalColumns) {
            model.addColumn(col);
        }

        updateList();
    }

    /**
     * Ustawia styl dialogu.
     */
    private void setDesign() {
        getContentPane().setBackground(new Color(230, 255, 230));
        columnList.setBackground(new Color(240, 255, 240));
    }
}