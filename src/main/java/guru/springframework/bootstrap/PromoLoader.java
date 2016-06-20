package guru.springframework.bootstrap;

import guru.springframework.domain.Promotion;
import guru.springframework.repositories.PromoRepository;
import guru.springframework.domain.Product;
import guru.springframework.repositories.ProductRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.math.BigDecimal;

@Component
public class PromoLoader implements ApplicationListener<ContextRefreshedEvent>{

    private PromoRepository promoRepository;

    private Logger log = Logger.getLogger(PromoLoader.class);

    @Autowired
    public void setPromoRepository(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    Promotion shirt = new Promotion();
    shirt.setDescription("Spring Framework Guru Shirt");
        shirt.setName("Hey");
    shirt.setStart("18");
        shirt.setEnd("18");
        shirt.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
    promoRepository.save(shirt);
        Product product = new Product();
        product.setProductId("12345");
        product.setDescription("testing description");
        product.setImageUrl("imageurl");
        product.setPrice(new BigDecimal(10.00));
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(product);
        shirt.setProducts(products);

    log.info("Saved Shirt - id: " + shirt.getId());


    }
}
