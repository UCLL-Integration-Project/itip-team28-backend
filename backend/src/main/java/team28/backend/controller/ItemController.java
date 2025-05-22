package team28.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import team28.backend.controller.dto.ItemInput;
import team28.backend.model.Item;
import team28.backend.service.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Get all items")
    @ApiResponse(responseCode = "200", description = "List of items returned successfully")
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @Operation(summary = "Create new item")
    @ApiResponse(responseCode = "200", description = "Item was successfully created")
    @PostMapping
    public Item createItem(@RequestBody @Valid ItemInput itemInput) {
        return itemService.createItem(itemInput.name());
    }
}


