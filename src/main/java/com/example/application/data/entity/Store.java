package com.example.application.data.entity;

import javax.annotation.Nonnull;
import javax.persistence.Entity;

@Entity
public class Store extends AbstractEntity {
    @Nonnull
    private String name;

    private String address;

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
