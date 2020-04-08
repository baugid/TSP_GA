import java.util.Arrays;

public class TSPMixer {
    private final TSPPopulation[] populations;
    int generation = 1;
    int mixCycle = 50;

    public TSPMixer(TSPPopulation[] populations) {
        this.populations = populations;
    }

    void mixPopulations() {
        if (generation % mixCycle == 0) {
            mix();
            if (mixCycle > 5)
                mixCycle = mixCycle - 2;
        }
        generation++;
    }

    private void mix() {
        Arrays.stream(populations).parallel().forEach(pop -> {
            TSPPopulation other = getMixPartner(pop);
            pop.mixIn(other);
        });
    }

    private TSPPopulation getMixPartner(TSPPopulation pop) {
        int index;
        do {
            index = TSPUtils.randomIndex(populations.length);
        } while (pop == populations[index]);
        return populations[index];
    }
}