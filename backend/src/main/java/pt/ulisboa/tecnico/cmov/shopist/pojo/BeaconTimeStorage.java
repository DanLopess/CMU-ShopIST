package pt.ulisboa.tecnico.cmov.shopist.pojo;

import pt.ulisboa.tecnico.cmov.shopist.dto.QueueTimeResponseDTO;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class BeaconTimeStorage {
    private final Map<UUID, BeaconTimes> storage = new HashMap<>();

    public void saveInTime(UUID uuid, LocalDateTime timestamp) throws InvalidDataException {
        if(isValidIn(timestamp)) {
            storage.put(uuid, new BeaconTimes(timestamp, null));
            return;
        }
        throw new InvalidDataException("there was an error in in time");
    }

    public void saveOutTime(UUID uuid, LocalDateTime timestamp) throws InvalidDataException {
        if(storage.containsKey(uuid) && storage.get(uuid) != null) {
            BeaconTimes times = storage.get(uuid);
            if(isValidOut(times, timestamp)) {
                times.setOut(timestamp);
                return;
            }
        }
        throw new InvalidDataException("there was an error in out time");
    }

    private boolean isValidIn(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(timestamp)) {
            return false;
        }
        return true;
    }

    private boolean isValidOut(BeaconTimes times, LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(timestamp) || timestamp.isBefore(times.getIn())) {
            return false;
        }
        return true;
    }

    private Integer getMeanDurationLast1Hour() {
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        int sum = 0;
        Set<UUID> keySet = storage.keySet();
        for(UUID uuid : keySet) {
            LocalDateTime out = storage.get(uuid).getOut();
            if(out != null && differenceInSeconds(now, out) <= 3600) {
                sum += durationInSeconds(uuid);
                count += 1;
            }
        }
        return count != 0 ? sum / count : 0;

    }

    public QueueTimeResponseDTO getStats(UUID uuid) {
        Integer mean = getMeanDurationLast1Hour();
        Integer estimationTime = null;
        if(uuid != null) {
            estimationTime = getEstimation(mean, uuid);
        }
        return new QueueTimeResponseDTO(mean, estimationTime);
    }

    private Integer getEstimation(int mean, UUID uuid) {
        LocalDateTime now = LocalDateTime.now();
        BeaconTimes times = storage.get(uuid);
        if(times == null || times.getOut() != null) {
            return null;
        }
        LocalDateTime in = times.getIn();
        int res = Math.toIntExact(mean - differenceInSeconds(now, in));
        return Math.max(res, 0);
    }

    private long getMeanDuration() {
        long sum = storage.keySet().stream().mapToLong(this::durationInSeconds).sum();
        return sum / storage.size();
    }

    private long durationInSeconds(UUID uuid) {
        BeaconTimes times = storage.get(uuid);
        return differenceInSeconds(times.getOut(), times.getIn());
    }

    public boolean hasStorageUuid(UUID uuid) {
        return storage.containsKey(uuid);
    }

    private long differenceInSeconds(LocalDateTime t2, LocalDateTime t1) {
        return ChronoUnit.SECONDS.between(t1, t2);
    }
}
