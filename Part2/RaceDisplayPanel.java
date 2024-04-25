import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// calss to make a panel to display the race
public class RaceDisplayPanel extends JPanel {
    private List<Horse> horses;
    private int raceLength;
    private String imagePath;

    public RaceDisplayPanel(List<Horse> horses, int raceLength, String imagePath) {
        this.horses = horses;
        this.raceLength = raceLength;
        this.imagePath=imagePath;
        setOpaque(false);
        setPreferredSize(new Dimension(600, 400)); // panel size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw background image
        BufferedImage backgroundImage = loadImage(imagePath);

        // Calculate dimensions for the race display
        int horseWidth = 40; 
        int horseHeight = 40; 
        int raceWidth = getWidth() - 100; // Width of the race track (excluding labels)
        int raceHeight = getHeight() - 100; 
        int startX = 50; // X-coordinate of the left side of the race track
        int startY = 50; // Y-coordinate of the top side of the race track

        // Draw the race track
        g2d.drawImage(backgroundImage, startX, startY, raceWidth, raceHeight, this);

        // Draw the horses with images
        for (int i = 0; i < horses.size(); i++) {
            Horse horse = horses.get(i);
            int x = (int) (50 + (double) horse.getDistanceTravelled() / raceLength * (raceWidth - horseWidth));
            int y = (int) (raceHeight * (i + 1) / (double) (horses.size() + 1))-30; // Vertical position of the horse

            x = Math.min(x, raceWidth - horseWidth);

            // Draw horse image
            BufferedImage horseImage = horse.getHorseImage();
            if (horseImage != null) {
                g2d.drawImage(horseImage, startX + x, startY + y, horseWidth, horseHeight, this);
            } else {
                // Draw default square if image not found
                g2d.setColor(Color.BLACK);
                g2d.fillRect(startX + x, startY + y, horseWidth, horseHeight);
            }
        }
    }


    // Method to load an image
    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
