package psyriccio.laftst;

import javax.swing.SwingUtilities;

public class LookAndFeelTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
        });
    }
}
