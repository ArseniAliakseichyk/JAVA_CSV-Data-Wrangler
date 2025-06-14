package view;

import controller.CSVController;
import javax.swing.*;
import java.awt.*;

/**
 * Główna ramka aplikacji CSV Data Wrangler.
 * Zawiera panel tabeli i panel kontrolny.
 */
public class MainFrame extends JFrame {
    private CSVController controller;
    private TablePanel tablePanel;
    private ControlPanel controlPanel;
    private JLabel statusLabel;

    /**
     * Konstruktor ramki głównej. Inicjalizuje komponenty i ustawia widoczność.
     */
    public MainFrame() {
        super("CSV Data Wrangler");
        controller = new CSVController();
        initComponents();
        applyDesign();
        setVisible(true);
    }

    /**
     * Inicjalizuje komponenty ramki głównej.
     */
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        tablePanel = new TablePanel();
        controlPanel = new ControlPanel(controller);
        tablePanel.setController(controller);
        controller.setTablePanel(tablePanel);
        controller.setMainFrame(this);

        statusLabel = new JLabel("Liczba rekordow: 0");
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);

        setLayout(new BorderLayout(10, 10));
        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Aktualizuje etykietę statusu z bieżącą liczbą rekordów.
     */
    public void updateStatus() {
        int rows = tablePanel.getTable().getModel().getRowCount();
        statusLabel.setText("Liczba rekordow: " + rows);
    }

    /**
     * Zwraca tabelę z panelu tabeli.
     *
     * @return tabela JTable
     */
    public JTable getTable() {
        return tablePanel.getTable();
    }

    /**
     * Zwraca panel tabeli.
     *
     * @return panel tabeli
     */
    public TablePanel getTablePanel() {
        return tablePanel;
    }

    /**
     * Zwraca kontroler CSV.
     *
     * @return kontroler CSV
     */
    public CSVController getController() {
        return controller;
    }

    /**
     * Stosuje styl do ramki głównej.
     */
    private void applyDesign() {
        Color bg = new Color(230, 255, 230);
        getContentPane().setBackground(bg);
        statusLabel.setForeground(new Color(0, 100, 0));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel panel = (JPanel) getContentPane().getComponent(2);
        panel.setBackground(new Color(200, 230, 200));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0, 150, 0)));
    }
}