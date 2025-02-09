package it.unipi.movieland.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringCountDTO {

    private String label;
    private int count;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
