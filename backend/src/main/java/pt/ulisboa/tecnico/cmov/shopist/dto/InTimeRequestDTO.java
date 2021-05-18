package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Coordinates;

@Data
public class InTimeRequestDTO {
    private String timestamp;
    private Coordinates coordinates;
}
