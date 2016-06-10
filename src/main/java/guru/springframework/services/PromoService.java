package guru.springframework.services;


import guru.springframework.domain.Promotion;

public interface PromoService {
    Iterable<Promotion> listAllPromotions();

    Promotion getPromoById(Integer id);

    Promotion savePromo(Promotion promotion);
}
