package pt.ulisboa.tecnico.cmov.shopist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.cmov.shopist.pojo.ListOfProducts;
import pt.ulisboa.tecnico.cmov.shopist.service.ListService;

import java.util.List;

@RestController
@RequestMapping(path = "/lists")
public class ListController {
    ListService listService;

    @Autowired
    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping
    public List<ListOfProducts> getAllLists() {
        return listService.getLists();
    }
}
