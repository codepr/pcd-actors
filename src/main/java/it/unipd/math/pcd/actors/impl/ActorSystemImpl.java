/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Andrea Giacomo Baldan
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * Please, insert description here.
 *
 * @author Andrea Giacomo Baldan
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors.impl;

import it.unipd.math.pcd.actors.AbsActorSystem;
import it.unipd.math.pcd.actors.Actor;
import it.unipd.math.pcd.actors.AbsActor;
import it.unipd.math.pcd.actors.ActorRef;
import it.unipd.math.pcd.actors.Message;
import it.unipd.math.pcd.actors.ActorSystem;
import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.HashMap;
import java.util.Map;

public class ActorSystemImpl extends AbsActorSystem {

    private ExecutorService eService;

    public ActorSystemImpl() {
        eService = Executors.newCachedThreadPool();
    }

    /**
     * Create an instance of {@link ActorRef}
     * @param mode Possible mode to create an actor. Could be{@code LOCAL} or
     * {@code REMOTE}.
     * @return An instance to {@link ActorRef}
     */
	@Override
    protected ActorRef createActorReference(ActorMode mode) {
        if(mode == ActorMode.LOCAL)
            return new LocalActorRef(this);
        else return null;
    }

    public void startSystem(Actor<?> actor) {
        eService.execute(new StartLoop<>((AbsActor<Message>) actor));
    }

    private class StartLoop<T extends Message> implements Runnable {
        public AbsActor<T> actor;

        public StartLoop(Actor<T> actor) { this.actor = (AbsActor) actor; }

        public void run() {
            if(actor.isAlive()) {
                while(true) {
                    try {
                        actor.receive(actor.getNextMessage());
                    } catch (NoSuchActorException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
