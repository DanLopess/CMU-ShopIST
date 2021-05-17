package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class QueueTimeRequestDTO {
    private Coordinates coordinates;
    private UUID uuid;
}
