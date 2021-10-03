package org.stroganoff.util;

import org.stroganoff.server.MonoThreadServer;
import org.stroganoff.server.MultiThreadServer;

import javax.annotation.processing.Messager;
import java.io.DataOutputStream;
import java.io.IOException;

public class Messenger implements IMessenger {

    public void sendMessage(DataOutputStream dataOutputStream, String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }

    public void sendMessageForAllClient(DataOutputStream out, String inputMessage) throws IOException {
        synchronized (MultiThreadServer.getServerList()) {
            for (MonoThreadServer monoThreadServer : MultiThreadServer.getServerList()) {
                if (monoThreadServer.getOut() != out) {
                    sendMessage(monoThreadServer.getOut(), inputMessage); // Отослать принятое сообщение всем по списку
                }
            }
        }
    }
}
