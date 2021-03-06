package guru.springframework.services;


import guru.springframework.domain.Promotion;

import java.util.List;

public interface PromoService {
    Iterable<Promotion> listAllPromotions();

    Promotion getPromoById(Integer id);

    Promotion savePromo(Promotion promotion);

    List<Promotion> findById(Integer id);

    void delete(Promotion promotion);

    Long count();

    List<Promotion> findTop3ByOrderByPostedDesc();

    List<Promotion> findByPostedString(String string);

}
