package com.coding.fitness.services.admin.faq;

import com.coding.fitness.dtos.FAQDTO;

public interface FAQService {

    FAQDTO createFAQ(Long productId, FAQDTO faqdto);
}
