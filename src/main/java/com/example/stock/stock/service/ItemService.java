package com.example.stock.stock.service;

import com.example.stock.stock.model.Item;

import java.util.List;

public interface ItemService {
    Item addItemToStock(Item item);
    List<Item> getInventory();
    Item getItemById(long itemId);
    Item withdrawal(long itemId, long amount);
    Item deposit(long itemId, long amount);
    void deleteItem(long itemId);
}
