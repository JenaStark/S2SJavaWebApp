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

    Promotion shirt2 = new Promotion();
    shirt2.setDescription("Spring Framework Guru Shirt");
        shirt2.setName("Hey");
    shirt2.setStart("18");
        shirt2.setEnd("18");
        shirt2.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
        Store store = new Store();
        store.setName("Store Example");
        store.setStoreNumber(22);
        storeRepository.save(store);
        ArrayList<Integer> storeIDs = new ArrayList<Integer>();
        storeIDs.add(store.getId());
        shirt2.setStoreIDs(storeIDs);

        Product mug = new Product();
        mug.setDescription("Spring Framework Guru Mug");
        mug.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_coffee_mug-r11e7694903c348e1a667dfd2f1474d95_x7j54_8byvr_512.jpg");
        mug.setProductId("168639393495335947");
        mug.setPrice(new BigDecimal("11.95"));
        mug.setShelf(5);
        mug.setBin(3);
        productRepository.save(mug);

    ArrayList<Integer> productIDs = new ArrayList<>();
        productIDs.add(mug.getId());
        shirt2.setProductIDs(productIDs);

    promoRepository.save(shirt2);

    log.info("Saved Shirt - id: " + shirt2.getId());


    }
}
