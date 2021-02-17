package com.example.stock.stock.model;

public class ItemDB {
    private long itemNo;
    private String name;
    private long amount;
    private String inventoryCode;

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
}
