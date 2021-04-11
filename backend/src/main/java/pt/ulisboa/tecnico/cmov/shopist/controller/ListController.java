package pt.ulisboa.tecnico.cmov.shopist.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ListOfProducts;
import pt.ulisboa.tecnico.cmov.shopist.service.ListService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/list")
public class ListController {
    ListService listService;

    @Autowired
    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping
    @Operation(summary = "Create a list", description = "Return the created list UUID")
    public UUID createList(@RequestBody ListOfProducts list) throws InvalidDataException, ListExistsException {
        return listService.createList(list);
    }

    @PutMapping
    @Operation(summary = "Update a list", description = "Returns the updated list")
    public ListOfProducts updateList(@RequestBody ListOfProducts list) throws InvalidDataException, ListNotFoundException {
        return listService.updateList(list);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Find list by UUID", description = "Returns a list")
    public ListOfProducts getListByUuid(@PathVariable("uuid") String uuid) {
        if (uuid != null) {
            String storeUUID = HtmlUtils.htmlEscape(uuid);
            Optional<ListOfProducts> list = listService.getListByUUID(storeUUID);
            return list.orElse(null);
        }
        return null;
    }
}
