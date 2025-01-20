package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.ProductionCountries;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductionCountriesRepository {

    private List<ProductionCountries> productionCountriesList = new ArrayList<>();

    // Ottieni tutte le production countries
    public List<ProductionCountries> getAllProductionCountries() {
        return productionCountriesList;
    }

    // Ottieni una production country tramite un ID (ad esempio, tramite uno dei paesi)
    public Optional<ProductionCountries> getProductionCountryById(String countryId) {
        return productionCountriesList.stream()
                .filter(productionCountries -> productionCountries.getCountryList().contains(countryId))
                .findFirst();
    }

    // Aggiungi una nuova produzione country
    public ProductionCountries addProductionCountry(ProductionCountries productionCountries) {
        productionCountriesList.add(productionCountries);
        return productionCountries;
    }

    // Modifica una production country esistente
    public ProductionCountries updateProductionCountry(String countryId, ProductionCountries updatedProductionCountries) {
        ProductionCountries existingProductionCountries = getProductionCountryById(countryId)
                .orElseThrow(() -> new RuntimeException("Production country not found"));
        existingProductionCountries.setCountryList(updatedProductionCountries.getCountryList());
        return existingProductionCountries;
    }

    // Elimina una production country tramite ID
    public void deleteProductionCountry(String countryId) {
        ProductionCountries productionCountries = getProductionCountryById(countryId)
                .orElseThrow(() -> new RuntimeException("Production country not found"));
        productionCountriesList.remove(productionCountries);
    }
}