package guru.springframework.services;


import guru.springframework.domain.Store;

public interface StoreService {
    Iterable<Store> listAllStores();

    Store getStoreById(Integer id);

    Store saveStore(Store store);
}