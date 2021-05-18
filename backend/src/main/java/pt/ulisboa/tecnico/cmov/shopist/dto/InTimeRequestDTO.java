package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;

@Data
public class InTimeRequestDTO {
    private String timestamp;
    private Coordinates coordinates;
}
