package org.stroganoff;

import org.apache.log4j.Logger;
import org.stroganoff.client.Client;
import org.stroganoff.exception.OptionRunnerException;
import org.stroganoff.server.MultiThreadServer;
import org.stroganoff.util.IMessenger;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class OptionRunner {
    public static final Logger logger = Logger.getLogger(OptionRunner.class);
    public static final String NICK_MESSAGE = "Введите Ваш никнейм";
    public static final String EXIT_MESSAGE = "Выход по команде пользователя";
    public static final String NO_COMMAND_MESSAGE = "Такой команды нет";

    public void optionRun(IUserInterface userInterface, Properties properties,
                          BufferedReader bufferedReader, IMessenger iMessenger) throws OptionRunnerException {
        String userCommand = null;
        int port = Integer.parseInt(properties.getProperty("port"));
        boolean isCommandGotten = false;
        while (!isCommandGotten) {
            userCommand = userInterface.getStringFromUser(bufferedReader);

            switch (userCommand) {
                case "1": {
                    isCommandGotten = true;
                    MultiThreadServer multiThreadServer = new MultiThreadServer(port, iMessenger);
                    multiThreadServer.startServer();
                    break;
                }
                case "2": {
                    isCommandGotten = true;
                    userInterface.showUserMessage(NICK_MESSAGE);
                    String userNickName = userInterface.getStringFromUser(bufferedReader);
                    String serverIP = properties.getProperty("serverIP");

                    Socket socket = null;
                    DataOutputStream dataOutputStream = null;
                    DataInputStream dataInputStream = null;
                    try {
                        socket = new Socket(serverIP, port);
                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataInputStream = new DataInputStream(socket.getInputStream());
                    } catch (IOException e) {
                        logger.error("Client has got an error " + e.getMessage(), e);
                        userInterface.showErrorMessage(e.getMessage());
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    Client client = new Client(socket, dataOutputStream, dataInputStream, reader, userNickName, iMessenger, userInterface);
                    client.clientStart();
                    break;
                }
                case "q": {
                    userInterface.showUserMessage(EXIT_MESSAGE);
                    throw new OptionRunnerException("Выход по требованию пользователя");
                }
                default: {
                    userInterface.showUserMessage(NO_COMMAND_MESSAGE);
                }
            }
        }
    }

}
