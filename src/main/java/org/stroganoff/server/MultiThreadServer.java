package org.stroganoff.server;

import org.apache.log4j.Logger;

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
    private static final String MAIN_SERVER_EXITING_MESSAGE = "Main Server exiting...";

    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
    Logger logger = Logger.getLogger(MultiThreadServer.class);
    public static final List<MonoThreadServer> serverList = Collections.synchronizedList(new LinkedList<>()); // We can use ConcurrentHashMap

    public boolean startServer() {
        try (ServerSocket server = new ServerSocket(3180);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            logger.info(SERVER_START_MESSAGE);
            while (!server.isClosed()) {
                if (br.ready()) {
                    String serverCommand = br.readLine();
                    if ("quit".equalsIgnoreCase(serverCommand) || "exit".equalsIgnoreCase(serverCommand)) {
                        logger.info(MAIN_SERVER_EXITING_MESSAGE);
                        break;
                    }
                }
                Socket client = server.accept();
                MonoThreadServer monoThreadServer = new MonoThreadServer(client);
                serverList.add(monoThreadServer);
                fixedThreadPool.execute(monoThreadServer);
                logger.info(CONNECTION_ACCEPTED_MESSAGE + client.getInetAddress().getHostAddress());
            }
            fixedThreadPool.shutdown();
        } catch (IOException e) {
            logger.error(SOCKET_ERROR_CONNECTION, e);
        }
        return true;
    }
}