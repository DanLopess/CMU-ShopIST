package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private final Set<ListOfProducts> lists;

    private ProductService productService;

    @Autowired
    public ListService(ProductService productService) {
        lists = new HashSet<>();
        this.productService = productService;
    }

    public ListService() {
        this.lists = new HashSet<>();
    }

    public String createList(ListOfProducts list) throws InvalidDataException, ListExistsException {
        validateList(list);
        if (!lists.add(list)) {
            throw new ListExistsException("List already exists in server.");
        }
        list.setCreationDate(LocalDateTime.now());
        list.setUpdateDate(LocalDateTime.now());
        list.setId(UUID.randomUUID().toString());
        addNewProductsToProductService(list.getProducts());
        return list.getId();
    }

    public ListOfProducts updateList(ListOfProducts list) throws InvalidDataException, ListNotFoundException {
        validateList(list);
        Optional<ListOfProducts> listToUpdate = lists.stream().filter(list::equals).findAny();
        if (listToUpdate.isEmpty()) {
            throw new ListNotFoundException("Specified list was not found");
        } else {
            ListOfProducts updatedList = listToUpdate.get();
            updatedList.setUpdateDate(LocalDateTime.now());
            updatedList.setName(list.getName());
            updatedList.setCategory(list.getCategory());
            updatedList.setProducts(list.getProducts());
            addNewProductsToProductService(updatedList.getProducts());
            return updatedList;
        }
    }

    public void addNewProductsToProductService(List<Product> products) {
        products.forEach(p -> {
            try {
                productService.addProduct(p);
            } catch (ProductExistsException ignored) { }
        });
    }

    public Optional<ListOfProducts> getListByUUID(String id) {
        if (id == null) return Optional.empty();
        return lists.stream().filter(list -> id.equals(list.getId())).findAny();
    }

    public void validateList(ListOfProducts list) throws InvalidDataException {
        if (isEmpty(list.getName()) || isEmpty(list.getCategory()) || list.getProducts() == null || list.getProducts().isEmpty() ) {
            throw new InvalidDataException("List has invalid information");
        }
    }
}
