package org.stroganoff;

import java.io.BufferedReader;

public interface IUserInterface {
    String getStringFromUser(BufferedReader reader);

    void showUserMessage(String inputMessage);

    void showErrorMessage(String errorExpression);
}
