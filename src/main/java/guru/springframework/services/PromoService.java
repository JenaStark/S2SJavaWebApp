package guru.springframework.services;


import guru.springframework.domain.Promotion;

import java.util.List;

public interface PromoService {
    Iterable<Promotion> listAllPromotions();

    Promotion getPromoById(Integer id);

    Promotion savePromo(Promotion promotion);

    List<Promotion> findById(Integer id);

}
