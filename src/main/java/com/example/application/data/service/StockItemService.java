package com.example.application.data.service;

import com.example.application.data.entity.StockItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StockItemService {

    private final StockItemRepository repository;

    @Autowired
    public StockItemService(StockItemRepository repository) {
        this.repository = repository;
    }

    public StockItem addToStock(StockItem entity) {
        StockItem entityToSave = repository.findItemByStoreAndProduct(entity.getStore().getId(), entity.getProduct().getId());

        if (entityToSave == null) {
            entity.setId(UUID.randomUUID());
            entityToSave = entity;
        } else {
            entityToSave.setAmount(entityToSave.getAmount() + entity.getAmount());
        }
        return repository.save(entityToSave);
    }

    public Optional<StockItem> get(UUID id) {
        return repository.findById(id);
    }

    public StockItem update(StockItem entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<StockItem> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
