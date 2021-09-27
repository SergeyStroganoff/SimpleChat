package org.stroganoff;

import org.apache.log4j.Logger;
import org.stroganoff.client.Client;
import org.stroganoff.server.MultiThreadServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class App {
    public static final Logger logger = Logger.getLogger(App.class);
    public static final String FILE_PROP_ERROR_LOGGER = "File properties loading error";
    public static final String FILE_PROP_ERROR_USER = "файл настроек отсутствует или поврежден";

    public static void main(String[] args) throws InterruptedException {
        IUserInterface userInterface = new UserInterface();
        Initializer initializer = new Initializer();
        Properties properties = null;
        try {
            properties = initializer.getAppProp();
        } catch (IOException e) {
            logger.error(FILE_PROP_ERROR_LOGGER, e);
            userInterface.showErrorMessage(FILE_PROP_ERROR_USER);
            userInterface.showUserMessage("Программа будет завершена");
            Thread.sleep(5000);
            System.exit(1);
        }

        userInterface.showUserMessage("Введите 1 для запуска сервера и 2 для запуска клиента");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String userCommand = null;
        int port = Integer.parseInt(properties.getProperty("port"));
        boolean isCommandGotten = false;
        while (!isCommandGotten) {
            userCommand = userInterface.getStringFromUser(bufferedReader);
            if ("1".equals(userCommand)) {
                isCommandGotten = true;
                MultiThreadServer multiThreadServer = new MultiThreadServer(port);
                multiThreadServer.startServer();
            }
            if ("2".equals(userCommand)) {
                isCommandGotten = true;
                userInterface.showUserMessage("Введите Ваш никнейм");
                String userNickName = userInterface.getStringFromUser(bufferedReader);
                String serverIP = properties.getProperty("serverIP");
                Client client = new Client(serverIP, port, userNickName);
                client.clientStart();
            }
        }
    }
}
