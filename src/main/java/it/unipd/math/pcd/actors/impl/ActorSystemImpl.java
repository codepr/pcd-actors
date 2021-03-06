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
import it.unipd.math.pcd.actors.ActorRef;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ActorSystemImpl extends AbsActorSystem {

    /**
     * ExecutorService to generate a thread pool
     */
    private ExecutorService eService;

    /**
     * Constructor to initialize {@code eService} as a {@code newCachedThreadPool}
     */
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
        if (mode == ActorMode.LOCAL)
            return new LocalActorRef(this);
        else throw new IllegalArgumentException();
    }

    /**
     * Stops all the actors of the system, clear the container (map) and shutdown
     * executor service instance {@code eService}
     */
    @Override
    public void stop() {
        super.stop();
        eService.shutdown();
    }

    /**
     * Execute a runnable with {@code eService} instance of Executor
     * @param receivingLoop Runnable type to be executed
     */
    @Override
    public void startActorReceiveLoop(Runnable receivingLoop) {
        eService.execute(receivingLoop);
    }
}
