package controller;

import dto.OfferRequest;
import dto.OfferResponse;
import logging.LoggerService;
import model.Offer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import patterns.builder.OfferBuilder;
import service.OfferService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offers")
public class OfferRestController {

    private final OfferService offerService;
    private final LoggerService logger = LoggerService.getInstance();

    public OfferRestController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public List<OfferResponse> getAll() {
        logger.info("GET /api/offers");
        return offerService.getAllOffers()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/active")
    public List<OfferResponse> getActive() {
        logger.info("GET /api/offers/active");
        return offerService.getActiveOffers()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OfferResponse getById(@PathVariable int id) {
        logger.info("GET /api/offers/" + id);
        return toResponse(offerService.getOfferById(id));
    }

    @GetMapping("/by-food/{foodItemId}")
    public List<OfferResponse> getByFoodItem(@PathVariable int foodItemId) {
        logger.info("GET /api/offers/by-food/" + foodItemId);
        return offerService.getOffersByFoodItemId(foodItemId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<OfferResponse> create(@RequestBody @Validated OfferRequest request) {
        logger.info("POST /api/offers for foodItemId=" + request.getFoodItemId());
        Offer offer = new OfferBuilder()
                .foodItemId(request.getFoodItemId())
                .discountPercentage(request.getDiscountPercentage())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(true)
                .build();

        offerService.addOffer(offer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(offer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferResponse> update(@PathVariable int id,
                                                @RequestBody @Validated OfferRequest request) {
        logger.info("PUT /api/offers/" + id);
        Offer existing = offerService.getOfferById(id);
        Offer updated = OfferBuilder.fromExisting(existing)
                .foodItemId(request.getFoodItemId())
                .discountPercentage(request.getDiscountPercentage())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        offerService.updateOffer(updated);
        return ResponseEntity.ok(toResponse(updated));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        logger.info("POST /api/offers/" + id + "/deactivate");
        offerService.deactivateOffer(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        logger.info("DELETE /api/offers/" + id);
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    private OfferResponse toResponse(Offer offer) {
        OfferResponse resp = new OfferResponse();
        resp.setId(offer.getId());
        resp.setFoodItemId(offer.getFoodItemId());
        resp.setDiscountPercentage(offer.getDiscountPercentage());
        resp.setDescription(offer.getDescription());
        resp.setStartDate(offer.getStartDate());
        resp.setEndDate(offer.getEndDate());
        resp.setActive(offer.isActive());
        return resp;
    }
}

