package guru.springframework.repositories;

import guru.springframework.domain.Campaign;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Michael on 6/16/16.
 */
public interface CampaignRepository extends CrudRepository<Campaign, Integer>{
}
