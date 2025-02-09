package it.unipi.movieland.DTO;

public class CombinedPercentageDTO {
    private String label;
    private int movieCount;
    private double combinedPercentage;

    public CombinedPercentageDTO(String label, int movieCount, double combinedPercentage) {
        this.label = label;
        this.movieCount = movieCount;
        this.combinedPercentage = combinedPercentage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public double getCombinedPercentage() {
        return combinedPercentage;
    }

    public void setCombinedPercentage(double combinedPercentage) {
        this.combinedPercentage = combinedPercentage;
    }
}
