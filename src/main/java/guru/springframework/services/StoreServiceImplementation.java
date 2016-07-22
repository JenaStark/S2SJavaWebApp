package guru.springframework.services;

import guru.springframework.domain.Store;
import guru.springframework.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImplementation implements StoreService {
    private StoreRepository storeRepository;

    @Autowired
    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Iterable<Store> listAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Store getStoreById(Integer id) {
        return storeRepository.findOne(id);
    }

    @Override
    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Long count() { return storeRepository.count();}
}
