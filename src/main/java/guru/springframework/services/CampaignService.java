package guru.springframework.services;

import guru.springframework.domain.Campaign;

/**
 * Created by Michael on 6/16/16.
 */
public interface CampaignService {
    Iterable<Campaign> listAllCampaigns();

    Campaign getCampaignById(Integer id);

    Campaign saveCampaign(Campaign campaign);
}

