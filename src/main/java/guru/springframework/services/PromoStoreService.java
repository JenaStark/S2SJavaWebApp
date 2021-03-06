package guru.springframework.services;


import guru.springframework.domain.PromotionStore;
import guru.springframework.domain.Store;
import java.util.List;

public interface PromoStoreService {
    Iterable<PromotionStore> listAllPromotionStores();

    PromotionStore getPromotionStoreById(Integer id);

    PromotionStore savePromotionStore(PromotionStore promotionStore);

    List<PromotionStore> findByPromoID(Integer id);

    PromotionStore findFirstByPromoIDAndStoreID(Integer promoID, Integer storeID);

    void delete(PromotionStore promotionStore);

    List<PromotionStore> findByStatus(String status);

    List<PromotionStore> findByTime(String time);

    Long countByStoreIDAndStatus(Integer storeID, String status);




}