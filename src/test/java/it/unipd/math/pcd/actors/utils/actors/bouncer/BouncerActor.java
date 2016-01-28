package it.unipd.math.pcd.actors.utils.actors.bouncer;

import it.unipd.math.pcd.actors.AbsActor;
import it.unipd.math.pcd.actors.utils.messages.bouncer.BounceMessage;

/**
 * Created by codep on 1/28/16.
 */
public class BouncerActor extends AbsActor<BounceMessage> {

    String lastStatement;

    public String getLastStatement() {
        return lastStatement;
    }

    @Override
    public void receive(BounceMessage message) {
        lastStatement = message.getStatement();
        switch(message.getStatement().toLowerCase()) {
            case "hi":
                self.send(new BounceMessage("Hello"), sender);
                break;
            case "how are you?":
                self.send(new BounceMessage("Fine."), sender);
                break;
            default:
                self.send(new BounceMessage("42"), sender);
                break;
        }
    }
}
