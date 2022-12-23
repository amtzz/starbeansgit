package com.example.application.data.entity;

import javax.annotation.Nonnull;
import javax.persistence.Entity;

@Entity
public class Product extends AbstractEntity {

    @Nonnull
    private String name;

    @Nonnull
    private double price;

    @Nonnull
    private int minimumStock;

    @Nonnull
    private int replenishAmount;

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public int getReplenishAmount() {
        return replenishAmount;
    }

    public void setReplenishAmount(int replenishAmount) {
        this.replenishAmount = replenishAmount;
    }
}
