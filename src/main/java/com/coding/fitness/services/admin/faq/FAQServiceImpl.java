package com.coding.fitness.services.admin.faq;

import com.coding.fitness.dtos.FAQDTO;
import com.coding.fitness.entity.FAQ;
import com.coding.fitness.entity.Product;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.repository.FAQRepository;
import com.coding.fitness.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{

    private final FAQRepository faqRepository;
    private final ProductRepository productRepository;
    private final Mapper mapper;

    public FAQDTO createFAQ(Long productId, FAQDTO faqdto){
        Optional.ofNullable(productId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid product id"));

        Optional.ofNullable(faqdto)
                .filter(faq-> !faq.getAnswer().isBlank() && !faq.getQuestion().isBlank())
                .orElseThrow(()-> new ValidationException("Invalid question or answer"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ValidationException("No Product Found"));

        FAQ faq = new FAQ();
        faq.setQuestion(faqdto.getQuestion());
        faq.setAnswer(faqdto.getAnswer());
        faq.setProduct(product);

         return mapper.getFAQDTO(faqRepository.save(faq));


    }
}
