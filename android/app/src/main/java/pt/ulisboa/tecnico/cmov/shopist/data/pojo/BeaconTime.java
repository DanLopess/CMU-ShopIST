package pt.ulisboa.tecnico.cmov.shopist.data.pojo;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeaconTime {
    private Timestamp timestamp;
    private Coordinates coordinates;
    private UUID uuid;
}
