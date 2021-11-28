package pl.student;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);
    public static final int DEFAULT_PORT = 6666;

    private ServerSocket socket;
    private final List<ClientHandler> connectionsList;
    @Getter
    private Hairdresser hairdresser;

    public Server() throws IOException {
        this.connectionsList = new LinkedList<>();
        this.hairdresser = new Hairdresser();
    }

    public void startServer(int port) {
        try {
            this.socket = new ServerSocket(port);
            logger.info("Server started");
            logger.info("Server is now waiting for clients");
            while (true){
                Socket newClient = socket.accept();
                ClientHandler newCon = new ClientHandler(newClient, this);
                this.connectionsList.add(newCon);
                newCon.start();
                logger.info("New client with id " + newCon.getId() + " has joined the server");
            }
        } catch (IOException e) {
            logger.error("Problem with opening new connection with client");
        }
    }

    public void broadcastMessage(Response message) throws IOException {
        for(ClientHandler con : connectionsList) {
            if(con.isAlive() && con.getSocket().isConnected()) {
                con.sendMessage(message);
            }
        }
        logger.info("Publish updated hairdresser table to all users");
    }

    public static void main(String[] args) throws IOException {
        try {
            Server server = new Server();
            server.startServer(Server.DEFAULT_PORT);
        } catch (Exception e) {
            logger.error("Could not start server on port: " + DEFAULT_PORT);
        }
    }
}
