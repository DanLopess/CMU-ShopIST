package pt.ulisboa.tecnico.cmov.shopist.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Beacon {
    private Coordinates coordinates;

    @JsonIgnore
    private BeaconTimeStorage beaconTimeStorage;

    public Beacon(Coordinates coordinates) {
        this.coordinates = coordinates;
        this.beaconTimeStorage = new BeaconTimeStorage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }

    public BeaconTimeStorage getBeaconTimeStorage() {
        if(beaconTimeStorage == null) {
            beaconTimeStorage = new BeaconTimeStorage();
        }
        return beaconTimeStorage;
    }
}
