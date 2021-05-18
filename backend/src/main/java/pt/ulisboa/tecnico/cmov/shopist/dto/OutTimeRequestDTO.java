package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OutTimeRequestDTO {
    private String timestamp;
    private UUID uuid;
}
