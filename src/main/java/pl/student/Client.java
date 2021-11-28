package pl.student;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);
    public static final String LOCALHOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 6666;

    private Socket socket;
    private Hairdresser hairdresser;

    public Client() {
        this.hairdresser = new Hairdresser();
    }

    public void connect(String ipAddress, int port) throws IOException {
        this.socket = new Socket(ipAddress, port);
        logger.info("Connected to server " + ipAddress + " using port " + port);
        Thread readerThread = new Thread(new ObjectReader(this));
        Thread writerThread = new Thread(new ObjectWriter(this));
        readerThread.start();
        writerThread.start();
        try {
            readerThread.join();
            writerThread.join();
        } catch (InterruptedException e) {
            logger.error("Thread interrupted");
            System.exit(1);
        }
        socket.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connect(Client.LOCALHOST, Client.DEFAULT_PORT);
        } catch (IOException e) {
            logger.error("Cannot connect to server using provided ip: " + Client.LOCALHOST + " on port: " + Client.DEFAULT_PORT);
        }
    }
}
