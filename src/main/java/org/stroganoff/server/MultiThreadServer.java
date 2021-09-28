package org.stroganoff.server;

import org.apache.log4j.Logger;
import org.stroganoff.IUserInterface;
import org.stroganoff.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    private static final String CONNECTION_ACCEPTED_MESSAGE = "Connection accepted with ";
    private static final String SOCKET_ERROR_CONNECTION = "Fixed ServerSocket error connection";
    private static final String SERVER_START_MESSAGE = "Server socket created, and listen to server commands";
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
    private final Logger logger = Logger.getLogger(MultiThreadServer.class);
    protected static final List<MonoThreadServer> serverList = Collections.synchronizedList(new LinkedList<>()); // We can use ConcurrentHashMap
    private final int portNumber;
    private final IUserInterface userInterface = new UserInterface();
    private final History history = new HistoryManager();

    public MultiThreadServer(int portNumber) {
        this.portNumber = portNumber;
    }

    public void startServer() {

        try (ServerSocket server = new ServerSocket(portNumber);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            logger.info(SERVER_START_MESSAGE);
            userInterface.showUserMessage(SERVER_START_MESSAGE);
            userInterface.showUserMessage("Для выхода введите - exit");
            ConsoleListener consoleListener = new ConsoleListener(server, br);
            consoleListener.startListening();
            while (!server.isClosed()) {
                Socket client = server.accept();
                logger.debug("We have got a client connection" + client.getInetAddress());
                MonoThreadServer monoThreadServer = new MonoThreadServer(client, history);
                serverList.add(monoThreadServer);
                fixedThreadPool.execute(monoThreadServer);
                logger.info(CONNECTION_ACCEPTED_MESSAGE + client.getInetAddress().getHostAddress());
            }
            fixedThreadPool.shutdown();
        } catch (IOException e) {
            logger.error(SOCKET_ERROR_CONNECTION, e);
        }
    }
}