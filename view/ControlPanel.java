package view;

import controller.CSVController;
import util.CSVUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel kontrolny aplikacji.
 * Zawiera przyciski i pola do zarządzania danymi CSV.
 */
public class ControlPanel extends JPanel {
    private CSVController controller;
    private JTextField filterField;
    private JComboBox<String> columnCombo, filterTypeCombo, delimiterCombo;
    private JCheckBox headerCheckBox;
    private JButton newButton;
    private static File lastUsedDirectory = new File(System.getProperty("user.home"));

    /**
     * Konstruktor panelu kontrolnego.
     *
     * @param controller kontroler CSV do obsługi operacji
     */
    public ControlPanel(CSVController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        newButton = new JButton("Nowy");
        newButton.addActionListener(this::newAction);
        add(newButton, gbc);

        gbc.gridy = 1;
        gbc.gridx = 1;
        JButton loadButton = new JButton("Wczytaj CSV");
        loadButton.addActionListener(this::loadAction);
        add(loadButton, gbc);

        gbc.gridx = 2;
        JButton saveButton = new JButton("Zapisz CSV");
        saveButton.addActionListener(this::saveAction);
        add(saveButton, gbc);

        gbc.gridx = 3;
        headerCheckBox = new JCheckBox("Naglowki", true);
        add(headerCheckBox, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Separator:"), gbc);
        gbc.gridx = 1;
        delimiterCombo = new JComboBox<>(new String[]{",", ";", "|", "\\t"});
        add(delimiterCombo, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Filtr:"), gbc);
        gbc.gridx = 1;
        filterField = new JTextField(15);
        add(filterField, gbc);
        gbc.gridx = 2;
        add(new JLabel("Kolumna:"), gbc);
        gbc.gridx = 3;
        columnCombo = new JComboBox<>();
        add(columnCombo, gbc);
        gbc.gridx = 4;
        add(new JLabel("Typ:"), gbc);
        gbc.gridx = 5;
        filterTypeCombo = new JComboBox<>(new String[]{"Zawiera", "Rozpoczyna sie", "Konczy sie", "Rowna sie"});
        add(filterTypeCombo, gbc);
        gbc.gridx = 6;
        JButton filterButton = new JButton("Filtruj");
        filterButton.addActionListener(this::filterAction);
        add(filterButton, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        JButton addButton = new JButton("Dodaj wiersz");
        addButton.addActionListener(e -> showAddRowDialog());
        add(addButton, gbc);
        gbc.gridx = 1;
        JButton deleteButton = new JButton("Usun zaznaczony");
        deleteButton.addActionListener(this::deleteAction);
        add(deleteButton, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        JButton addColumnButton = new JButton("Dodaj kolumne");
        addColumnButton.addActionListener(e -> addColumn());
        add(addColumnButton, gbc);
        gbc.gridx = 1;
        JButton removeColumnButton = new JButton("Usun kolumne");
        removeColumnButton.addActionListener(e -> removeColumn());
        add(removeColumnButton, gbc);
        gbc.gridx = 2;
        JButton manageButton = new JButton("Zarzadzaj kolumnami");
        manageButton.addActionListener(e -> openColumnManager());
        add(manageButton, gbc);
        gbc.gridx = 5;
        JButton editHeadersButton = new JButton("Edytuj naglowki");
        editHeadersButton.addActionListener(e -> editHeaders());
        add(editHeadersButton, gbc);

        applyDesign();
    }

    /**
     * Obsługuje akcję tworzenia nowego pliku CSV.
     *
     * @param e zdarzenie akcji
     */
    private void newAction(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Podaj liczbe kolumn:", "3");
        if (input != null) {
            try {
                int c = Integer.parseInt(input);
                if (c > 0) {
                    controller.createNewFile(c);
                    updateColumns();
                } else {
                    JOptionPane.showMessageDialog(this, "Liczba kolumn musi byc wieksza od 0");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidlowa liczba kolumn");
            }
        }
    }

    /**
     * Dodaje nową kolumnę do danych.
     */
    private void addColumn() {
        if (controller.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Najpierw wczytaj dane!");
            return;
        }
        String name = JOptionPane.showInputDialog(this, "Podaj nazwę nowej kolumny:");
        if (name != null && !name.trim().isEmpty()) {
            controller.addColumn(name.trim());
            updateColumns();
        }
    }

    /**
     * Usuwa wybraną kolumnę z danych.
     */
    private void removeColumn() {
        if (controller.getColumnCount() == 0) {
            JOptionPane.showMessageDialog(this, "Brak kolumn do usuniecia");
            return;
        }
        int i = columnCombo.getSelectedIndex();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz kolumne do usuniecia");
            return;
        }
        int o = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunac kolumne '" + columnCombo.getSelectedItem() + "'?", "Potwierdzenie usuniecia", JOptionPane.YES_NO_OPTION);
        if (o == JOptionPane.YES_OPTION) {
            controller.removeColumn(i);
            updateColumns();
        }
    }

    /**
     * Edytuje nagłówki kolumn.
     */
    private void editHeaders() {
        if (controller.getColumnCount() == 0) {
            JOptionPane.showMessageDialog(this, "Brak kolumn do edycji");
            return;
        }

        List<String> headers = controller.getHeaders();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        List<JTextField> fields = new ArrayList<>();

        for (int i = 0; i < controller.getColumnCount(); i++) {
            String text = i < headers.size() ? headers.get(i) : "Kolumna " + (i + 1);
            panel.add(new JLabel("Kolumna " + (i + 1) + ":"));
            JTextField field = new JTextField(text, 20);
            panel.add(field);
            fields.add(field);
        }

        int r = JOptionPane.showConfirmDialog(this, panel, "Edytuj naglowki", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            List<String> newHeaders = new ArrayList<>();
            for (JTextField field : fields) {
                newHeaders.add(field.getText());
            }
            controller.updateHeaders(newHeaders);
            updateColumns();
        }
    }

    /**
     * Obsługuje akcję filtrowania danych.
     *
     * @param e zdarzenie akcji
     */
    private void filterAction(ActionEvent e) {
        String word = filterField.getText();
        int col = columnCombo.getSelectedIndex();
        String type = (String) filterTypeCombo.getSelectedItem();
        controller.filterTable(word, col, type);
    }

    /**
     * Obsługuje akcję usuwania zaznaczonego wiersza.
     *
     * @param e zdarzenie akcji
     */
    private void deleteAction(ActionEvent e) {
        int row = ((MainFrame) SwingUtilities.getWindowAncestor(this)).getTablePanel().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Nie zaznaczono wiersza");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąc zaznaczony wiersz?", "Potwierdzenie usuniecia", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.deleteRow(row);
        }
    }

    /**
     * Otwiera dialog zarządzania kolumnami.
     */
    private void openColumnManager() {
        if (controller.getRowCount() > 0) {
            JTable t = ((MainFrame) SwingUtilities.getWindowAncestor(this)).getTable();
            TablePanel p = ((MainFrame) SwingUtilities.getWindowAncestor(this)).getTablePanel();
            new ColumnManager((JFrame) SwingUtilities.getWindowAncestor(this), t, p.getOriginalColumns()).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Brak danych do zarzadzania");
        }
    }

    /**
     * Aktualizuje listę kolumn w combo box.
     */
    public void updateColumns() {
        columnCombo.removeAllItems();
        List<String> headers = controller.getHeaders();
        int c = controller.getColumnCount();

        if (!headers.isEmpty()) {
            for (String h : headers) columnCombo.addItem(h);
        } else {
            for (int i = 0; i < c; i++) columnCombo.addItem("Kolumna " + (i + 1));
        }

        if (c > 0) columnCombo.setSelectedIndex(c - 1);
    }

    /**
     * Pokazuje dialog dodawania nowego wiersza.
     */
    private void showAddRowDialog() {
        if (controller.getColumnCount() == 0) {
            JOptionPane.showMessageDialog(this, "Najpierw wczytaj dane!");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField[] fields = new JTextField[controller.getColumnCount()];
        List<String> headers = controller.getHeaders();

        for (int i = 0; i < fields.length; i++) {
            String name = i < headers.size() ? headers.get(i) : "Kolumna " + (i + 1);
            panel.add(new JLabel(name + ":"));
            fields[i] = new JTextField(20);
            panel.add(fields[i]);
        }

        int r = JOptionPane.showConfirmDialog(this, panel, "Dodaj nowy wiersz", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            String[] row = new String[fields.length];
            for (int i = 0; i < row.length; i++) row[i] = fields[i].getText();
            controller.addRow(row);
        }
    }

    /**
     * Obsługuje akcję wczytywania pliku CSV.
     *
     * @param e zdarzenie akcji
     */
    private void loadAction(ActionEvent e) {
        JFileChooser fc = new JFileChooser(lastUsedDirectory);
        fc.setFileFilter(new FileNameExtensionFilter("Pliki CSV", "csv"));
        int o = fc.showOpenDialog(this);

        if (o == JFileChooser.APPROVE_OPTION) {
            lastUsedDirectory = fc.getSelectedFile().getParentFile();
            String d = (String) delimiterCombo.getSelectedItem();
            if ("\\t".equals(d)) d = "\t";

            try {
                String det = CSVUtils.detectDelimiter(fc.getSelectedFile());
                if (!d.equals(det)) {
                    int ch = JOptionPane.showConfirmDialog(this, "Wykryty separator: '" + det + "'. Uzyc wykrytego separatora?", "Wykryty separator", JOptionPane.YES_NO_OPTION);
                    if (ch == JOptionPane.YES_OPTION) {
                        d = det;
                        delimiterCombo.setSelectedItem(det.equals("\t") ? "\\t" : det);
                    }
                }
            } catch (Exception ex) {}

            boolean h = headerCheckBox != null && headerCheckBox.isSelected();
            controller.loadCSV(fc.getSelectedFile(), d, h);
            updateColumns();
        }
    }

    /**
     * Obsługuje akcję zapisywania pliku CSV.
     *
     * @param e zdarzenie akcji
     */
    private void saveAction(ActionEvent e) {
        JFileChooser fc = new JFileChooser(lastUsedDirectory);
        fc.setFileFilter(new FileNameExtensionFilter("Pliki CSV", "csv"));
        int o = fc.showSaveDialog(this);

        if (o == JFileChooser.APPROVE_OPTION) {
            lastUsedDirectory = fc.getSelectedFile().getParentFile();
            File f = fc.getSelectedFile();
            String p = f.getAbsolutePath();
            if (!p.toLowerCase().endsWith(".csv")) f = new File(p + ".csv");

            String d = (String) delimiterCombo.getSelectedItem();
            if ("\\t".equals(d)) d = "\t";

            boolean h = headerCheckBox != null && headerCheckBox.isSelected();
            controller.saveCSV(f, d, h);
        }
    }

    /**
     * Tworzy pasek menu dla aplikacji.
     *
     * @return pasek menu
     */
    public JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Plik");
        JMenuItem open = new JMenuItem("Otworz");
        JMenuItem save = new JMenuItem("Zapisz");
        JMenuItem col = new JMenuItem("Zarzadzaj kolumnami");
        open.addActionListener(this::loadAction);
        save.addActionListener(this::saveAction);
        col.addActionListener(e -> openColumnManager());
        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(col);
        bar.add(file);
        return bar;
    }

    /**
     * Stosuje styl do panelu kontrolnego.
     */
    private void applyDesign() {
        Color bg = new Color(220, 255, 220);
        setBackground(bg);
        for (Component c : getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(new Color(180, 230, 180));
                c.setForeground(Color.BLACK);
            } else if (c instanceof JLabel) {
                c.setForeground(new Color(0, 100, 0));
            } else if (c instanceof JComboBox || c instanceof JTextField) {
                c.setBackground(Color.WHITE);
            } else if (c instanceof JCheckBox) {
                c.setBackground(bg);
            }
        }
    }
}