import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solar extends JPanel {
    static final int width = 950;
    static final int height = 900;
    // 1.0 is top-down, 0.3 is very flat. 0.45 is the "sweet spot" for 3D depth.
    static final double TILT = 0.45; 

    static class Plannet {
        String name;
        int r, baseSize;
        double speed, angle;
        Color color;
        int currentX, currentY, currentSize;

        public Plannet(String name, int r, int size, double speed, Color color) {
            this.name = name;
            this.r = r;
            this.baseSize = size;
            this.speed = speed;
            this.color = color;
            this.angle = Math.random() * Math.PI * 2;
        }

        void tick(Point center) {
            angle += speed;
            // Calculate 3D projected coordinates
            currentX = center.x + (int) (r * Math.cos(angle));
            currentY = center.y + (int) (r * Math.sin(angle) * TILT);
            
            // Perspective scaling: Closer to bottom = larger
            double scale = 0.8 + ((Math.sin(angle) + 1) / 2) * 0.5; 
            currentSize = (int) (baseSize * scale);
        }
    }

    private final List<Plannet> plannets = new ArrayList<>(List.of(
        new Plannet("Mercury", 80, 7, 0.045, new Color(180, 170, 160)),
        new Plannet("Venus", 130, 11, 0.035, new Color(220, 190, 130)),
        new Plannet("Earth", 180, 12, 0.030, new Color(70, 130, 255)),
        new Plannet("Mars", 230, 9, 0.025, new Color(230, 100, 80)),
        new Plannet("Jupiter", 310, 22, 0.015, new Color(230, 180, 140)),
        new Plannet("Saturn", 390, 19, 0.012, new Color(210, 190, 140)),
        new Plannet("Uranus", 460, 14, 0.009, new Color(170, 220, 230)),
        new Plannet("Neptune", 520, 14, 0.007, new Color(100, 130, 240))
    ));

    private final Point center = new Point(width / 2, height / 2 - 40);

    public Solar() {
        setBackground(new Color(5, 7, 15));
        Timer timer = new Timer(16, e -> {
            for (Plannet p : plannets) p.tick(center);
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- 1. Draw Background Orbits ---
        g2.setStroke(new BasicStroke(1.5f));
        for (Plannet p : plannets) {
            g2.setColor(new Color(255, 255, 255, 25));
            int orbitW = p.r * 2;
            int orbitH = (int) (p.r * 2 * TILT);
            g2.drawOval(center.x - p.r, center.y - (orbitH / 2), orbitW, orbitH);
        }

        // --- 2. Depth Sorting ---
        // We sort by currentY so objects "further back" (lower Y) are drawn first
        List<Plannet> sortedPlanets = new ArrayList<>(plannets);
        sortedPlanets.sort(Comparator.comparingInt(p -> p.currentY));

        boolean sunDrawn = false;

        // --- 3. Render Objects ---
        for (Plannet p : sortedPlanets) {
            
            // Draw Sun when we reach the middle depth
            if (!sunDrawn && p.currentY > center.y) {
                drawSun(g2);
                sunDrawn = true;
            }

            drawPlanet(g2, p);
        }
        
        // If planets are all behind the sun, draw sun last
        if (!sunDrawn) drawSun(g2);
    }

   private void drawSun(Graphics2D g2) {
    int sunSize = 45;

    // 1. The Outer Heat Haze (Atmosphere)
    // We use a very soft orange that fades to invisible
    float[] hazeDist = {0.4f, 1.0f};
    Color[] hazeColors = {new Color(255, 100, 0, 100), new Color(255, 50, 0, 0)};
    RadialGradientPaint haze = new RadialGradientPaint(center.x, center.y, sunSize * 2.0f, hazeDist, hazeColors);
    g2.setPaint(haze);
    g2.fillOval(center.x - sunSize * 2, center.y - sunSize * 2, sunSize * 4, sunSize * 4);

    // 2. The Main Sun Body (Multi-color Gradient)
    // This creates a "white-hot" center with a "fire-orange" edge
    float[] sunDist = {0.0f, 0.5f, 0.9f, 1.0f};
    Color[] sunColors = {
        new Color(255, 255, 255),   // White Hot Center
        new Color(255, 220, 0),     // Bright Yellow
        new Color(255, 100, 0),     // Deep Orange
        new Color(200, 50, 0)       // Dark Red Rim
    };
    
    RadialGradientPaint sunBody = new RadialGradientPaint(center.x, center.y, sunSize, sunDist, sunColors);
    g2.setPaint(sunBody);
    g2.fillOval(center.x - sunSize, center.y - sunSize, sunSize * 2, sunSize * 2);

    // 3. Optional: Add a small "Lens Flare" or Shine
    g2.setColor(new Color(255, 255, 255, 80));
    g2.fillOval(center.x - (sunSize/2), center.y - (sunSize/2), sunSize/2, sunSize/3);
}

    private void drawPlanet(Graphics2D g2, Plannet p) {
        // Spherical shadow effect
        float[] dist = {0f, 1f};
        Color[] colors = {p.color.brighter(), p.color.darker().darker()};
        
        // Offset the highlight slightly toward the sun
        RadialGradientPaint pGrad = new RadialGradientPaint(
            p.currentX - p.currentSize/3, p.currentY - p.currentSize/3, 
            p.currentSize * 2.5f, dist, colors);
        
        g2.setPaint(pGrad);
        g2.fillOval(p.currentX - p.currentSize, p.currentY - p.currentSize, p.currentSize * 2, p.currentSize * 2);

        // 3D Rings for Saturn
        if (p.name.equals("Saturn")) {
            drawSaturnRings(g2, p);
        }

        // Label
        g2.setColor(new Color(220, 220, 220, 180));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString(p.name, p.currentX + p.currentSize + 2, p.currentY - p.currentSize);
    }

    private void drawSaturnRings(Graphics2D g2, Plannet p) {
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < 3; i++) {
            int ringW = p.currentSize + 8 + (i * 5);
            int ringH = (int) (ringW * TILT);
            g2.setColor(new Color(200, 180, 150, 120 - (i * 30)));
            g2.drawOval(p.currentX - ringW, p.currentY - ringH, ringW * 2, ringH * 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("3D Solar System Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(width, height);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(new Solar());
            frame.setVisible(true);
        });
    }
}