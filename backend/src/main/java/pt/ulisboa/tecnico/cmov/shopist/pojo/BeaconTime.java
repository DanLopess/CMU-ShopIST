package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class BeaconTime {
    private Timestamp timestamp;
    private Coordinates coordinates;
    private UUID uuid;
}
