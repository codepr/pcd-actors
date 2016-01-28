package it.unipd.math.pcd.actors.utils.messages.bouncer;

import it.unipd.math.pcd.actors.Message;
/**
 * Created by codep on 1/28/16.
 */
public class BounceMessage implements Message {
    String statement;

    public BounceMessage(String statement) {
        this.statement = statement;
    }

    public String getStatement() {
        return statement;
    }
}
