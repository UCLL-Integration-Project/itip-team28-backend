package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Item;
import team28.backend.repository.ItemRepository;

@Service
public class ItemService {
    private final ItemRepository ItemRepository;

    public ItemService(ItemRepository ItemRepository) {
        this.ItemRepository = ItemRepository;
    }

    public List<Item> getAllItems() {
        return ItemRepository.findAll();
    }

    public Item createItem(String name) {
        String normalizedName = name.toLowerCase();
        if (ItemRepository.existsByName(normalizedName)) {
            throw new ServiceException("Item with name " + normalizedName + " already exists");
        }

        Item NewItem = new Item(normalizedName);

        return ItemRepository.save(NewItem);
    }

    public Item getItemById(Long id) {
        return ItemRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Item with id " + id + " not found"));
    }
}
