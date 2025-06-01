package dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
   private int reservationId;
   private int customerId;
   private int roomId;
   private LocalDate checkindate;
   private LocalDate checkoutDate;
   private Double amount;
   private String status;

}
