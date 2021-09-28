package org.stroganoff.client;

import org.apache.log4j.Logger;
import org.stroganoff.IUserInterface;
import org.stroganoff.UserInterface;

import java.io.*;
import java.net.Socket;

public class Client {
    public static final String CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE = "Client successfully connected to socket.";
    public static final String CLIENT_GOT_THE_MESSAGE = "Client got the message: ";
    private String host = "localhost";
    private final int portNumber;
    private final Logger logger = Logger.getLogger(Client.class);
    private final String nickName;
    private final IUserInterface userInterface = new UserInterface();


    public Client(String host, int portNumber, String nickName) {
        this.host = host;
        this.portNumber = portNumber;
        this.nickName = nickName;
    }

    public Client(int portNumber, String nickName) {
        this.portNumber = portNumber;
        this.nickName = nickName;
    }

    public void clientStart() {
        try (Socket socket = new Socket(host, portNumber);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            logger.info(CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE);
            userInterface.showUserMessage(CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE);

            while (!socket.isOutputShutdown()) {
                if (dataInputStream.available() > 0) {
                    String inputStringFromServer = dataInputStream.readUTF();
                    if ("GetNickName".equals(inputStringFromServer)) {
                        logger.debug(CLIENT_GOT_THE_MESSAGE + inputStringFromServer);
                        sendMessage(dataOutputStream, nickName);
                    } else {
                        userInterface.showUserMessage(inputStringFromServer);
                    }
                }

                if (reader.ready()) {
                    String clientCommand = reader.readLine();
                    sendMessage(dataOutputStream, clientCommand);
                    logger.debug("Client sent message " + clientCommand + " to server.");
                    Thread.sleep(500);

                    // проверяем условие выхода из соединения
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
        }
    }

    private void sendMessage(DataOutputStream dataOutputStream, String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }
}

