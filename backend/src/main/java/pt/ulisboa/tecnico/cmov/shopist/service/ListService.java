package pt.ulisboa.tecnico.cmov.shopist.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.InvalidDataException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListExistsException;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListNotFoundException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ListOfProducts;

import java.time.LocalDateTime;
import java.util.*;

import static pt.ulisboa.tecnico.cmov.shopist.util.ShopISTUtils.isEmpty;

@Data
@Service
public class ListService {
    private final Set<ListOfProducts> lists;

    public ListService() {
        this.lists = new HashSet<>();
    }

    public UUID createList(ListOfProducts list) throws InvalidDataException, ListExistsException {
        validateList(list);
        if (!lists.add(list)) {
            throw new ListExistsException("List already exists in server.");
        }
        list.setCreationDate(LocalDateTime.now());
        list.setUpdateDate(LocalDateTime.now());
        list.setUuid(UUID.randomUUID());
        // todo traverse products and get the product uuid from product service or create new one
        return list.getUuid();
    }

    public ListOfProducts updateList(ListOfProducts list) throws InvalidDataException, ListNotFoundException {
        validateList(list);
        Optional<ListOfProducts> listToUpdate = lists.stream().filter(list::equals).findAny();
        if (listToUpdate.isEmpty()) {
            throw new ListNotFoundException("Specified list was not found");
        } else {
            ListOfProducts updatedList = listToUpdate.get(); // TODO GET PRODUCTS FROM PRODUCT SERVICE, OR CREATE NEW PRODUCT
            updatedList.setUpdateDate(LocalDateTime.now());
            updatedList.setName(list.getName());
            updatedList.setCategory(list.getCategory());
            updatedList.setProducts(list.getProducts());
            return updatedList;
        }
    }

    public Optional<ListOfProducts> getListByUUID(String uuid) {
        if (uuid == null) return Optional.empty();
        return lists.stream().filter(list -> uuid.equals(list.getUuid().toString())).findAny();
    }

    public void validateList(ListOfProducts list) throws InvalidDataException {
        if (isEmpty(list.getName()) || isEmpty(list.getCategory()) || list.getProducts() == null || list.getProducts().isEmpty() ) {
            throw new InvalidDataException("List has invalid information");
        }
    }
}
