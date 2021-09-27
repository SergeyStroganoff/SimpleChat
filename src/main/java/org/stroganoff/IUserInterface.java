package org.stroganoff;

import java.io.BufferedReader;
import java.io.IOException;

public interface IUserInterface {
    String getStringFromUser(BufferedReader reader);

    void showUserMessage(String inputMessage);

    void showErrorMessage(String errorExpression);
}
