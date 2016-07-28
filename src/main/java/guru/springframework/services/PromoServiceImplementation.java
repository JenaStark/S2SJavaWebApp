package guru.springframework.services;

import guru.springframework.domain.Promotion;
import guru.springframework.repositories.PromoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Promotion> findById(Integer id) { return promoRepository.findById(id);}

    public void delete(Promotion promotion){promoRepository.delete(promotion);}

    public Long count(){return promoRepository.count();}

    public List<Promotion> findTop3ByOrderByPostedDesc() {return promoRepository.findTop3ByOrderByPostedDesc();}

    public List<Promotion> findByPostedString(String string) {return promoRepository.findByPostedString(string);}

}
