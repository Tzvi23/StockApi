package com.example.stock.stock.repository;

import com.example.stock.stock.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository  extends JpaRepository<Item, Long> {
}
