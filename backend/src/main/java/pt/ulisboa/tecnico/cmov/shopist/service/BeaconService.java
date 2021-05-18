package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.dto.InTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.OutTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.QueueTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.QueueTimeResponseDTO;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.BeaconTime;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Beacon;
import pt.ulisboa.tecnico.cmov.shopist.pojo.BeaconTimes;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Coordinates;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pt.ulisboa.tecnico.cmov.shopist.util.ShopISTUtils.isEmpty;

@Slf4j
@Service
public class BeaconService {
    private final Set<Beacon> beacons;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public BeaconService() {
        this.beacons = new HashSet<>();
    }

    public Beacon createBeacon(Beacon s) throws StoreExistsException, InvalidDataException {
        validateBeaconData(s);
        if (!beacons.add(s)) {
            throw new StoreExistsException("Beacon already exists in server.");
        }
        return s;
    }

    public Optional<List<Beacon>> getBeaconByTimeUuid(int meters, Coordinates coordinates) {
        if (coordinates == null) return Optional.empty();
        List<Beacon> beaconRes = new ArrayList<>();
        for(Beacon beacon: beacons) {
            if(coordinates.distance(beacon.getCoordinates()) <= meters) {
                beaconRes.add(beacon);
            }
        }
        return Optional.of(beaconRes);
    }

    private Optional<Beacon> getBeaconWithLeastDistance(Coordinates coordinates) {
        if (coordinates == null) return Optional.empty();
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
            return Optional.empty();
        }
        return Optional.of(beaconRes);
    }

    private void validateBeaconData(Beacon s) throws InvalidDataException {
        if (s.getCoordinates() == null) {
            throw new InvalidDataException("Store has invalid information");
        }
        Optional<Beacon> beacon = getBeaconWithLeastDistance(s.getCoordinates());
        Beacon beacon1 = beacon.orElse(null);
        if(beacon1 != null) {
            throw new InvalidDataException("Beacon already exists in a radius of 100 meters");
        }
    }

    public UUID inTime(InTimeRequestDTO time) throws InvalidDataException {
        UUID uuid = UUID.randomUUID();
        Optional<Beacon> beacon = this.getBeaconWithLeastDistance(time.getCoordinates());
        Beacon beacon1 = beacon.orElse(null);
        if(beacon1 != null) {
            LocalDateTime localTime = LocalDateTime.parse(time.getTimestamp(), formatter);
            beacon1.getBeaconTimeStorage().saveInTime(uuid, localTime);
            return uuid;
        }
        throw new InvalidDataException("beacon not found");
    }

    public UUID outTime(OutTimeRequestDTO time) throws InvalidDataException {
        Beacon beacon = this.getBeaconWithUuid(time.getUuid());
        if(beacon != null) {
            LocalDateTime localTime = LocalDateTime.parse(time.getTimestamp(), formatter);
            beacon.getBeaconTimeStorage().saveOutTime(time.getUuid(), localTime);
            return time.getUuid();
        }
        throw new InvalidDataException("beacon not found");
    }

    private Beacon getBeaconWithUuid(UUID uuid) {
        for(Beacon beacon: beacons) {
            if(beacon.getBeaconTimeStorage().hasStorageUuid(uuid)) {
                return beacon;
            }
        }
        return null;
    }

    public QueueTimeResponseDTO getMeanDurationLast1Hour(QueueTimeRequestDTO requestDTO) {
        Optional<Beacon> beacon;
        beacon = this.getBeaconWithLeastDistance(requestDTO.getCoordinates());
        return beacon.map(beacon1 -> beacon1.getBeaconTimeStorage().getStats(requestDTO.getUuid())).orElse(new QueueTimeResponseDTO(null, null));
    }

    // TODO GET ALL STORES BY COORDINATE PROXIMITY
}
