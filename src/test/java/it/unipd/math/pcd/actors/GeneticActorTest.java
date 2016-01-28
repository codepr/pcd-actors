package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.utils.ActorSystemFactory;
import it.unipd.math.pcd.actors.utils.actors.genetic.*;
import it.unipd.math.pcd.actors.utils.messages.genetic.*;
import it.unipd.math.pcd.actors.utils.actors.TrivialActor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by codep on 1/27/16.
 */
public class GeneticActorTest {

    private ActorSystem system;

    /**
     * Initializes the {@code system} with a concrete implementation before each test.
     */
    @Before
    public void init() {
        this.system = ActorSystemFactory.buildActorSystem();
    }

    @Test
    public void shouldCalcFittestIndividual() throws InterruptedException {
        TestActorRef genRef = new TestActorRef(system.actorOf(GeneticActor.class));
        byte[] solution = new byte[32];
        for (int i = 0; i < solution.length; i++) {
            solution[i] = 1;
        }
        solution[4] = 0;
        solution[18] = 0;
        solution[23] = 0;
        GeneticActor genActor = (GeneticActor) genRef.getUnderlyingActor(system);
        genActor.initPopulationAndSolution(15, solution);
        while(((GeneticActor) genRef.getUnderlyingActor(system)).getFitness() < 32) {
            TestActorRef nature = new TestActorRef(system.actorOf(TrivialActor.class));
            nature.send(new Evolve(), genRef);
        }

        Thread.sleep(2000);

        Assert.assertEquals("The solution should be 11110111111111111101111011111111",
                "11110111111111111101111011111111",
                ((GeneticActor) genRef.getUnderlyingActor(system)).printFittest());
    }
}
