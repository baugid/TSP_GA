import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Integer.min;

public class TSPPopulation {

    private List<TSPChromosome> population;
    private final int initialSize;
    private int generationsSinceStable;
    private boolean permanentlyStable;

    TSPPopulation(final int initialSize) {
        this.population = init(initialSize);
        this.initialSize = initialSize;
    }

    boolean isStable() {
        return population.get(initialSize * 3 / 4).getDistance() == getAlpha().getDistance();
    }

    int getPopulationSize() {
        return initialSize;
    }

    TSPChromosome getAlpha() {
        return this.population.get(initialSize - 1);
    }

    private List<TSPChromosome> init(final int initialSize) {
        final List<TSPChromosome> eden = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {
            final TSPChromosome chromosome = TSPChromosome.create();
            eden.add(chromosome);
        }
        return eden;
    }

    void update() {
        doMutation();
        doReverse();
        doSpawn();
        doCrossOver();
        doSelection();

        generationsSinceStable++;
        if (isStable()) {
            if (generationsSinceStable <= TSPUtils.CITIES.length / 5) {
                permanentlyStable = true;
            }
            generationsSinceStable = 0;
            fixSociety();
        }
    }

    private void doReverse() {
        final List<TSPChromosome> newPopulation = new ArrayList<>();
        for (int i = 0; i < this.population.size() / 15; i++) {
            final TSPChromosome mutation = this.population.get(TSPUtils.randomIndex(this.population.size())).reverse();
            newPopulation.add(mutation);
        }
        this.population.addAll(newPopulation);
    }

    public void fixSociety() {
        if (allMutated == 10) {
            allMutated = 0;
            replacePopulation();
        } else {
            mutatePopulation();
        }
    }

    private int allMutated = 0;

    private void mutatePopulation() {
        for (int i = 0; i < population.size() - 1; i++) {
            TSPChromosome newChr = population.get(0);
            int count = min(TSPUtils.randomIndex(allMutated + 1 + i / 100) + 2, TSPUtils.CITIES.length);
            for (int j = 0; j < count; j++) {
                newChr = newChr.mutate();
            }
            population.set(i, newChr);
        }
        allMutated++;
    }

    private void replacePopulation() {
        for (int i = population.size() / 10; i < population.size() - 1; i++) {
            population.set(i, TSPChromosome.create());
        }
    }

    private void doSpawn() {
        for (int i = 0; i < initialSize / 9; i++) {
            this.population.add(TSPChromosome.create());
        }
    }

    private void doSelection() {
        PriorityQueue<TSPChromosome> queue = new PriorityQueue<>(Comparator.comparingDouble(TSPChromosome::getDistance).reversed());
        for (int i = 0; i < initialSize; i++) {
            queue.add(population.get(i));
        }

        for (TSPChromosome item : population) {
            if (queue.peek().getDistance() > item.getDistance()) { //doesn't throw, because queue is never empty
                queue.poll();
                queue.add(item);
            }
        }

        ArrayList<TSPChromosome> newPop = new ArrayList<>(queue);
        newPop.sort(Comparator.comparingDouble(TSPChromosome::getDistance).reversed());
        population = newPop;
    }

    private void doMutation() {
        final List<TSPChromosome> newPopulation = new ArrayList<>();
        for (int i = 0; i < this.population.size() / 5; i++) {
            final TSPChromosome mutation = this.population.get(TSPUtils.randomIndex(this.population.size())).mutate();
            newPopulation.add(mutation);
        }
        this.population.addAll(newPopulation);
    }

    private void doCrossOver() {
        final List<TSPChromosome> newPopulation = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            TSPChromosome partner = getCrossOverPartner(i);
            TSPChromosome[] newChromosomes = population.get(i).crossOver(partner);
            newPopulation.add(newChromosomes[0]);
            newPopulation.add(newChromosomes[1]);
        }
        this.population.addAll(newPopulation);
    }

    private TSPChromosome getCrossOverPartner(int chromosome) {
        int index;
        do {
            index = TSPUtils.randomIndex(this.population.size());
        } while (chromosome == index);
        return population.get(index);
    }

    private TSPChromosome getRandomElement() {
        return population.get(TSPUtils.randomIndex(population.size()));
    }

    public void mixIn(TSPPopulation other) {
        permanentlyStable = false;
        for (int i = 0; i < initialSize / 10; i++) {
            population.set(TSPUtils.randomIndex(initialSize - 1), other.getRandomElement());
        }
    }

    public boolean isPermanentlyStable() {
        return permanentlyStable;
    }
}
