package pl.student;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class Hairdresser implements Serializable {
    private List<Reservation> reservationList;

    public Hairdresser() {
        this.reservationList = fromWeekDays(Calendar.MONDAY, Calendar.MONDAY);
    }

    private static Date getSpecificDate(int day, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static List<Reservation> fromWeekDays(int begin, int end) {
        List<Reservation> reservationList = new ArrayList<>();
        for (int i = begin; i <= end; i++) {
            for (int j = 8; j <= 18; j++) {
                reservationList.add(new Reservation(getSpecificDate(i, j)));
            }
        }
        return reservationList;
    }

    public void displayTable() {
        for (Reservation reservation : this.reservationList) {
            System.out.println(reservation);
        }
    }

    private Reservation searchForSpecificDate(Date date) {
        for (Reservation reservation : this.reservationList) {
            if (reservation.getDate().equals(date)) {
                return reservation;
            }
        }
        return null;
    }

    public void addReservation(Date date, Long clientId) throws NoSuchElementException, Exception {
        Reservation reservation = this.searchForSpecificDate(date);
        System.out.println(date);
        if (reservation == null) {
            throw new NoSuchElementException("Cannot find this reservation");
        }
        if(!reservation.isFree()) {
            if(reservation.getClientId().equals(clientId)) {
                throw new NoSuchElementException("You have already make this reservation");
            }
            throw new NoSuchElementException("Someone has already made this reservation");
        }
        reservation.setTaken(clientId);
    }

    public void removeReservation(Date date, Long clientId) throws NoSuchElementException, Exception {
        Reservation reservation = this.searchForSpecificDate(date);
        if (reservation == null) {
            throw new NoSuchElementException("Cannot find this reservation");
        }
        if(!reservation.isFree()) {
            if(!reservation.getClientId().equals(clientId)) {
                throw new NoSuchElementException("You don't have permission to remove this reservation");
            } else {
                reservation.setFree();
            }
        } else {
            throw new NoSuchElementException("Someone has already taken this reservation");
        }
    }
}
