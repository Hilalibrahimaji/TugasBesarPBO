import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

import controller.PeminjamanController;
import view.PeminjamanFrame;

public class EduCorApp {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatLightFlatIJTheme());
        } catch (Exception ex) {
            System.err.println("Gagal mengatur tema FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            PeminjamanFrame frame = new PeminjamanFrame();
            new PeminjamanController(frame);
            frame.setVisible(true);
        });
    }
}
