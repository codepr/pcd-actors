package it.unipd.math.pcd.actors.utils.actors.bouncer;

import it.unipd.math.pcd.actors.AbsActor;
import it.unipd.math.pcd.actors.exceptions.UnsupportedMessageException;
import it.unipd.math.pcd.actors.Message;
import it.unipd.math.pcd.actors.utils.messages.bouncer.BounceMessage;
import it.unipd.math.pcd.actors.utils.messages.bouncer.ResponseMessage;

/**
 * Created by codep on 1/28/16.
 */
public class BouncerActor extends AbsActor<Message> {

    String lastStatement;

    public String getLastStatement() {
        return lastStatement;
    }

    @Override
    public void receive(Message message) {
        if(message instanceof BounceMessage) {
            lastStatement = ((BounceMessage) message).getStatement();
            switch (((BounceMessage) message).getStatement().toLowerCase()) {
                case "hi":
                    self.send(new ResponseMessage("Hello"), sender);
                    break;
                case "how are you?":
                    self.send(new ResponseMessage("Fine."), sender);
                    break;
                default:
                    self.send(new ResponseMessage("42"), sender);
                    break;
            }
        } else if (message instanceof ResponseMessage) {
            lastStatement = ((ResponseMessage) message).getResponse();
        } else {
            throw new UnsupportedMessageException(message);
        }
    }
}
