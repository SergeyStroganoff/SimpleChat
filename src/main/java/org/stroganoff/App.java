package org.stroganoff;

import org.apache.log4j.Logger;
import org.stroganoff.client.Client;
import org.stroganoff.server.MultiThreadServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    public static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        IUserInterface userInterface = new UserInterface();
        userInterface.showUserMessage("Введите 1 для запуска сервера и 2 для запуска клиента");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String userCommand = null;
        boolean isCommandGotten = false;
        while (!isCommandGotten) {
            userCommand = userInterface.getStringFromUser(bufferedReader);
            if ("1".equals(userCommand)) {
                isCommandGotten = true;
                MultiThreadServer multiThreadServer = new MultiThreadServer();
                multiThreadServer.startServer();
            }
            if ("2".equals(userCommand)) {
                isCommandGotten = true;
                userInterface.showUserMessage("Введите Ваш никнейм");
                String userNickName = userInterface.getStringFromUser(bufferedReader);
                Client client = new Client(3180, userNickName);
                client.clientStart();
            }
        }
    }
}
