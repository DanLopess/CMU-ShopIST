package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryProductDto;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static pt.ulisboa.tecnico.cmov.shopist.util.ShopISTUtils.isEmpty;

@Data
@Service
public class ListService {
    private final Set<PantryDto> pantries;

    private ProductService productService;

    @Autowired
    public ListService(ProductService productService) {
        pantries = new HashSet<>();
        this.productService = productService;
    }

    public ListService() {
        this.pantries = new HashSet<>();
    }

    public PantryDto createPantry(PantryDto pantryDto) throws InvalidDataException, ListExistsException {
        validatePantry(pantryDto);
        if (!pantries.add(pantryDto)) {
            throw new ListExistsException("Pantry already exists in server.");
        }
        pantryDto.setUuid(UUID.randomUUID().toString());
        for (PantryProductDto productDto : pantryDto.getProducts()) {
            if (productDto.getUuid() == null) {
                productDto.setUuid(UUID.randomUUID().toString());
            }
        }
        return pantryDto;
    }

    public PantryDto updateList(PantryDto pantryDto) throws InvalidDataException, ListNotFoundException {
        validatePantry(pantryDto);
        Optional<PantryDto> pantryToUpdate = pantries.stream().filter(pantryDto::equals).findAny();
        if (pantryToUpdate.isEmpty()) {
            throw new ListNotFoundException("Specified list was not found");
        } else {
            pantryToUpdate.get().update(pantryDto);
            return pantryToUpdate.get();
        }
    }

    public Optional<PantryDto> getListByUUID(String id) {
        if (id == null) return Optional.empty();
        return pantries.stream().filter(pantry -> id.equalsIgnoreCase(pantry.getUuid())).findAny();
    }

    public void validatePantry(PantryDto pantry) throws InvalidDataException {
        if (isEmpty(pantry.getName())) {
            throw new InvalidDataException("List has invalid information");
        }
    }
}
