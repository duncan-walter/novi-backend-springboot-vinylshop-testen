package walter.duncan.vinylwebshop.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.dtos.stock.StockRequestDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.helpers.UrlHelper;
import walter.duncan.vinylwebshop.services.StockService;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {
    private final UrlHelper urlHelper;
    private final StockService stockService;

    public StockController(UrlHelper urlHelper, StockService stockService) {
        this.urlHelper = urlHelper;
        this.stockService = stockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDto> getStockById(@PathVariable Long id) {
        return ResponseEntity.ok(this.stockService.findStockById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockResponseDto>> getStocks() {
        return ResponseEntity.ok(this.stockService.findAllStocks());
    }

    @PostMapping
    public ResponseEntity<StockResponseDto> createStock(@RequestBody @Valid StockRequestDto stockRequestDto) {
        var stockResponseDto = this.stockService.createStock(stockRequestDto);
        var location = this.urlHelper.getResourceUri(0L);

        return ResponseEntity
                .created(location)
                .body(stockResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDto> updateStock(@PathVariable Long id, @RequestBody @Valid StockRequestDto stockRequestDto) {
        var stockResponseDto = this.stockService.updateStock(id, stockRequestDto);
        var location = this.urlHelper.getResourceUri(0L);

        return ResponseEntity
                .ok()
                .location(location)
                .body(stockResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStock(@PathVariable Long id) {
        this.stockService.deleteStock(id);

        return ResponseEntity.noContent().build();
    }
}