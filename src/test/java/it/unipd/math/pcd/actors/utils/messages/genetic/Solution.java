package it.unipd.math.pcd.actors.utils.messages.genetic;

/**
 * Created by codep on 1/27/16.
 */
public class Solution extends GeneticMessage{
    private String solution;

    public Solution(String solution) {
        this.solution = solution;
    }

    public String getSolution() {
        return solution;
    }
}
