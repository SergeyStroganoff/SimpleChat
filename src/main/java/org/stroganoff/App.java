package org.stroganoff;

import org.apache.log4j.Logger;
import org.stroganoff.exception.OptionRunnerException;
import org.stroganoff.exception.PropertiesException;
import org.stroganoff.util.IMessenger;
import org.stroganoff.util.Messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class App {
    public static final Logger logger = Logger.getLogger(App.class);
    public static final String FILE_PROP_ERROR_USER = "файл настроек отсутствует или поврежден";
    public static final String USER_FIRST_MESSAGE = "Введите 1 для запуска сервера, 2 для запуска клиента -'q' для выхода";
    public static final String ERROR_MESSAGE = "Программа будет завершена";

    public static void main(String[] args) throws InterruptedException, OptionRunnerException {
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
        IMessenger iMessenger = new Messenger();
        OptionRunner optionRunner = new OptionRunner();
        optionRunner.optionRun(userInterface, properties, bufferedReader, iMessenger);
    }


}
