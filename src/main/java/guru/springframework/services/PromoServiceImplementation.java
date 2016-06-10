package guru.springframework.services;

import guru.springframework.domain.Promotion;
import guru.springframework.repositories.PromoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImplementation implements PromoService {
    private PromoRepository promoRepository;

    @Autowired
    public void setPromoRepository(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @Override
    public Iterable<Promotion> listAllPromotions() {
        return promoRepository.findAll();
    }

    @Override
    public Promotion getPromoById(Integer id) {
        return promoRepository.findOne(id);
    }

    @Override
    public Promotion savePromo(Promotion promotion) {
        return promoRepository.save(promotion);
    }
}
