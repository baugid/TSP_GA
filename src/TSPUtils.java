import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class TSPUtils {

    final static Random R = new Random(10000);

    static final TSPGene[] CITIES = generateData(60);
    static final int[] baseArray = IntStream.range(0, CITIES.length).toArray();
    static final double[][] distanceTable = generateTable();

    private static double[][] generateTable() {
        double[][] res = new double[CITIES.length][CITIES.length];
        for (int i = 0; i < CITIES.length; i++) {
            for (int j = i; j < CITIES.length; j++) {
                double dist = CITIES[i].distance(CITIES[j]);
                res[i][j] = (res[j][i] = dist);
            }
        }
        return res;
    }

    private TSPUtils() {
        throw new RuntimeException("No!");
    }

    private static TSPGene[] generateData(final int numDataPoints) {
        final TSPGene[] data = new TSPGene[numDataPoints];
        for (int i = 0; i < numDataPoints; i++) {
            data[i] = new TSPGene(R.nextInt(World.WIDTH), R.nextInt(World.HEIGHT));
        }
        return data;
    }

    static int randomIndex(final int limit) {
        return R.nextInt(limit);
    }

    static boolean[] makeArray(int[] genes) {
        boolean[] result = new boolean[CITIES.length];
        for (int gene : genes) {
            result[gene] = true;
        }
        return result;
    }

    static void shuffleArray(int[] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = randomIndex(i + 1);

            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static List<TSPGene> toTSPGene(int[] chromosome) {
        List<TSPGene> res = new ArrayList<>();
        for (int gen : chromosome) {
            res.add(CITIES[gen]);
        }
        return res;
    }
}
