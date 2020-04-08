import javax.swing.*;
import java.awt.*;

public class World extends JPanel {

    private final TSPPopulation population;
    private int generation = 0;

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    private World() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        this.population = new TSPPopulation(25000);
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.CYAN);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString("gen : " + ++this.generation, 350, 15);
        g.drawString("pop size : " + this.population.getPopulationSize(), 150, 15);
        g.drawString("shortest path : "
                + String.format("%.2f", population.getAlpha().getDistance()), 500, 15);
        drawBestChromosome(g);
    }

    private void drawBestChromosome(final Graphics2D g) {
        final java.util.List<TSPGene> chromosome = TSPUtils.toTSPGene(this.population.getAlpha().getChromosome());
        g.setColor(Color.WHITE);
        for (int i = 0; i < chromosome.size() - 1; i++) {
            TSPGene gene = chromosome.get(i);
            TSPGene neighbor = chromosome.get(i + 1);
            g.drawLine(gene.getX(), gene.getY(), neighbor.getX(), neighbor.getY());
        }
        g.setColor(Color.RED);
        for (final TSPGene gene : chromosome) {
            g.fillOval(gene.getX() - 3, gene.getY() - 3, 6, 6);
        }
    }

    public static void main(String[] args) {
        World w = new World();
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("Genetic Algorithms");
            frame.setResizable(false);
            frame.add(w, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        Thread t = new Thread(() -> {
            while (true) {
                w.calc();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void calc() {
        long start = System.nanoTime();
        for (int i = 0; i < 200; i++) {
            this.population.update();
        }
        System.out.println(System.nanoTime() - start);
        repaint();
    }
}
