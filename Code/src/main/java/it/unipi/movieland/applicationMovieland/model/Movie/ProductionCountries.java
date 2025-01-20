package it.unipi.movieland.applicationMovieland.model.Movie;

import java.util.List;

public class ProductionCountries {
    private List<String> countryList;

    public ProductionCountries(List<String> countryList) {
        this.countryList = countryList;
    }

    public List<String> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<String> countryList) {
        this.countryList = countryList;
    }

    @Override
    public String toString() {
        return "ProductionCountries{" +
                "countryList=" + countryList +
                '}';
    }
}