package dto;

import lombok.*;

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
