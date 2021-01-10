package com.nim_2201744304.binusezyfoody;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String type;
    private Integer price;
    private  Integer stock=0;
    private  Integer orderQuantity=0;

    public Item() {
    }

    public Item(String name, String type, int price, int stock, int orderQuantity) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.stock = stock;
        this.orderQuantity = orderQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
