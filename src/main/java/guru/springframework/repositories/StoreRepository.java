package guru.springframework.repositories;

import guru.springframework.domain.Store;
import org.springframework.data.repository.CrudRepository;


public interface StoreRepository extends CrudRepository<Store, Integer>{

}
