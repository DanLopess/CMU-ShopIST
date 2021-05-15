package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ProductExistsException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ListOfProducts;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;

import java.time.LocalDateTime;
import java.util.*;

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

    public String createPantry(PantryDto pantryDto) throws InvalidDataException, ListExistsException {
        validatePantry(pantryDto);
        if (!pantries.add(pantryDto)) {
            throw new ListExistsException("Pantry already exists in server.");
        }
        pantryDto.setUuid(UUID.randomUUID().toString());
        return pantryDto.getUuid();
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

    public void addNewProductsToProductService(List<Product> products) {
        products.forEach(p -> {
            try {
                productService.addProduct(p);
            } catch (ProductExistsException ignored) { }
        });
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
