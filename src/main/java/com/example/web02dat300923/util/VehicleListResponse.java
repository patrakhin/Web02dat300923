package com.example.web02dat300923.util;

import com.example.web02dat300923.dto.VehicleDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleListResponse {
    private List<VehicleDTO> content;
    private int totalPages;

    public List<VehicleDTO> getContent() {
        return content;
    }

    public void setContent(List<VehicleDTO> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
