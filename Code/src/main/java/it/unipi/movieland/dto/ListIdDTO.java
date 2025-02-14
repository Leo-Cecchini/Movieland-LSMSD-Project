package it.unipi.movieland.dto;

import java.util.ArrayList;
import java.util.List;

public class ListIdDTO {
    private int id;

    private List<String> allIds;

    public int getId() {
        return id;
    }
    public void setId(int id) {}

    public List<String> getAllIds() {
        return allIds;
    }

    public void setAllIds(List<String> allIds) {
        this.allIds = allIds;
    }
    public ListIdDTO() {
        this.allIds = new ArrayList<String>();
    }
}
