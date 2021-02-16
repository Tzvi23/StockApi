package com.example.stock.stock.controller;

import com.example.stock.stock.model.Item;
import com.example.stock.stock.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/inventory")
    public ResponseEntity<List<Item>> getInventory() {
        return ResponseEntity.ok().body(itemService.getInventory());
    }

    @GetMapping("/getItem/{itemNo}")
    public ResponseEntity<Item> getItemDetails(@PathVariable long itemNo){
        return ResponseEntity.ok().body(itemService.getItemById(itemNo));
    }

    @PutMapping("/withdraw/{itemNo}/{amount}")
    public ResponseEntity<Item> withdraw(@PathVariable long itemNo, @PathVariable long amount){
        return ResponseEntity.ok().body(this.itemService.withdrawal(itemNo,amount));
    }

    @PutMapping("/deposit/{itemNo}/{amount}")
    public ResponseEntity<Item> deposit(@PathVariable long itemNo, @PathVariable long amount){
        return ResponseEntity.ok().body(this.itemService.deposit(itemNo,amount));
    }

    @PostMapping("/addItem")
    public ResponseEntity<Item> addItem(@RequestBody Item item){
        return ResponseEntity.status(201).body(this.itemService.addItemToStock(item));
    }

    @DeleteMapping("/removeItem/{itemNo}")
    public HttpStatus removeItem (@PathVariable long itemNo){
        this.itemService.deleteItem(itemNo);
        return HttpStatus.OK;
    }
}
