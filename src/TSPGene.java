public class TSPGene {

    private final int x;
    private final int y;

    TSPGene(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

    double distance(TSPGene other) {
        int dx = getX() - other.getX();
        int dy = getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
