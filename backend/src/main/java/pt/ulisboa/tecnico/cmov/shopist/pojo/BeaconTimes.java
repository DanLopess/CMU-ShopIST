package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BeaconTimes {
    private LocalDateTime in;
    private LocalDateTime out;
}
