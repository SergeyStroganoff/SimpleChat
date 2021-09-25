package org.stroganoff.server;

import org.apache.log4j.Logger;

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
    private DataOutputStream out;
    private DataInputStream in;
    private String nikNameClient;
    private final StringBuilder stringBuilder = new StringBuilder();

    public MonoThreadServer(Socket threadSocket) {
        this.threadSocket = threadSocket;
    }

    public String getNikNameClient() {
        return nikNameClient;
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(threadSocket.getOutputStream());
            in = new DataInputStream(threadSocket.getInputStream());
            // канал записи в сокет следует инициализировать сначала канал чтения для избежания блокировки выполнения программы на ожидании заголовка в сокете
            logger.debug(CREATED_MONO_SERVER_MESSAGE + threadSocket.getInetAddress().getHostAddress() + " is ready ");
            sendMessage("GetNickName");
            nikNameClient = in.readUTF();
            stringBuilder.append("[").append(nikNameClient).append("]").append(" - ");
            while (!threadSocket.isClosed()) {
                String inputMessage = in.readUTF();
                if ("quit".equalsIgnoreCase(inputMessage) || "exit".equalsIgnoreCase(inputMessage)) {
                    logger.info(CLIENT_INITIALIZED_CLOSE_CONNECTION + threadSocket.getInetAddress().getHostAddress());
                    sendMessage("Server reply - " + inputMessage + " - OK");
                    break;
                }
                // main work here
                System.out.println(inputMessage);
                stringBuilder.append(inputMessage);
                for (MonoThreadServer monoThreadServer : MultiThreadServer.serverList) {
                    monoThreadServer.sendMessage(stringBuilder.toString()); // Отослать принятое сообщение всем по списку
                }
                stringBuilder.delete(nikNameClient.length() + 5, stringBuilder.length());
            }
        } catch (IOException e) {
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

    private void sendMessage(String inputMessage) throws IOException {
        out.writeUTF(inputMessage);
        logger.debug("Server Wrote message to client " + inputMessage);
        out.flush();
    }
}

