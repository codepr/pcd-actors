/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
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

/**
 * Please, insert description here.
 *
 * @author Andrea Giacomo Baldan
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

/**
 * A reference of an actor that allow to locate it in the actor system.
 * Using this reference it is possible to send a message among actors.
 *
 * @author Andrea Giacomo Baldan
 * @version 1.0
 * @since 1.0
 */
public class ActorRefImpl<T extends Message> implements ActorRef<T> {

    /**
     * {@link ActorSystem} reference for sending messages
     */
    private ActorSystem actorSystem;

    /**
     * {@link ActorMode} type to set mode
     */
    private ActorSystem.ActorMode mode;

    public ActorRefImpl(ActorSystem actorSystem, ActorSystem.ActorMode mode) {
        this.actorSystem = actorSystem;
        this.mode = mode;
    }

    /**
     * Sends a {@code message} to another actor
     *
     * @param message The message to send
     * @param to The actor to which sending the message
     */
    @Override
    public void send(T message, ActorRef to) {
        AbsActor<T> actor = (AbsActor<T>) actorSystem.getActorInstance(to);
        actor.receive(message);
    }

    /**
     * Compare two actorRef
     *
     * @param to The ActorRef reference to compare with
     * @return An integer that states if to is < = or > of this
     */
    @Override
    public int compareTo(ActorRef to) {
        return this.compareTo(to);
    }
}
