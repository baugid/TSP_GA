import java.util.Arrays;

public class TSPMetaPopulation {
    private final TSPPopulation[] populations;
    private final TSPMixer mixer;

    public TSPMetaPopulation(int populations, int citizens) {
        this.populations = new TSPPopulation[populations];
        for (int i = 0; i < populations; i++) {
            this.populations[i] = new TSPPopulation(citizens);
        }
        mixer = new TSPMixer(this.populations);
    }

    public TSPChromosome getAlpha() {
        TSPChromosome best = populations[0].getAlpha();
        for (int i = 1; i < populations.length; i++) {
            if (populations[i].getAlpha().getDistance() < best.getDistance()) {
                best = populations[i].getAlpha();
            }
        }
        return best;
    }

    public void update() {
        Arrays.stream(populations).parallel().forEach(TSPPopulation::update);
        mixer.mixPopulations();
    }

    public int getPopulationSize() {
        return populations.length * populations[0].getPopulationSize();
    }

    public boolean isUnstable() {
        boolean stable = true;
        for (TSPPopulation pop : populations) {
            stable &= pop.isPermanentlyStable();
        }
        return !stable;
    }
}
