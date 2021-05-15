package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.BeaconTime;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Beacon;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Coordinates;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pt.ulisboa.tecnico.cmov.shopist.util.ShopISTUtils.isEmpty;

@Slf4j
@Service
public class BeaconService {
    private final Set<Beacon> beacons;

    public BeaconService() {
        this.beacons = new HashSet<>();
    }

    public Beacon createBeacon(Beacon s) throws StoreExistsException, InvalidDataException {
        validateBeaconData(s);
        if (!beacons.add(s)) {
            throw new StoreExistsException("Store already exists in server.");
        }
        return s;
    }

    public Beacon updateBeacon(Beacon s) throws StoreNotFoundException, InvalidDataException {
        validateBeaconData(s);
        Optional<Beacon> storeToUpdate = beacons.stream().filter(s::equals).findAny();
        if (storeToUpdate.isEmpty()) {
            throw new StoreNotFoundException("Specified beacon was not found");
        } else {
            Beacon updatedBeacon = storeToUpdate.get();
            updatedBeacon.setCoordinates(s.getCoordinates());
            return s;
        }
    }

    public Optional<Beacon> getBeaconByCoordinatesRange(Coordinates coordinates) {
        if (coordinates == null) return Optional.empty();
        return Optional.ofNullable(getBeaconWithLeastDistance(coordinates));
    }

    private Beacon getBeaconWithLeastDistance(Coordinates coordinates) {
        Beacon beaconRes = null;
        double distanceRes = Double.MAX_VALUE;
        for(Beacon beacon: beacons) {
            if(beaconRes == null) {
                beaconRes = beacon;
                distanceRes = coordinates.distance(beacon.getCoordinates());
            }
            if(coordinates.distance(beacon.getCoordinates()) <= distanceRes) {
                beaconRes = beacon;
                distanceRes = coordinates.distance(beacon.getCoordinates());
            }
        }
        if(distanceRes > 100) {
            return null;
        }
        return beaconRes;
    }

    private void validateBeaconData(Beacon s) throws InvalidDataException {
        if (s.getCoordinates() == null) {
            throw new InvalidDataException("Store has invalid information");
        }
    }

    public UUID inTime(BeaconTime time) throws InvalidDataException {
        UUID uuid = UUID.randomUUID();
        Optional<Beacon> beacon = this.getBeaconByCoordinatesRange(time.getCoordinates());
        Beacon beacon1 = beacon.orElse(null);
        if(beacon1 != null) {
            beacon1.getBeaconTimeStorage().saveInTime(time.getUuid(), time.getTimestamp());
            return uuid;
        }
        throw new InvalidDataException("store not found");
    }

    public void outTime(BeaconTime time) throws InvalidDataException {
        Optional<Beacon> beacon = this.getBeaconByCoordinatesRange(time.getCoordinates());
        Beacon beacon1 = beacon.orElse(null);
        if(beacon1 != null) {
            beacon1.getBeaconTimeStorage().saveOutTime(time.getUuid(), time.getTimestamp());
            return;
        }
        throw new InvalidDataException("store not found");
    }

    public long getMeanDurationLast1Hour(Coordinates coordinates) {
        Optional<Beacon> store = this.getBeaconByCoordinatesRange(coordinates);
        return store.map(beacon1 -> beacon1.getBeaconTimeStorage().getMeanDurationLast1Hour()).orElse(0L);
    }

    // TODO GET ALL STORES BY COORDINATE PROXIMITY
}
