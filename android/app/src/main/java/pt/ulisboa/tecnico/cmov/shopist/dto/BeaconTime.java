package pt.ulisboa.tecnico.cmov.shopist.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeaconTime {
    private String timestamp;
    private Coordinates coordinates;
    private UUID uuid;
}
