package org.stroganoff.client;

import org.apache.log4j.Logger;
import org.stroganoff.IUserInterface;
import org.stroganoff.UserInterface;
import org.stroganoff.util.IMessenger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static final String CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE = "Client successfully connected to socket.";
    public static final String CLIENT_GOT_THE_MESSAGE = "Client got the message: ";
    private final Logger logger = Logger.getLogger(Client.class);
    private final String nickName;
    private final IUserInterface userInterface = new UserInterface();
    private final IMessenger iMessenger;
    private final Socket socket;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private final BufferedReader reader;


    public Client(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream, BufferedReader reader,
                  String nickName, IMessenger iMessenger) {
        this.socket = socket;
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.nickName = nickName;
        this.iMessenger = iMessenger;
        this.reader = reader;
    }

    public void clientStart() {
        try {
            if (socket == null) {
                throw new IOException("Подключение не состоялось");
            }
            logger.info(CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE);
            userInterface.showUserMessage(CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE);

            while (!socket.isOutputShutdown()) {
                if (dataInputStream.available() > 0) {
                    String inputStringFromServer = dataInputStream.readUTF();
                    if ("GetNickName".equals(inputStringFromServer)) {
                        logger.debug(CLIENT_GOT_THE_MESSAGE + inputStringFromServer);
                        iMessenger.sendMessage(dataOutputStream, nickName);
                    } else {
                        userInterface.showUserMessage(inputStringFromServer);
                    }
                }

                if (reader.ready()) {
                    String clientCommand = reader.readLine();
                    iMessenger.sendMessage(dataOutputStream, clientCommand);
                    logger.debug("Client sent message " + clientCommand + " to server.");
                    Thread.sleep(500);

                    // Проверяем условие выхода из соединения
                    if ("quit".equalsIgnoreCase(clientCommand) || "exit".equalsIgnoreCase(clientCommand)) {
                        logger.info("Client commanded to kill connections");
                        if (dataInputStream.available() > 0) {
                            String in = dataInputStream.readUTF();
                            userInterface.showUserMessage(in);
                        }
                        break;
                    }
                }
            }
            logger.info("Closing connections & channels on client side - DONE.");
        } catch (IOException e) {
            logger.error("Client has got an error " + e.getMessage(), e);
            userInterface.showErrorMessage(e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Ожидание потока прервано", e);
            userInterface.showErrorMessage(e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    dataInputStream.close();
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

