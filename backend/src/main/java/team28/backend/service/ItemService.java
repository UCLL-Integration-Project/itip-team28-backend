package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Item;
import team28.backend.repository.ItemRepository;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item createItem(String name) {
        if (itemRepository.existsByName(name)) {
            throw new ServiceException("Item with name " + name + "already exists");
        }

        Item NewItem = new Item(name);

        return itemRepository.save(NewItem);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Item with id " + id + " not found"));
    }
}
