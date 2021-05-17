package pt.ulisboa.tecnico.cmov.shopist.dto;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Coordinates;

import java.util.UUID;

@Data
public class QueueTimeRequestDTO {
    private Coordinates coordinates;
    private UUID uuid;
}
