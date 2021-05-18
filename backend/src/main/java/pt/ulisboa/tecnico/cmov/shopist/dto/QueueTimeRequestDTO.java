package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QueueTimeRequestDTO {
    private Coordinates coordinates;
    private UUID uuid;
}
