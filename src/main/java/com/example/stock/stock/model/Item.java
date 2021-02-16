package com.example.stock.stock.model;

import javax.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemNo;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private long amount;

    @Column(name = "inventoryCode", unique = true)
    private String inventoryCode;

    public Item() {
    }

    public Item(long itemNo, String name, long amount, String inventoryCode) {
        this.itemNo = itemNo;
        this.name = name;
        this.amount = amount;
        this.inventoryCode = inventoryCode;
    }

    public long getItemNo() {
        return itemNo;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setItemNo(long itemNo) {
        this.itemNo = itemNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }
}
