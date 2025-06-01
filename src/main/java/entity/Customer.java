package entity;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Integer id;
    private String name;
    private String contactNumber;
    private int loyaltyPoints;
}
