package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class BeaconTimes {
    private Timestamp in;
    private Timestamp out;
}
