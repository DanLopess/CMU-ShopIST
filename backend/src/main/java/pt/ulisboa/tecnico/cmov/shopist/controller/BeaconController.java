package pt.ulisboa.tecnico.cmov.shopist.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.cmov.shopist.dto.InTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.OutTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.QueueTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.QueueTimeResponseDTO;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreExistsException;
import pt.ulisboa.tecnico.cmov.shopist.dto.Beacon;
import pt.ulisboa.tecnico.cmov.shopist.service.BeaconService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/beacon")
public class BeaconController {
    BeaconService beaconService;

    @Autowired
    public BeaconController(BeaconService beaconService) {
        this.beaconService = beaconService;
    }

    @PostMapping
    @Operation(summary = "Create a beacon", description = "Return the created store")
    public Beacon createBeacon(@RequestBody Beacon beacon) throws InvalidDataException, StoreExistsException {
        return beaconService.createBeacon(beacon);
    }

    @PostMapping(path = "/in")
    @Operation(summary = "save the time the user got in beacon's range", description = "Return UUID")
    public UUID postInTime(@RequestBody InTimeRequestDTO time) throws InvalidDataException {
        return beaconService.inTime(time);
    }

    @PostMapping(path = "/out")
    @Operation(summary = "save the time the user got out beacon's range", description = "Return OK")
    public UUID postOutTime(@RequestBody OutTimeRequestDTO time) throws InvalidDataException {
        return beaconService.outTime(time);
    }

    @PostMapping(path = "/queueTime")
    @Operation(summary = "get queue's mean duration time of last 1 hour")
    public QueueTimeResponseDTO getMeanDurationTime(@RequestBody QueueTimeRequestDTO requestDTO) {
        return beaconService.getMeanDurationLast1Hour(requestDTO);
    }
}
