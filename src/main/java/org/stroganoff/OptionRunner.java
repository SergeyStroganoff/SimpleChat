package org.stroganoff;

import org.stroganoff.client.Client;
import org.stroganoff.server.MultiThreadServer;
import org.stroganoff.util.IMessenger;

import java.io.BufferedReader;
import java.util.Properties;

public class OptionRunner {
    public static final String NICK_MESSAGE = "Введите Ваш никнейм";
    public static final String EXIT_MESSAGE = "Выход по команде пользователя";
    public static final String NO_COMMAND_MESSAGE = "Такой команды нет";

    public void optionRun(IUserInterface userInterface, Properties properties,
                          BufferedReader bufferedReader, IMessenger iMessenger) {
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
                    Client client = new Client(serverIP, port, userNickName, iMessenger);
                    client.clientStart();
                    break;
                }
                case "q": {
                    isCommandGotten = true;
                    userInterface.showUserMessage(EXIT_MESSAGE);
                    System.exit(0);
                    break;
                }
                default: {
                    userInterface.showUserMessage(NO_COMMAND_MESSAGE);
                }
            }
        }
    }

}
