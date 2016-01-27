package it.unipd.math.pcd.actors.utils.messages.genetic;

/**
 * Created by codep on 1/27/16.
 */
public class Result extends GeneticMessage{
    private int result;

    public Result(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
