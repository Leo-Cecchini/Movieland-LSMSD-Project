package it.unipi.movieland.controller.Movie;

import it.unipi.movieland.model.Movie.ProductionCountries;
import it.unipi.movieland.service.Movie.ProductionCountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/production-countries")
public class ProductionCountriesController {

    @Autowired
    private ProductionCountriesService productionCountriesService;

    // Endpoint per ottenere tutte le production countries
    @GetMapping
    public List<ProductionCountries> getAllProductionCountries() {
        return productionCountriesService.getAllProductionCountries();
    }

    // Endpoint per ottenere una production country tramite ID
    @GetMapping("/{countryId}")
    public ResponseEntity<ProductionCountries> getProductionCountryById(@PathVariable String countryId) {
        Optional<ProductionCountries> productionCountry = productionCountriesService.getProductionCountryById(countryId);
        return productionCountry.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere una nuova production country
    @PostMapping
    public ResponseEntity<ProductionCountries> addProductionCountry(@RequestBody ProductionCountries productionCountries) {
        ProductionCountries addedCountry = productionCountriesService.addProductionCountry(productionCountries);
        return ResponseEntity.ok(addedCountry);
    }

    // Endpoint per aggiornare una production country esistente
    @PutMapping("/{countryId}")
    public ResponseEntity<ProductionCountries> updateProductionCountry(
            @PathVariable String countryId,
            @RequestBody ProductionCountries updatedProductionCountries) {
        ProductionCountries updatedCountry = productionCountriesService.updateProductionCountry(countryId, updatedProductionCountries);
        return ResponseEntity.ok(updatedCountry);
    }

    // Endpoint per eliminare una production country tramite ID
    @DeleteMapping("/{countryId}")
    public ResponseEntity<Void> deleteProductionCountry(@PathVariable String countryId) {
        productionCountriesService.deleteProductionCountry(countryId);
        return ResponseEntity.noContent().build();
    }
}
