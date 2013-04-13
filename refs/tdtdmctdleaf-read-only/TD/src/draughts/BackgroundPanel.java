package draughts;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Definiuje główny panel, w którym zawarte są pozostałe komponenty. 
 */
public class BackgroundPanel extends JPanel {

    private BufferedImage img;

    /**
     * Wczytuje obraz tła i zapisuje do pola img. 
     */
    public BackgroundPanel() {
        super();
        try {
            img = ImageIO.read(getClass().getResource("/draughts/draughtsBackground.jpg"));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     *Rysuje obraz img jako tło panelu.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, 751, 551, null);
    }
}
