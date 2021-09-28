package org.stroganoff;

import org.apache.log4j.Logger;
import org.stroganoff.client.Client;
import org.stroganoff.exception.PropertiesException;
import org.stroganoff.server.MultiThreadServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class App {
    public static final Logger logger = Logger.getLogger(App.class);
    public static final String FILE_PROP_ERROR_USER = "файл настроек отсутствует или поврежден";
    public static final String USER_FIRST_MESSAGE = "Введите 1 для запуска сервера и 2 для запуска клиента";
    public static final String ERROR_MESSAGE = "Программа будет завершена";
    public static final String NICK_MESSAGE = "Введите Ваш никнейм";

    public static void main(String[] args) throws InterruptedException {
        IUserInterface userInterface = new UserInterface();
        Initializer initializer = new Initializer();
        Properties properties = null;
        try {
            properties = initializer.getAppProp();
        } catch (IOException | PropertiesException e) {
            logger.error(e.getMessage(), e);
            userInterface.showErrorMessage(FILE_PROP_ERROR_USER);
            userInterface.showUserMessage(ERROR_MESSAGE);
            Thread.sleep(5000);
            System.exit(1);
        }

        userInterface.showUserMessage(USER_FIRST_MESSAGE);
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
                userInterface.showUserMessage(NICK_MESSAGE);
                String userNickName = userInterface.getStringFromUser(bufferedReader);
                String serverIP = properties.getProperty("serverIP");
                Client client = new Client(serverIP, port, userNickName);
                client.clientStart();
            }
        }
    }
}
