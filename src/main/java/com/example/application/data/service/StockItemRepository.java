package com.example.application.data.service;

import com.example.application.data.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface StockItemRepository extends JpaRepository<StockItem, UUID> {

    @Query("SELECT i FROM StockItem i WHERE i.store.id = ?1 and i.product.id = ?2")
    StockItem findItemByStoreAndProduct(UUID storeId, UUID productId);
}