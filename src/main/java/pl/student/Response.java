package pl.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {
    private int status;
    private String message;
    private List<Reservation> reservationsList;
}
