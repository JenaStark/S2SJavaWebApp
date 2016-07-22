package guru.springframework.services;


import guru.springframework.domain.Campaign;

public interface CampaignService {
    Iterable<Campaign> listAllCampaigns();

    Campaign getCampaignById(Integer id);

    Campaign saveCampaign(Campaign campaign);

    void delete(Campaign campaign);

    Long count();

}
