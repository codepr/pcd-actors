package it.unipd.math.pcd.actors.utils.messages.bouncer;

import it.unipd.math.pcd.actors.Message;

/**
 * Created by codep on 1/29/16.
 */
public class ResponseMessage implements Message {

    String response;

    public ResponseMessage(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}

