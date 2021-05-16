package pt.ulisboa.tecnico.cmov.shopist.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import pt.ulisboa.tecnico.cmov.shopist.dto.InTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.OutTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.dto.QueueTimeResponseDTO;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.BeaconTime;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Beacon;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Coordinates;
import pt.ulisboa.tecnico.cmov.shopist.service.BeaconService;

import java.util.List;
import java.util.Optional;
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

    @PutMapping
    @Operation(summary = "Update a beacon", description = "Returns the updated store")
    public Beacon updateStore(@RequestBody Beacon beacon) throws InvalidDataException, StoreNotFoundException {
        return beaconService.updateBeacon(beacon);
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

    @GetMapping(path = "/queueTime")
    @Operation(summary = "get queue's mean duration time of last 1 hour")
    public QueueTimeResponseDTO getMeanDurationTime(@RequestBody Coordinates coordinates) {
        return beaconService.getMeanDurationLast1Hour(coordinates);
    }
}
