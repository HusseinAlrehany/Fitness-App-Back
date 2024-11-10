package com.coding.fitness.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDTO {

    private Long id;

    private String name;
    private Long price;

    private String description;


    private byte[] byteImg;

    //to link the product to the category
    private Long categoryId;
    private String categoryName;

    //to get the image as multipart from the front end
    //then return it as byte array
    private MultipartFile img;

}
