package pl.student;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

enum ReservationStatus implements Serializable {
    FREE,
    TAKEN,
}

@Getter
@ToString
public class Reservation implements Serializable {
    private Long clientId;
    private final Date date;
    private ReservationStatus status;

    public Reservation(Date date) {
        this.clientId = null;
        this.date = date;
        this.status = ReservationStatus.FREE;
    }

    public boolean isFree() {
        return this.status == ReservationStatus.FREE;
    }

    public void setTaken(Long clientId) {
        this.status = ReservationStatus.TAKEN;
        this.clientId = clientId;
    }

    public void setFree() {
        this.status = ReservationStatus.TAKEN;
        this.clientId = null;
    }
}
