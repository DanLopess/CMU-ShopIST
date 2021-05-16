package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Coordinates;

import java.sql.Timestamp;

@Data
public class InTimeRequestDTO {
    private Timestamp timestamp;
    private Coordinates coordinates;
}
