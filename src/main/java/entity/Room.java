package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Room {
    private Integer id;
    private String number;
    private String type;
    private Double price;
    private String status;
}
