package ru.services.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.services.store.mapper.ProductMapper;
import ru.services.store.model.ProductResponse;
import ru.services.store.service.StoreService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/store/v1")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final ProductMapper mapper;

    @Operation(summary = "Get all product list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products info")
    })
    @GetMapping("/items")
    public List<ProductResponse> get(@RequestParam(value = "name_like", required = false) String name) {
        return storeService.getProductsList(name).stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}