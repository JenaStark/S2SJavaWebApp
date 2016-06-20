package guru.springframework.bootstrap;

import guru.springframework.domain.Campaign;
import guru.springframework.repositories.CampaignRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 6/16/16.
 */
@Component
public class CampaignLoader implements ApplicationListener<ContextRefreshedEvent> {
    private CampaignRepository campaignRepository;

    private Logger log = Logger.getLogger(CampaignLoader.class);

    @Autowired
    public void setCampaignRepository(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

         Campaign campaign = new Campaign();
         campaign.setName("Segun");
         campaign.setDescription("Spring Framework Guru Shirt");

//         Integer[] integerList = new ArrayList<Integer>();
//         campaign.setPromoIDs(integerList);
         campaignRepository.save(campaign);

         log.info("Saved Campaign - id: " + campaign.getId());
    }
}
