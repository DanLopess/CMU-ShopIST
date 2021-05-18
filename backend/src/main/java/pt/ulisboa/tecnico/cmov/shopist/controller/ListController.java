package pt.ulisboa.tecnico.cmov.shopist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.service.ListService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/pantry")
public class ListController {
    ListService listService;

    @Autowired
    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping
    @Operation(summary = "Create a list", description = "Return the created list UUID")
    public PantryDto createList(@RequestBody PantryDto list) throws InvalidDataException, ListExistsException {
        log.info("Received create list request: " + list.toString());
        return listService.createPantry(list);
    }

    @PutMapping
    @Operation(summary = "Update a list", description = "Returns the updated list")
    public PantryDto updateList(@RequestBody PantryDto list) throws InvalidDataException, ListNotFoundException {
        log.info("Received update list request: " + list.toString());
        return listService.updateList(list);
    }

    @GetMapping()
    @Operation(summary = "Find list by UUID", description = "Returns a list")
    public PantryDto getListByUuid(@Parameter(required = true) String uuid) {
        log.info("Received get list by uuid request. UUID: " + uuid);
        if (uuid != null) {
            String storeUUID = HtmlUtils.htmlEscape(uuid);
            Optional<PantryDto> list = listService.getListByUUID(storeUUID);
            return list.orElse(null);
        }
        return null;
    }
}
