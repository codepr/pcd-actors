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
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;
import it.unipd.math.pcd.actors.mailbox.MailBox;
import it.unipd.math.pcd.actors.mailbox.MailBoxImpl;
import it.unipd.math.pcd.actors.impl.AbsActorRef;

/**
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;

    /**
     * MailBox for incoming messages
     */
    protected MailBox<T> mailBox;

    /**
     * Actor internal status flag
     */
    private volatile boolean alive;

    /**
     * Looping to apply receive method on incoming messages status flag
     */
    private volatile boolean looping;

    public AbsActor() {
        this.mailBox = new MailBoxImpl<>();
        this.alive = true;
        this.looping = false;
    }

    /**
     * Sets the self-reference.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }

    public final void setSender(ActorRef<T> sender) {
        this.sender = sender;
    }

    public void enqueue(T message) {
        if (!alive)
            throw new NoSuchActorException();
        mailBox.enqueue(message);
        if(!this.looping) start();
    }

    public void stop() {
        this.alive = false;
        this.looping = false;
        while(!mailBox.isEmpty()) {
            try {
                receive(getNextMessage());
            } catch (NoSuchActorException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAlive() {
        return this.alive;
    }

    private T getNextMessage() {
        if (!alive)
            throw new NoSuchActorException();
        return this.mailBox.remove();
    }

    private synchronized void start() {
        ((AbsActorRef<T>) self).execute(new ReceiveLoop());
        this.alive = true;
        this.looping = true;
    }

    private class ReceiveLoop implements Runnable {
        @Override
        public void run() {
            while (isAlive()) {
                try {
                    receive(getNextMessage());
                } catch (NoSuchActorException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
