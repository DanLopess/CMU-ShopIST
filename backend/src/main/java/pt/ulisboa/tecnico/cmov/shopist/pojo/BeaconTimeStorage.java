package pt.ulisboa.tecnico.cmov.shopist.pojo;

import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;

import java.sql.Timestamp;
import java.util.*;


public class BeaconTimeStorage {
    private final Map<UUID, BeaconTimes> storage = new HashMap<>();

    public void saveInTime(UUID uuid, Timestamp timestamp) throws InvalidDataException {
        if(isValidIn(timestamp)) {
            storage.put(uuid, new BeaconTimes(timestamp, null));
            return;
        }
        throw new InvalidDataException("there was an error in in time");
    }

    public void saveOutTime(UUID uuid, Timestamp timestamp) throws InvalidDataException {
        if(storage.containsKey(uuid) && storage.get(uuid) != null) {
            BeaconTimes times = storage.get(uuid);
            if(isValidOut(times, timestamp)) {
                times.setOut(timestamp);
                return;
            }
        }
        throw new InvalidDataException("there was an error in out time");
    }

    private boolean isValidIn(Timestamp timestamp) {
        Timestamp now = new Timestamp(new Date().getTime());
        if(now.before(timestamp)) {
            return false;
        }
        return true;
    }

    private boolean isValidOut(BeaconTimes times, Timestamp timestamp) {
        Timestamp now = new Timestamp(new Date().getTime());
        if(now.before(timestamp) || timestamp.before(times.getIn())) {
            return false;
        }
        return true;
    }

    public long getMeanDurationLast1Hour() {
        Timestamp now = new Timestamp(new Date().getTime());
        long count = 0;
        long sum = 0;
        Set<UUID> keySet = storage.keySet();
        for(UUID uuid : keySet) {
            Timestamp out = storage.get(uuid).getOut();
            if(out != null && differenceInSeconds(now, out) <= 3600) {
                sum += durationInSeconds(uuid);
                count += 1;
            }
        }
        return count != 0 ? sum / count : 0;
    }

    private long getMeanDuration() {
        long sum = storage.keySet().stream().mapToLong(this::durationInSeconds).sum();
        return sum / storage.size();
    }

    private long durationInSeconds(UUID uuid) {
        BeaconTimes times = storage.get(uuid);
        return differenceInSeconds(times.getOut(), times.getIn());
    }

    private long differenceInSeconds(Timestamp t2, Timestamp t1) {
        return (t2.getTime() - t1.getTime()) / 1000;
    }
}
