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
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());) {
            logger.info(CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE);
            userInterface.showUserMessage(CLIENT_SUCCESSFULLY_CONNECTED_MESSAGE);

            while (!socket.isOutputShutdown()) {
                if (dataInputStream.read() > -1) {
                    String inputStringFromServer = dataInputStream.readUTF();
                    if ("GetNickName".equals(inputStringFromServer)) {
                        dataOutputStream.writeUTF(nickName);
                        dataOutputStream.flush();
                    }
                    userInterface.showUserMessage(inputStringFromServer);
                    logger.debug(CLIENT_GOT_THE_MESSAGE + inputStringFromServer);
                }

                if (reader.ready()) {
                    String clientCommand = reader.readLine();
                    dataOutputStream.writeUTF(clientCommand);
                    dataOutputStream.flush();
                    logger.debug("Client sent message " + clientCommand + " to server.");
                    Thread.sleep(500);

                    // проверяем условие выхода из соединения
                    if ("quit".equalsIgnoreCase(clientCommand) || "exit".equalsIgnoreCase(clientCommand)) {
                        logger.info("Client commanded to kill connections");
                        if (dataInputStream.read() > -1) {
                            String in = dataInputStream.readUTF();
                            userInterface.showUserMessage(in);
                        }
                        break;
                    }
                }
            }
            logger.info("Closing connections & channels on clentSide - DONE.");
        } catch (IOException | InterruptedException e) {
            logger.error("Client has got an error", e);
            userInterface.showErrorMessage(e.getMessage());
        }
    }
}

