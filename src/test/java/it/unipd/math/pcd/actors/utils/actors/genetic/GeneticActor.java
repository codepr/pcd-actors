package it.unipd.math.pcd.actors.utils.actors.genetic;

import it.unipd.math.pcd.actors.AbsActor;
import it.unipd.math.pcd.actors.utils.messages.genetic.*;

/**
 * Created by codep on 1/27/16.
 */
public class GeneticActor extends AbsActor<GeneticMessage> {

    private static final double mutationRate = 0.015;
    private static final boolean elitism = true;
    private static final int tournamentSize = 5;
    private static final double uniformRate = 0.5;

    private int fitness = 0;
    Individual[] individuals;
    private byte[] solution = new byte[32];

    public void setGenesAndSolution(int popSize, byte[] solution) {
        individuals = new Individual[popSize];
        for (int i = 0; i < popSize; i++) {
            Individual newIndividual = new Individual();
            newIndividual.generateIndividual();
            individuals[i] = newIndividual;
        }
        this.solution = solution;
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < individuals.length; i++) {
            if (fittest.getFitness() <= individuals[i].getFitness()) {
                fittest = individuals[i];
            }
        }
        return fittest;
    }

    private int calcFitness() {
        int fitness;
        Individual indiv = getFittest();
        fitness = indiv.getFitness();
        this.fitness = fitness;
        return fitness;
    }

    private Individual tournamentSelection(Individual[] pop) {
        Individual[] tournament = new Individual[tournamentSize];
        for (int i = 0; i < tournamentSize; ++i) {
            tournament[i] = new Individual();
        }
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.length);
            tournament[i] = pop[randomId];
        }
        Individual fittest = pop[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < pop.length; i++) {
            if (fittest.getFitness() <= pop[i].getFitness()) {
                fittest = pop[i];
            }
        }
        return fittest;
    }

    public void mutate(Individual indiv) {
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                byte gene = (byte) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }

    public Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    @Override
    public void receive(GeneticMessage message) {
        if (message instanceof Evolve) {
            Individual[] newPopulation = new Individual[individuals.length];
            for (int i = 0; i < newPopulation.length; ++i) {
                newPopulation[i] = new Individual();
            }
            if (elitism) {
                newPopulation[0] = this.getFittest();
            }

            // Crossover population
            int elitismOffset;
            if (elitism) {
                elitismOffset = 1;
            } else {
                elitismOffset = 0;
            }

            for (int i = elitismOffset; i < newPopulation.length; i++) {
                Individual indiv1 = tournamentSelection(newPopulation);
                Individual indiv2 = tournamentSelection(newPopulation);
                Individual newIndiv = crossover(indiv1, indiv2);
                newPopulation[i] = newIndiv;
            }

            for (int i = elitismOffset; i < newPopulation.length; i++) {
                mutate(newPopulation[i]);
            }

            individuals = newPopulation;
            calcFitness();
        } else if (message instanceof Get) {
            self.send(new Result(fitness), sender);
        } else if (message instanceof GetSolution) {
            self.send(new Solution(getFittest().toString()), sender);
        }
    }

    public int getFitness() {
        return fitness;
    }

    public String printFittest() {
        return getFittest().toString();
    }

    private class Individual {

        private byte[] genes = new byte[32];

        public void generateIndividual() {
            for (int i = 0; i < genes.length; i++) {
                byte gene = (byte) Math.round(Math.random());
                genes[i] = gene;
            }
        }

        public byte getGene(int index) {
            return genes[index];
        }

        public void setGene(int index, byte value) {
            genes[index] = value;
        }

        public int size() {
            return genes.length;
        }

        public int getFitness() {
            int fitness = 0;
            // Loop through individual's genes and compare them to the solution ones
            for (int i = 0; i < genes.length; i++) {
                if (genes[i] == solution[i]) {
                    fitness++;
                }
            }
            return fitness;
        }

        @Override
        public String toString() {
            String geneString = "";
            for (int i = 0; i < size(); i++) {
                geneString += getGene(i);
            }
            return geneString;
        }
    }
}
