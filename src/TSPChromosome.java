import java.util.Arrays;

public class TSPChromosome {

    private final int[] chromosome;
    private final double distance;

    public double getDistance() {
        return this.distance;
    }

    private TSPChromosome(final int[] chromosome) {
        this.chromosome = chromosome;
        this.distance = calculateDistance();
    }

    static TSPChromosome create(final int[] points) {
        int[] genes = Arrays.copyOf(points, points.length);
        TSPUtils.shuffleArray(genes);
        return new TSPChromosome(genes);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final int gene : this.chromosome) {
            builder.append(TSPUtils.CITIES[gene]).append((" : "));
        }
        return builder.toString();
    }

    int[] getChromosome() {
        return this.chromosome;
    }

    private double calculateDistance() {
        double total = 0.0f;
        for (int i = 0; i < this.chromosome.length - 1; i++) {
            total += TSPUtils.distanceTable[chromosome[i]][chromosome[i + 1]];
        }
        return total;
    }

    TSPChromosome[] crossOver(final TSPChromosome other) {
        int[] myDNA = Arrays.copyOf(chromosome, TSPUtils.randomIndex(chromosome.length - 1) + 1);
        int[] firstCrossOver = new int[chromosome.length];

        System.arraycopy(myDNA, 0, firstCrossOver, 0, myDNA.length);
        cross(other.chromosome, TSPUtils.makeArray(myDNA), firstCrossOver, myDNA.length);


        int[] secondCrossOver = new int[chromosome.length];
        int[] otherDNA = Arrays.copyOf(other.chromosome, TSPUtils.randomIndex(chromosome.length - 1) + 1);

        System.arraycopy(otherDNA, 0, secondCrossOver, 0, otherDNA.length);
        cross(chromosome, TSPUtils.makeArray(otherDNA), secondCrossOver, otherDNA.length);

        return new TSPChromosome[]{
                new TSPChromosome(firstCrossOver),
                new TSPChromosome(secondCrossOver)
        };
    }

    private void cross(int[] otherDNA, boolean[] base, int[] firstCrossOver, int startIdx) {
        int curIdx = startIdx;
        for (int gene : otherDNA) {
            if (!base[gene]) {
                firstCrossOver[curIdx++] = gene;
            }
        }
    }

    TSPChromosome mutate() {
        final int[] copy = Arrays.copyOf(chromosome, chromosome.length);
        int indexA;
        int indexB;
        do {
            indexA = TSPUtils.randomIndex(copy.length);
            indexB = TSPUtils.randomIndex(copy.length);
        } while (indexA == indexB);
        copy[indexA] = chromosome[indexB];
        copy[indexB] = chromosome[indexA];
        return new TSPChromosome(copy);
    }
}
