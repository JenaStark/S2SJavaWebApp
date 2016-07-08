package guru.springframework.repositories;

import guru.springframework.domain.PromotionStore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface PromoStoreRepository extends CrudRepository<PromotionStore, Integer>{

    List<PromotionStore> findByPromoID(Integer id);

    PromotionStore findFirstByPromoIDAndStoreID(Integer promoID, Integer storeID);

}
