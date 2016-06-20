package guru.springframework.bootstrap;

import guru.springframework.domain.Promotion;
import guru.springframework.repositories.PromoRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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

        Promotion shirt2 = new Promotion();
        shirt2.setDescription("Segun");
        shirt2.setName("Hey2");
        shirt2.setStart("19");
        shirt2.setEnd("19");
        shirt2.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
        promoRepository.save(shirt2);




    log.info("Saved Shirt - id: " + shirt.getId());

    }
}
