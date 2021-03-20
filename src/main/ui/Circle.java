package ui;

import javafx.scene.shape.Ellipse;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends Component {

    private int diameter;

    public Circle(int radius) {
        diameter = radius * 2;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(new Color(31,170,31));

        g2d.fillOval(0,0,diameter,diameter);
    }
}
