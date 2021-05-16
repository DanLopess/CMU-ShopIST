package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class OutTimeRequestDTO {
    private Timestamp timestamp;
    private UUID uuid;
}
