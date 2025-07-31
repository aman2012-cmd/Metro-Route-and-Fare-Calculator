package MetroApp;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class MapView extends JPanel {

    private final Graph_M graph;

    public MapView(Graph_M graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(1300, 800));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraph((Graphics2D) g);
    }

    private void drawGraph(Graphics2D g2d) {
        Map<String, double[]> positions = Map.ofEntries(
                Map.entry("Noida Sector 62", new double[]{50, 100}),
                Map.entry("Noida Sector 59", new double[]{150, 100}),
                Map.entry("Noida Sector 61", new double[]{250, 100}),
                Map.entry("Noida Sector 52", new double[]{350, 100}),
                Map.entry("Noida Sector 34", new double[]{450, 100}),
                Map.entry("Noida City Centre", new double[]{550, 100}),
                Map.entry("Golf Course", new double[]{650, 100}),
                Map.entry("Botanical Garden", new double[]{750, 100}),
                Map.entry("Yamuna Bank", new double[]{850, 100}),
                Map.entry("Laxmi Nagar", new double[]{950, 100}),
                Map.entry("Rajiv Chowk", new double[]{850, 200}),
                Map.entry("Samaypur Badli", new double[]{50, 500}),
                Map.entry("Rohini Sector 18", new double[]{150, 500}),
                Map.entry("Rohini Sector 16", new double[]{250, 500}),
                Map.entry("Rohini Sector 15", new double[]{350, 500}),
                Map.entry("Rohini West", new double[]{450, 500}),
                Map.entry("Pitampura", new double[]{550, 500}),
                Map.entry("Netaji Subhash Place", new double[]{650, 500}),
                Map.entry("Kashmere Gate", new double[]{750, 500}),
                Map.entry("Mandi House", new double[]{850, 400}),
                Map.entry("Central Secretariat", new double[]{950, 400}),
                Map.entry("INA", new double[]{1050, 300}),
                Map.entry("AIIMS", new double[]{1150, 300}),
                Map.entry("Green Park", new double[]{1250, 300}),
                Map.entry("Hauz Khas", new double[]{1050, 200}),
                Map.entry("Malviya Nagar", new double[]{1150, 200}),
                Map.entry("Saket", new double[]{1250, 200}),
                Map.entry("Qutub Minar", new double[]{1350, 200})
        );

        Map<String, Color> lineColors = Map.of(
                "Blue", Color.BLUE,
                "Yellow", Color.YELLOW,
                "Red", Color.RED,
                "Green", Color.GREEN,
                "Magenta", Color.MAGENTA,
                "Orange", Color.ORANGE
        );

        // Draw metro lines
        for (String from : graph.getStations()) {
            double[] fromPos = positions.get(from);
            if (fromPos == null) {
                continue;
            }

            for (String to : graph.getNeighbors(from)) {
                double[] toPos = positions.get(to);
                if (toPos == null) {
                    continue;
                }

                String line = graph.getLine(from, to);
                g2d.setColor(lineColors.getOrDefault(line, Color.GRAY));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine((int) fromPos[0], (int) fromPos[1], (int) toPos[0], (int) toPos[1]);
            }
        }

        // Draw stations
        for (Map.Entry<String, double[]> entry : positions.entrySet()) {
            String station = entry.getKey();
            double[] pos = entry.getValue();

            g2d.setColor(Color.BLACK); // Keep the station dots black
            g2d.fillOval((int) pos[0] - 5, (int) pos[1] - 5, 10, 10);

            // Set a darker, bolder font for station names
            g2d.setColor(new Color(50, 50, 50)); // Darker grey for better contrast
            g2d.setFont(new Font("Arial", Font.BOLD, 11)); // Bolder and slightly larger font size
            g2d.drawString(station, (int) pos[0] + 7, (int) pos[1] - 5);
        }
    }
}
