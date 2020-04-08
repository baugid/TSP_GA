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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TSPGene gene = (TSPGene) o;
        return this.x == gene.x &&
                this.y == gene.y;
    }
}
