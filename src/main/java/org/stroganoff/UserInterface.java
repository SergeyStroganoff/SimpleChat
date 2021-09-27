package org.stroganoff;

import java.io.BufferedReader;
import java.io.IOException;

public class UserInterface implements IUserInterface {

    public static final String ERROR_MESSAGE = "Произошла ошибка: ";

    @Override
    public String getStringFromUser(BufferedReader reader) {
        String expressionString = " ";
        try {
            expressionString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
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
