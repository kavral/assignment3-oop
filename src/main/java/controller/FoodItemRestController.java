package controller;

import dto.FoodItemRequest;
import dto.FoodItemResponse;
import logging.LoggerService;
import model.FoodItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import patterns.factory.FoodItemFactory;
import service.FoodItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemRestController {

    private final FoodItemService foodItemService;
    private final FoodItemFactory foodItemFactory = new FoodItemFactory();
    private final LoggerService logger = LoggerService.getInstance();

    public FoodItemRestController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping
    public List<FoodItemResponse> getAll() {
        logger.info("GET /api/food-items");
        return foodItemService.getAllFoodItems()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FoodItemResponse getById(@PathVariable int id) {
        logger.info("GET /api/food-items/" + id);
        return toResponse(foodItemService.getFoodItemById(id));
    }

    @PostMapping
    public ResponseEntity<FoodItemResponse> create(@RequestBody @Validated FoodItemRequest request) {
        logger.info("POST /api/food-items name=" + request.getName());
        FoodItem item = foodItemFactory.create(request);
        FoodItem saved = foodItemService.addFoodItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable int id,
                                            @RequestParam("price") double newPrice) {
        logger.info("PUT /api/food-items/" + id + "/price");
        FoodItem existing = foodItemService.getFoodItemById(id);
        foodItemService.updatePrice(existing.getName(), newPrice);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        logger.info("DELETE /api/food-items/" + id);
        foodItemService.deleteFoodItemById(id);
        return ResponseEntity.noContent().build();
    }

    private FoodItemResponse toResponse(FoodItem item) {
        FoodItemResponse resp = new FoodItemResponse();
        resp.setId(item.getId());
        resp.setName(item.getName());
        resp.setPrice(item.getPrice());
        resp.setDescription(item.getDescription());
        return resp;
    }
}

