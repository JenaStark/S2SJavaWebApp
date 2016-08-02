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






    }
}
