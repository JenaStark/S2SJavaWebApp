package guru.springframework.services;

import guru.springframework.domain.Campaign;
import guru.springframework.repositories.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignServiceImplementation implements CampaignService {
    private CampaignRepository campaignRepository;

    @Autowired
    public void setCampaignRepository(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Iterable<Campaign> listAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Campaign getCampaignById(Integer id) {
        return campaignRepository.findOne(id);
    }

    @Override
    public Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public void delete(Campaign campaign) {campaignRepository.delete(campaign);}


}
