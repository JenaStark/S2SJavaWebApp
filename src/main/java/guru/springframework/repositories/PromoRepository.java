package guru.springframework.repositories;

import guru.springframework.domain.Promotion;
import org.springframework.data.repository.CrudRepository;

public interface PromoRepository extends CrudRepository<Promotion, Integer>{
}
