import view.MainFrame;

/**
 * Główna klasa aplikacji.
 */
public class Main {
    /**
     * Główny punkt wejścia aplikacji.
     *
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.getController().setMainFrame(frame);
        });
    }
}