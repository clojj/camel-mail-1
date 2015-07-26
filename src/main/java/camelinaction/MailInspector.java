package camelinaction;

import org.apache.camel.Handler;
import org.apache.camel.Message;

public class MailInspector {

    @Handler
    public void process(Message message) {

        if (!message.hasAttachments()) {
            message.setBody("NOT Ok");
        }
    }
}
