package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.utils.ActorSystemFactory;
import it.unipd.math.pcd.actors.utils.actors.genetic.*;
import it.unipd.math.pcd.actors.utils.messages.genetic.*;
import it.unipd.math.pcd.actors.utils.actors.TrivialActor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * GeneticActor test class
 * @author Andrea Giacomo Baldan
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
        TestActorRef popSampleRef = new TestActorRef(system.actorOf(GeneticActor.class));
        byte[] solution = new byte[32];
        // init the solution
        for (int i = 0; i < solution.length; i++) {
            solution[i] = 1;
        }
        solution[4] = 0;
        solution[18] = 0;
        solution[23] = 0;
        
        GeneticActor popSampleActor = (GeneticActor) popSampleRef.getUnderlyingActor(system);
        popSampleActor.initPopulationAndSolution(15, solution);
        while(((GeneticActor) popSampleRef.getUnderlyingActor(system)).getFitness() < 32) {
            TestActorRef nature = new TestActorRef(system.actorOf(TrivialActor.class));
            nature.send(new Evolve(), popSampleRef);
        }

        Thread.sleep(2000);

        Assert.assertEquals("The solution should be 11110111111111111101111011111111",
                "11110111111111111101111011111111",
                ((GeneticActor) popSampleRef.getUnderlyingActor(system)).printFittest());
    }
}
