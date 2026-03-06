import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Solar extends JPanel {
    static final int width = 950;
    static final int height = 900;

    static class Plannet {
        String name;
        int r, size;
        double speed, angle;
        Color color;

        public Plannet(String name, int r, int size, double speed, Color color) {
            this.name = name;
            this.r = r;
            this.size = size;
            this.speed = speed;
            this.color = color;
        }

        void tick() {
            angle += speed;
        }
    }

    private final Plannet[] plannets = new Plannet[]{
        new Plannet("Mercury", 70, 6, 0.055, new Color(180, 170, 160)),
        new Plannet("Venus", 110, 10, 0.040, new Color(206, 178, 120)),
        new Plannet("Earth", 150, 10, 0.035, new Color(90, 140, 255)),
        new Plannet("Mars", 185, 8, 0.030, new Color(220, 110, 90)),
        new Plannet("Jupiter", 250, 18, 0.020, new Color(220, 190, 150)),
        new Plannet("Saturn", 310, 16, 0.017, new Color(220, 200, 140)),
        new Plannet("Uranus", 360, 12, 0.014, new Color(160, 200, 220)),
        new Plannet("Neptune", 410, 12, 0.012, new Color(120, 150, 240)),
    };

    private final Point center = new Point(width / 2, height / 2 - 60);

    public Solar() {
        setBackground(new Color(10, 12, 20)); // Deep space color
        Timer timer = new Timer(16, e -> {
            for (Plannet p : plannets) {
                p.tick();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Draw Orbit Paths ---
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(255, 255, 255, 40)); // Paint white for orbits
        for (Plannet p : plannets) {
            int d = p.r * 2;
            g2.drawOval(center.x - p.r, center.y - p.r, d, d);
        }

        // --- Draw Sun ---
        int sunSize = 36;
        GradientPaint sunGradient = new GradientPaint(
            center.x - sunSize, center.y - sunSize, new Color(255, 220, 120),
            center.x + sunSize, center.y + sunSize, new Color(255, 140, 50)
        );
        g2.setPaint(sunGradient);
        g2.fillOval(center.x - sunSize, center.y - sunSize, sunSize * 2, sunSize * 2);

        // --- Draw Planets ---
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        for (Plannet p : plannets) {
            // Calculate position based on angle
            int px = center.x + (int) (p.r * Math.cos(p.angle));
            int py = center.y + (int) (p.r * Math.sin(p.angle));

            // Planet body
            g2.setColor(p.color);
            g2.fillOval(px - p.size, py - p.size, p.size * 2, p.size * 2);

            // Special handling for Saturn's Rings
            if (p.name.equals("Saturn")) {
                g2.setColor(new Color(230, 220, 180, 180));
                AffineTransform old = g2.getTransform();
                g2.translate(px, py);
                g2.rotate(-0.6);
                g2.drawOval(-p.size - 6, -p.size / 2, (p.size + 6) * 2, p.size);
                g2.setTransform(old);
            }

            // Planet Label
            g2.setColor(new Color(240, 240, 240));
            int lx = px + 10;
            int ly = py - 10;
            g2.drawString(p.name, lx, ly);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Solar System (Clean Version)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(width, height);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(new Solar());
            frame.setVisible(true);
        });
    }
}