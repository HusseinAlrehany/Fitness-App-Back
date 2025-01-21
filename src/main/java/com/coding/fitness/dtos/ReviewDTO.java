package com.coding.fitness.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewDTO {

    private Long id;
    private Long rating;
    private String description;
    private MultipartFile img;
    private byte[] returnedImg;
    private Long userId;
    private Long productId;
    private String userName;
}
