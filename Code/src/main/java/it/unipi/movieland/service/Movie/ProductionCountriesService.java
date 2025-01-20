package it.unipi.movieland.service.Movie;

import it.unipi.movieland.model.Movie.ProductionCountries;
import it.unipi.movieland.repository.Movie.ProductionCountriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionCountriesService {

    @Autowired
    private ProductionCountriesRepository productionCountriesRepository;

    // Ottieni tutte le production countries
    public List<ProductionCountries> getAllProductionCountries() {
        return productionCountriesRepository.getAllProductionCountries();
    }

    // Ottieni una production country tramite ID
    public Optional<ProductionCountries> getProductionCountryById(String countryId) {
        return productionCountriesRepository.getProductionCountryById(countryId);
    }

    // Aggiungi una nuova production country
    public ProductionCountries addProductionCountry(ProductionCountries productionCountries) {
        return productionCountriesRepository.addProductionCountry(productionCountries);
    }

    // Modifica una production country esistente
    public ProductionCountries updateProductionCountry(String countryId, ProductionCountries updatedProductionCountries) {
        return productionCountriesRepository.updateProductionCountry(countryId, updatedProductionCountries);
    }

    // Elimina una production country tramite ID
    public void deleteProductionCountry(String countryId) {
        productionCountriesRepository.deleteProductionCountry(countryId);
    }
}
