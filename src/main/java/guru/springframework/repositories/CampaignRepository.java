package guru.springframework.repositories;

import guru.springframework.domain.Campaign;
import org.springframework.data.repository.CrudRepository;

public interface CampaignRepository extends CrudRepository<Campaign, Integer>{
}
