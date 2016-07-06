package guru.springframework.controllers;

import guru.springframework.domain.Promotion;
import guru.springframework.services.PromoService;
import guru.springframework.domain.Store;
import guru.springframework.services.StoreService;
import guru.springframework.domain.Product;
import guru.springframework.services.ProductService;
import guru.springframework.services.PromoStoreService;
import guru.springframework.domain.PromotionStore;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class PromotionController {

    private PromoService promoService;
    private StoreService storeService;
    private ProductService productService;
    private PromoStoreService promoStoreService;


    @Autowired
    public void setPromoService(PromoService promoService) {
        this.promoService = promoService;
    }

    @Autowired
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setPromoStoreService(PromoStoreService promoStoreService) {
        this.promoStoreService = promoStoreService;
    }


    @RequestMapping(value = "/promotions", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("promotions", promoService.listAllPromotions());
        System.out.println("Returning promotions:");
        return "promotions";
    }

    @RequestMapping("promotion/{id}")
    public String showPromotion(@PathVariable("id") Integer id, Model model){
        model.addAttribute("promotion", promoService.getPromoById(id));
        List<Store> stores = new ArrayList<Store>();
        if(promoService.getPromoById(id).getStoreIDs() != null) {
            for (Integer idnum : promoService.getPromoById(id).getStoreIDs()) {
                stores.add(storeService.getStoreById(idnum));
            }
        }

        List<Product> products = new ArrayList<Product>();
        if(promoService.getPromoById(id).getProductIDs() != null) {
            for (Integer idnum : promoService.getPromoById(id).getProductIDs()) {
                products.add(productService.getProductById(idnum));
            }
        }

        List<String> storeStatus = new ArrayList<String>();
            for (Store store: stores) {
                PromotionStore promoStore = new PromotionStore();
                promoStore = promoStoreService.findByPromoIDAndStoreID(id, store.getId());
                String status;
                if (promoStore.getStatus() == null) {
                    status = "Not completed.";
                } else {
                    status = promoStore.getStatus();
                }
                storeStatus.add(status);
            }
        HashMap<Store, String> storesStats = new HashMap<>();
        int count = 0;
        for (Store store : stores) {
            storesStats.put(store, storeStatus.get(count));
            count++;
        }
        model.addAttribute("storeStats", storesStats);
        model.addAttribute("status", storeStatus);
        model.addAttribute("stores", stores);
        model.addAttribute("products",products);
        return "promoshow";
    }

    @RequestMapping(value="promotion/singleSave", method=RequestMethod.POST )
    public void singleSave(@RequestParam("file") MultipartFile file){

        String fileName = null;
        if (!file.isEmpty()) {
            try {
                fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                BufferedOutputStream buffStream =
                        new BufferedOutputStream(new FileOutputStream(new File("/tmp/" + fileName)));
                buffStream.write(bytes);
                buffStream.close();
            } catch (Exception e) {
            }
        } else {
        }
    }

    @RequestMapping("promotion/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("promotion", promoService.getPromoById(id));
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("stores", storeService.listAllStores());
        return "blank";
    }

    @RequestMapping("promotion/new")
    public String newPromotion(Model model){
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("stores", storeService.listAllStores());

        return "blank";
    }

    @RequestMapping(value = "promotion", method = RequestMethod.POST)
    public String savePromotion(Promotion promotion, @RequestParam("file") MultipartFile file){

        String fileName = null;
        BufferedOutputStream buffStream = null;
        if (!file.isEmpty()) {
            try {
                fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                buffStream =
                        new BufferedOutputStream(new FileOutputStream(new File("/tmp/" + fileName)));
                buffStream.write(bytes);
                buffStream.close();
            } catch (Exception e) {
            } finally {
               IOUtils.closeQuietly(buffStream);
            }
        } else {
        }
    promotion.setFileLoc("/tmp/" + fileName);
    promoService.savePromo(promotion);

        if(promoService.getPromoById(promotion.getId()).getStoreIDs() != null) {
            for (Integer idnum : promoService.getPromoById(promotion.getId()).getStoreIDs()) {
                PromotionStore promoStore = new PromotionStore();

                promoStore.setPromoID(promotion.getId());
                promoStore.setStoreID(idnum);
                promoStore.setStatus("Not completed.");
                promoStoreService.savePromotionStore(promoStore);

            }
        }



        return "redirect:/promotion/" + promotion.getId();
    }
    @RequestMapping("promotion/{id}/store/{storeID}")
    public String completePromo(@PathVariable("id") Integer id, @PathVariable("storeID") Integer storeID, Model model) {
        return "completePromo";
    }

    @RequestMapping("*/promoform.html")
    public String newPromotion2(Model model){
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("stores", storeService.listAllStores());
        return "blank";
    }

}
