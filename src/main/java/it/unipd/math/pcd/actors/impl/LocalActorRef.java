package it.unipd.math.pcd.actors.impl;

import it.unipd.math.pcd.actors.ActorSystem;
import it.unipd.math.pcd.actors.Message;
import it.unipd.math.pcd.actors.ActorRef;

/**
 * Created by codep on 1/23/16.
 */
public class LocalActorRef<T extends  Message> extends AbsActorRef<T> {

    public LocalActorRef(ActorSystem system) {
        super(system);
    }
}
