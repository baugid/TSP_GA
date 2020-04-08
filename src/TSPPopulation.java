import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TSPPopulation {

    private ArrayList<TSPChromosome> population;
    private final int initialSize;

    TSPPopulation(final int initialSize) {
        init(initialSize);
        this.initialSize = initialSize;
    }

    int getPopulationSize() {
        return initialSize;
    }

    TSPChromosome getAlpha() {
        return this.population.get(initialSize - 1);
    }

    private void init(final int initialSize) {
        population = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {
            final TSPChromosome chromosome = TSPChromosome.create(TSPUtils.baseArray);
            population.add(chromosome);
        }
    }

    void update() {
        doCrossOver();
        doMutation();
        doSpawn();
        doSelection();
    }

    private void doSpawn() {
        for (int i = 0; i < 1000; i++) {
            this.population.add(TSPChromosome.create(TSPUtils.baseArray));
        }
    }

    private void doSelection() {
        PriorityQueue<TSPChromosome> queue = new PriorityQueue<>(Comparator.comparingDouble(TSPChromosome::getDistance).reversed());
        for (int i = 0; i < initialSize; i++) {
            queue.add(population.get(i));
        }
        for (TSPChromosome item : population) {
            if (queue.peek().getDistance() > item.getDistance()) {
                queue.remove();
                queue.add(item);
            }
        }

        ArrayList<TSPChromosome> newPop = new ArrayList<>(queue);
        newPop.sort(Comparator.comparingDouble(TSPChromosome::getDistance).reversed());
        population = newPop;
    }

    private void doMutation() {
        int startSize = population.size();
        for (int i = 0; i < startSize / 10; i++) {
            final TSPChromosome mutation = this.population.get(TSPUtils.randomIndex(startSize)).mutate();
            population.add(mutation);
        }
    }

    private void doCrossOver() {
        int startSize = population.size();
        population.ensureCapacity(3 * startSize);
        for (int i = 0; i < startSize; i++) {
            TSPChromosome partner = getCrossOverPartner(i, startSize);
            TSPChromosome[] newChromosomes = population.get(i).crossOver(partner);
            population.add(newChromosomes[0]);
            population.add(newChromosomes[1]);
        }
    }

    private TSPChromosome getCrossOverPartner(int chromosome, int size) {
        int index;
        do {
            index = TSPUtils.randomIndex(size);
        } while (chromosome == index);
        return population.get(index);
    }
}
