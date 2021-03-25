package ui.gui;

import java.awt.*;

// Represents a bar shape
public class Bar extends Component {

    private Color color;
    private int width;
    private int height;

    // EFFECTS: Sets initial parameters
    public Bar(Color color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    // MODIFIES: this
    // EFFECTS: When component is painted, draws a bar shape with given size and colour
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(color);

        g2d.fillOval(0,0,height,height);

        g2d.fillRect(height / 2,0,width - height,height);

        g2d.fillOval(width - height, 0, height, height);

    }

}
