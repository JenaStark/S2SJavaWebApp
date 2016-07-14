package guru.springframework.repositories;

import guru.springframework.domain.Promotion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PromoRepository extends CrudRepository<Promotion, Integer>{

    List<Promotion> findById(Integer id);

    Integer deleteById(Integer id);

}
