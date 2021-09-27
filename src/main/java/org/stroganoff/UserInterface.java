package org.stroganoff;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class UserInterface implements IUserInterface {
    public static final Logger logger = Logger.getLogger(UserInterface.class);
    public static final String ERROR_MESSAGE = "Произошла ошибка: ";
    public static final String INPUT_ERROR_LOG_MESSAGE = "Произошла ошибка при считывании команды с консоли";

    @Override
    public String getStringFromUser(BufferedReader reader) {
        String expressionString = " ";
        try {
            expressionString = reader.readLine();
        } catch (IOException e) {
            logger.error(INPUT_ERROR_LOG_MESSAGE, e);
            showErrorMessage("при вводе с консоли");
        }
        return expressionString;
    }

    @Override
    public void showUserMessage(String inputMessagePartTwo) {
        System.out.println(inputMessagePartTwo);
    }

    @Override
    public void showErrorMessage(String errorExpression) {
        System.out.println(ERROR_MESSAGE + errorExpression);
    }
}
