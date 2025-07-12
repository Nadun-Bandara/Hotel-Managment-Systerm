package entity;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    private Integer id;
    private String name;
    private String contactNumber;
    private int loyaltyPoints;
}
