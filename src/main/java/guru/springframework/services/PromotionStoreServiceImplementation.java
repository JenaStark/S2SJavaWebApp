package guru.springframework.services;

import guru.springframework.domain.PromotionStore;
import guru.springframework.repositories.PromoStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionStoreServiceImplementation implements PromoStoreService {
    private PromoStoreRepository promoStoreRepository;

    @Autowired
    public void setPromoStoreRepository(PromoStoreRepository promoStoreRepository) {
        this.promoStoreRepository = promoStoreRepository;
    }

    @Override
    public Iterable<PromotionStore> listAllPromotionStores() {
        return promoStoreRepository.findAll();
    }

    @Override
    public PromotionStore getPromotionStoreById(Integer id) {
        return promoStoreRepository.findOne(id);
    }

    @Override
    public PromotionStore savePromotionStore(PromotionStore promotionStore) {
        return promoStoreRepository.save(promotionStore);
    }

    public List<PromotionStore> findByPromoID(Integer id) {
        return promoStoreRepository.findByPromoID(id);
    }
}
