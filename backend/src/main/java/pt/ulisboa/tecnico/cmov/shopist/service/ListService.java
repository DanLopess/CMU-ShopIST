package pt.ulisboa.tecnico.cmov.shopist.service;

import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.cmov.shopist.exceptions.ListExistsException;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ListOfProducts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ListService {
    private Set<ListOfProducts> lists;

    public ListService() {
        this.lists = new HashSet<>();
    }

    public void addListOfProducts(ListOfProducts list) throws ListExistsException {
        if (!lists.add(list)) {
            throw new ListExistsException("List already exists in server.");
        }
    }

    public List<ListOfProducts> getLists(){
        return new ArrayList<>(lists);
    }
}
