package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.utils.ActorSystemFactory;
import it.unipd.math.pcd.actors.utils.actors.bouncer.BouncerActor;
import it.unipd.math.pcd.actors.utils.messages.bouncer.BounceMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.INTERNAL;

/**
 * Created by codep on 1/28/16.
 */
public class BouncerActorTest {

    private ActorSystem system;

    /**
     * Initializes the {@code system} with a concrete implementation before each test.
     */
    @Before
    public void init() {
        this.system = ActorSystemFactory.buildActorSystem();
    }

    @Test
    public void shouldAnswerToStatements() throws InterruptedException {
        TestActorRef oracle = new TestActorRef(system.actorOf(BouncerActor.class));
        TestActorRef stater = new TestActorRef(system.actorOf(BouncerActor.class));

        stater.send(new BounceMessage("hi"), oracle);

        Thread.sleep(2000);
        Assert.assertEquals("Should answer hello", "42", ((BouncerActor) stater.getUnderlyingActor(system)).getLastStatement());
    }
}
