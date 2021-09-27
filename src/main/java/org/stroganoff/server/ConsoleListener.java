package org.stroganoff.server;

import org.apache.log4j.Logger;
import org.stroganoff.IUserInterface;
import org.stroganoff.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;

public class ConsoleListener {
    private final ServerSocket server;
    private final BufferedReader bufferedReader;
    private final IUserInterface userInterface = new UserInterface();
    private final Logger logger = Logger.getLogger(ConsoleListener.class);

    public ConsoleListener(ServerSocket server, BufferedReader bufferedReader) {
        this.server = server;
        this.bufferedReader = bufferedReader;
    }

    public void startListening() {
        Runnable runnable = () -> {
            String serverCommand = userInterface.getStringFromUser(bufferedReader);
            if ("quit".equalsIgnoreCase(serverCommand) || "exit".equalsIgnoreCase(serverCommand)) {
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("Ошибка при закрытии сервера", e);
                    userInterface.showErrorMessage("при закрытии сервера");
                }
            }
        };
        new Thread(runnable).start();
    }
}
