package com.example.stock.stock.service;

import com.example.stock.stock.exception.ResourceNotFoundException;
import com.example.stock.stock.model.Item;
import com.example.stock.stock.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item addItemToStock(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    @Override
    public Item getItemById(long itemId) {
        Optional<Item> itemDb = this.itemRepository.findById(itemId);

        if (itemDb.isPresent()) {
            return itemDb.get();
        }
        else {
            throw new ResourceNotFoundException("Record not found with id : " + itemId);
        }

    }

    @Override
    public Item withdrawal(long itemId, long amount) {
        Optional<Item> itemDb = this.itemRepository.findById(itemId);

        if (itemDb.isPresent()){
            Item itemUpdate = itemDb.get();
            long currentAmount = itemUpdate.getAmount();
            long updatedAmount = currentAmount - amount;
            if (updatedAmount >= 0) {
                itemUpdate.setAmount(updatedAmount);
            }
            this.itemRepository.save(itemUpdate);
            return itemUpdate;
        }
        else {
            throw new ResourceNotFoundException("Record not found with id : " + itemId);
        }
    }

    @Override
    public Item deposit(long itemId, long amount) {
        Optional<Item> itemDb = this.itemRepository.findById(itemId);

        if (itemDb.isPresent() && amount > 0){
            Item itemUpdate = itemDb.get();
            itemUpdate.setAmount(itemUpdate.getAmount() + amount);
            this.itemRepository.save(itemUpdate);
            return itemUpdate;
        }
        else {
            throw new ResourceNotFoundException("Record not found with id : " + itemId);
        }
    }

    @Override
    public void deleteItem(long itemId) {
        Optional<Item> itemDb = this.itemRepository.findById(itemId);

        if (itemDb.isPresent()){
            this.itemRepository.delete(itemDb.get());
        } else {
            throw new ResourceNotFoundException("Record not found with id : " + itemId);
        }

    }
}
