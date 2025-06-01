package entity;

import lombok.*;

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
   private Date checkindate;
   private Date checkoutDate;
   private Double amount;
   private String status;

}
