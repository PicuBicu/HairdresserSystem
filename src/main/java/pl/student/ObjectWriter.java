package pl.student;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ObjectWriter implements Runnable {

    private static final Logger logger = LogManager.getLogger(Client.class);
    private final ObjectOutputStream writer;
    private final Scanner userInput;
    private final Client client;

    public ObjectWriter(Client client) throws IOException {
        this.userInput = new Scanner(System.in);
        this.client = client;
        this.writer = new ObjectOutputStream(client.getSocket().getOutputStream());
    }

    public String getMessage() {
        return this.userInput.nextLine();
    }

    public void displayCommands() {
        System.out.println("1) - Display hairdresser's table");
        System.out.println("2) - Make a reservation");
        System.out.println("3) - Remove reservation");
    }

    private Date getSpecificDate(String message) throws NumberFormatException {
        String[] args = message.split(" ");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(args[0]) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(args[1]));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        logger.info("Parsed date" + calendar.getTime());
        return calendar.getTime();
    }

    @Override
    public void run() {
        String message;
        while (true) {
            try {
                this.displayCommands();
                System.out.println("Choose an option: ");
                message = this.getMessage();
                switch (message) {
                    case "1":
                        this.client.getHairdresser().displayTable();
                        break;
                    case "2":
                    case "3":
                        System.out.println("Put message in correct order (1-Monday ... 5-Friday) (8-18 hour)");
                        writer.writeObject(new Request(Integer.parseInt(message), getSpecificDate(getMessage())));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
