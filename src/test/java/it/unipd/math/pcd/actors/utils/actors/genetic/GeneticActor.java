package it.unipd.math.pcd.actors.utils.actors.genetic;

import it.unipd.math.pcd.actors.AbsActor;
import it.unipd.math.pcd.actors.utils.messages.genetic.*;

/**
 * Really simple implementation of a genetic algorithm on binary genomes population
 * @author Andrea Giacomo Baldan
 */
public class GeneticActor extends AbsActor<GeneticMessage> {

    /**
     * Genetic algorithm constants
     * mutationRate stands for the probability to have a gene mutation across generations
     * elitism stands for natural selection of elite members on every new generation
     * tournamentSize represents the number of individuals that 'fights' to be the fittest of the generation
     * uniformRate represents the probability to choose a gene from one individual instead of a second one
     * during crossover
     */
    private static final double mutationRate = 0.015;
    private static final boolean elitism = true;
    private static final int tournamentSize = 5;
    private static final double uniformRate = 0.5;

    /**
     * Fitness level (e.g. how close to the solution he is) of the fittest member of the
     * current generation
     */
    private int fitness = 0;

    /**
     * Population of individuals
     */
    Individual[] individuals;

    /**
     * Evolution goal
     */
    private byte[] solution = new byte[32];

    /**
     * Initialize population
     * @param popSize Size of the population sample
     * @param solution Goal of the current evolution cycle
     */
    public void initPopulationAndSolution(int popSize, byte[] solution) {
        individuals = new Individual[popSize];
        initEveryIndividual(popSize);
        setSolution(solution);
    }

    /**
     * Initialize every single member of the population sample
     * @param popSize Size of the population sample
     */
    private void initEveryIndividual(int popSize) {
        for (int i = 0; i < popSize; i++) {
            Individual newIndividual = new Individual();
            newIndividual.generateIndividual();
            individuals[i] = newIndividual;
        }
    }

    /**
     * Sets the goal for the current evolution cycle
     * @param solution Byte type, representing the goal to be reached
     */
    private void setSolution(byte[] solution) {
        this.solution = solution;
    }

    /**
     * Loops through the population sample to find the fittest member
     * (e.g. the closer to the solution in terms of number of equal genes)
     * @return A reference to the fittest individual inside the population of the current generation
     */
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

    /**
     * Calculate the fitness value of the fittest individual in the current generation
     * inside the population sample
     * @return The fitness value of the fittest individual
     */
    private int calcFitness() {
        int fitness;
        Individual indiv = getFittest();
        fitness = indiv.getFitness();
        this.fitness = fitness;
        return fitness;
    }

    /**
     * Tournament selection system used to get the new fittest member of the next generation
     * @param pop Population sample, will give the members to be challenged inside the tournament
     * @return A reference to the fittest member survived in the tournament
     */
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

    /**
     * Mutation in the genes during the current generation
     * @param indiv Individual to mutate
     */
    public void mutate(Individual indiv) {
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                byte gene = (byte) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }

    /**
     * Crossover between two distinct individual inside the population sample,
     * generate a new individual resulted of a mix between his two parents
     * @param indiv1 Parent one
     * @param indiv2 Parent two
     * @return A reference to the newborn generated mixing two parents Individual
     */
    public Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newBorn = new Individual();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newBorn.setGene(i, indiv1.getGene(i));
            } else {
                newBorn.setGene(i, indiv2.getGene(i));
            }
        }
        return newBorn;
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

    /**
     * Private inner class representing an individual of the population sample
     * @author Andrea Giacomo Baldan
     */
    private class Individual {

        /**
         * Genetic inheritance represented as an array of byte
         */
        private byte[] genes = new byte[32];

        /**
         * Initialize the genetic inheritance of the individual with random values
         * between 0 and 1
         */
        public void generateIndividual() {
            for (int i = 0; i < genes.length; i++) {
                byte gene = (byte) Math.round(Math.random());
                genes[i] = gene;
            }
        }

        /**
         * Returns the gene corresponding to index
         * @param index Index inside the array of genes
         * @return The gene corresponding to index
         */
        public byte getGene(int index) {
            return genes[index];
        }

        /**
         * Sets the new gene at the position represented by index inside the array
         * @param index Index of the position inside the genes array
         * @param value New gene to be set inside the genes array
         */
        public void setGene(int index, byte value) {
            genes[index] = value;
        }

        /**
         * Returns the genes array length
         * @return The genes array length
         */
        public int size() {
            return genes.length;
        }

        /**
         * Calculate the fitness value of the individual
         * @return The fitness value of the individual
         */
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
