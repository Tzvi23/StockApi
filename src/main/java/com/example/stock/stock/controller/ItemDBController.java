package com.example.stock.stock.controller;

import com.example.stock.stock.model.ItemDB;
import com.example.stock.stock.service.ItemDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ItemDBController {
    @Autowired
    ItemDBService itemDBService;

    @PostMapping("/addItemDB")
    public String addItemDB(@RequestBody ItemDB item) throws InterruptedException, ExecutionException {
        return itemDBService.addItemDB(item);
    }

    @GetMapping("/getItemDB")
    public ItemDB getItemDB(@RequestParam long itemDBNo ) throws InterruptedException, ExecutionException {
        return itemDBService.getItemDB(itemDBNo);
    }

    @GetMapping("/getInventoryDB")
    public List<ItemDB> getInventoryDB( ) throws InterruptedException, ExecutionException {
        return  itemDBService.getInventoryDB();
    }

    @PutMapping("/depositDB")
    public ItemDB depositDB(@RequestParam long itemNo, @RequestParam long amount) throws ExecutionException, InterruptedException {
        return itemDBService.depositDB(itemNo,amount);
    }

    @PutMapping("/withdrawDB")
    public ItemDB withdrawDB(@RequestParam long itemNo, @RequestParam long amount) throws ExecutionException, InterruptedException {
        return itemDBService.withdrawDB(itemNo,amount);
    }

    @DeleteMapping("/removeItemDB")
    public String removeItemDB(long itemDBNo){
        return itemDBService.RemoveItemDB(itemDBNo);
    }
}
