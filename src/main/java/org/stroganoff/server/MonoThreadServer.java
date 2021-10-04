package org.stroganoff.server;

import org.apache.log4j.Logger;
import org.stroganoff.util.IMessenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadServer implements Runnable {
    public static final String CLOSING_SERVER_MESSAGE = "Closing connections & channels - DONE.";
    public static final String CLIENT_INITIALIZED_CLOSE_CONNECTION = "Client initialized close connection ";
    public static final String CREATED_MONO_SERVER_MESSAGE = "DataInputStream && DataOutputStream created. MonoServer ";
    private final Logger logger = Logger.getLogger(MonoThreadServer.class);
    private final Socket threadSocket;
    private final StringBuilder stringBuilder = new StringBuilder();
    private DataOutputStream out;
    private DataInputStream in;
    private String nikNameClient;
    private final History history;
    IMessenger iMessenger;

    public MonoThreadServer(Socket threadSocket, History history, IMessenger iMessenger) {
        this.threadSocket = threadSocket;
        this.history = history;
        this.iMessenger = iMessenger;
    }

    public String getNikNameClient() {
        return nikNameClient;
    }

    public DataOutputStream getOut() {
        return out;
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(threadSocket.getOutputStream());
            in = new DataInputStream(threadSocket.getInputStream());
            // канал записи в сокет следует инициализировать сначала канал чтения для избежания блокировки выполнения программы на ожидании заголовка в сокете
            logger.debug(CREATED_MONO_SERVER_MESSAGE + threadSocket.getInetAddress().getHostAddress() + " is ready ");
            Thread.sleep(500);
            iMessenger.sendMessage(out, "GetNickName");
            nikNameClient = in.readUTF();
            stringBuilder.append("[").append(nikNameClient).append("]").append(" - ");
            iMessenger.sendMessage(out, history.getHistoryMessage(5).toString());
            while (!threadSocket.isClosed()) {
                String inputMessage = in.readUTF();
                if ("quit".equalsIgnoreCase(inputMessage) || "exit".equalsIgnoreCase(inputMessage)) {
                    logger.info(CLIENT_INITIALIZED_CLOSE_CONNECTION + threadSocket.getInetAddress().getHostAddress());
                    iMessenger.sendMessage(out, "Server reply - " + inputMessage + " - OK");
                    break;
                }
                // main work here
                stringBuilder.append(inputMessage);
                history.putMessage(stringBuilder.toString());
                iMessenger.sendMessageForAllClient(out, stringBuilder.toString());
                stringBuilder.delete(nikNameClient.length() + 5, stringBuilder.length());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("working thread" + threadSocket.getInetAddress() + "trow exception", e);
        } finally {
            try {
                threadSocket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info(CLOSING_SERVER_MESSAGE);
            MultiThreadServer.serverList.remove(this);
        }
    }
}

