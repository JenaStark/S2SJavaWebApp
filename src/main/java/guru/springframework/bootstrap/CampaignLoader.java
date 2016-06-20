package guru.springframework.bootstrap;

import guru.springframework.domain.Campaign;
import guru.springframework.repositories.CampaignRepository;
import guru.springframework.domain.Promotion;
import guru.springframework.repositories.PromoRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.math.BigDecimal;

@Component
public class CampaignLoader implements ApplicationListener<ContextRefreshedEvent>{

    private CampaignRepository campaignRepository;

    private Logger log = Logger.getLogger(CampaignLoader.class);

    @Autowired
    public void setCampaignRepository(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Campaign shirts = new Campaign();
        shirts.setDescription("Spring Framework Guru Shirt Campaign");
        shirts.setName("Hey");

        Promotion shirt = new Promotion();
        shirt.setDescription("Spring Framework Guru Shirt");
        shirt.setName("Hey");
        shirt.setStart("18");
        shirt.setEnd("18");
        shirt.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
        ArrayList<Integer> promotions = new ArrayList<Integer>();
        promotions.add(1);
        shirts.setPromoIDs(promotions);

        campaignRepository.save(shirts);


        log.info("Saved Shirt - id: " + shirt.getId());


    }
}
