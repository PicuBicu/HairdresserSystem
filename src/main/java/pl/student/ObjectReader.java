package pl.student;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReader implements Runnable {

    private static final Logger logger = LogManager.getLogger(Client.class);
    private final ObjectInputStream reader;
    private final Client clientHandler;

    public ObjectReader(Client client) throws IOException {
        this.clientHandler = client;
        this.reader = new ObjectInputStream(client.getSocket().getInputStream());
    }

    @Override
    public void run() {
        Response input;
        try {
            while ((input = (Response) reader.readObject()) != null) {
                logger.info("Response status " + input.getStatus() + " Message: " + input.getMessage());
                if (input.getReservationsList() != null) {
                    this.clientHandler.getHairdresser().setReservationList(input.getReservationsList());
                    logger.info("Updating hairdresser reservation list");
                }
            }
        } catch (IOException e) {
            logger.error("Could not read message from server");
        } catch (ClassNotFoundException e) {
            logger.error("Class not found");
        } finally {
            try {
                this.reader.close();
            } catch (IOException e) {
                logger.error("Cannot close input stream");
            }
        }
    }
}
