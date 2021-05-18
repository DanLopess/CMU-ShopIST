package pt.ulisboa.tecnico.cmov.shopist.data.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueueTimeRequestDTO {
    private Coordinates coordinates;
    private UUID uuid;
}
