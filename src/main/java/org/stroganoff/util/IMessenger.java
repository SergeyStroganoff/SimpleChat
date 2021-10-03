package org.stroganoff.util;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IMessenger {

    public void sendMessage(DataOutputStream dataOutputStream, String message) throws IOException;

    public void sendMessageForAllClient(DataOutputStream out, String inputMessage) throws IOException;

}
