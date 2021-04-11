package pt.ulisboa.tecnico.cmov.shopist.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Store;
import pt.ulisboa.tecnico.cmov.shopist.service.StoreService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/store")
public class StoreController {
    StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    @Operation(summary = "Create a store", description = "Return the created store")
    public Store createStore(@RequestBody Store store) throws InvalidDataException, StoreExistsException {
        return storeService.createStore(store);
    }

    @PutMapping
    @Operation(summary = "Update a store", description = "Returns the updated store")
    public Store updateStore(@RequestBody Store store) throws InvalidDataException, StoreNotFoundException {
        return storeService.updateStore(store);
    }

    @GetMapping(path = "/all")
    @Operation(summary = "Find all stores by parameters", description = "Returns all stores with that name")
    public List<Store> getStoresByName(@RequestParam String name) {
        String storeName = HtmlUtils.htmlEscape(name);
        return storeService.getListOfStoresByName(storeName);
    }

    @GetMapping
    @Operation(summary = "Find store by parameters", description = "Returns a single store")
    public Store getSingleStore(@RequestParam(required = false) String uuid, @RequestParam(required = false) String coordinates) {
        if (uuid != null) {
            String storeUUID = HtmlUtils.htmlEscape(uuid);
            Optional<Store> store = storeService.getStoreByUUID(storeUUID);
            return store.orElse(null);
        } else if (coordinates != null) {
            String storeCoordinates = HtmlUtils.htmlEscape(coordinates);
            Optional<Store> store = storeService.getStoreByCoordinates(storeCoordinates);
            return store.orElse(null);
        }
        return null;
    }
}
