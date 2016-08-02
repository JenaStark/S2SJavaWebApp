package guru.springframework.bootstrap;

import guru.springframework.domain.Promotion;
import guru.springframework.repositories.PromoRepository;
import guru.springframework.domain.Store;
import guru.springframework.repositories.StoreRepository;
import guru.springframework.domain.Product;
import guru.springframework.repositories.ProductRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.math.BigDecimal;

@Component
public class PromoLoader implements ApplicationListener<ContextRefreshedEvent>{

    private PromoRepository promoRepository;
    private StoreRepository storeRepository;
    private ProductRepository productRepository;

    private Logger log = Logger.getLogger(PromoLoader.class);

    @Autowired
    public void setPromoRepository(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }


    @Autowired
    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {




    }
}
