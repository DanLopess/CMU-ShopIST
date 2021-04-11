package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.StoreNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Store;

import java.util.*;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.cmov.shopist.util.ShopISTUtils.isEmpty;

@Slf4j
@Service
public class StoreService {
    private final Set<Store> stores;

    public StoreService() {
        this.stores = new HashSet<>();
    }

    public Store createStore(Store s) throws StoreExistsException, InvalidDataException {
        validateStoreData(s);
        if (!stores.add(s)) {
            throw new StoreExistsException("Store already exists in server.");
        }
        s.setUuid(UUID.randomUUID());
        return s;
    }

    public Store updateStore(Store s) throws StoreNotFoundException, InvalidDataException {
        validateStoreData(s);
        Optional<Store> storeToUpdate = stores.stream().filter(s::equals).findAny();
        if (storeToUpdate.isEmpty()) {
            throw new StoreNotFoundException("Specified store was not found");
        } else {
            Store updatedStore = storeToUpdate.get();
            updatedStore.setName(s.getName());
            updatedStore.setCoordinates(s.getCoordinates());
            return s;
        }
    }

    public List<Store> getListOfStoresByName(String name) {
        if (name == null) return new ArrayList<>();
        return stores.stream().filter(store -> name.equals(store.getName())).collect(Collectors.toList());
    }

    public Optional<Store> getStoreByUUID(String uuid) {
        if (uuid == null) return Optional.empty();
        return stores.stream().filter(store -> uuid.equals(store.getUuid().toString())).findAny();
    }

    public Optional<Store> getStoreByCoordinates(String coordinates) {
        if (coordinates == null) return Optional.empty();
        return stores.stream().filter(store -> coordinates.equals(store.getCoordinates())).findAny();
    }

    private void validateStoreData(Store s) throws InvalidDataException {
        if (isEmpty(s.getName()) || isEmpty(s.getCoordinates())) {
            throw new InvalidDataException("Store has invalid information");
        }
    }

    // TODO GET ALL STORES BY COORDINATE PROXIMITY
}
