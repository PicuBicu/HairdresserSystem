package pl.student;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

@Getter
public class ClientHandler extends Thread {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final Socket socket;
    private final Server server;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(Response message) throws IOException {
        out.writeObject(message);
        out.reset();
    }

    @Override
    public void run() {
        try {
            sendMessage(new Response(200, "Initiating hairdresser table", this.server.getHairdresser().getReservationList()));
            while (true) {
                Request message = (Request) in.readObject();
                if (message == null) {
                    break;
                }
                try {
                    switch (message.getCommand()) {
                        case 2:
                            this.server.getHairdresser().addReservation(message.getDate(), this.getId());
                            this.server.broadcastMessage(new Response(200, "Add new reservation", this.server.getHairdresser().getReservationList()));
                            break;
                        case 3:
                            this.server.getHairdresser().removeReservation(message.getDate(), this.getId());
                            this.server.broadcastMessage(new Response(200, "Remove reservation", this.server.getHairdresser().getReservationList()));
                            break;
                    }
                    this.server.getHairdresser().displayTable();
                } catch (Exception e) {
                    out.writeObject(new Response(422, e.getMessage(), null));
                }
                logger.info("Message from client - " + this.getId() + ": " + message.getCommand() + "Date: " + message.getDate());
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        finally {
            this.close();
        }
    }

    public void close() {
        try {
            if (this.in != null) {
                this.in.close();
            }
            if (this.out != null) {
                this.out.close();
            }
        } catch (IOException e) {
           logger.error(e.getMessage());
        }
    }
}
