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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class PromotionController {

    private PromoService promoService;
    private StoreService storeService;
    private ProductService productService;
    private PromoStoreService promoStoreService;

    private final String USER_AGENT = "Mozilla/5.0";



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
                promoStore = promoStoreService.findFirstByPromoIDAndStoreID(id, store.getId());
                String status;
                if (promoStore.getStatus() == null) {
                    status = "Not completed";
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
        Date dateNow = new Date();
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                        d = sdf.parse(promoService.getPromoById(id).getEnd());
                    } catch (ParseException e) {

                           }
                String expired;
                if(d != null && dateNow.compareTo(d) > 0) {
                        expired= "Expired or ends today";
                    } else if (d == null){
                        expired = "No end date given";
                    } else {
                        expired = "Not expired";
                    }
        model.addAttribute("storeStats", storesStats);
        model.addAttribute("expired", expired);
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

                if(promoStoreService.findFirstByPromoIDAndStoreID(promotion.getId(), idnum) == null) {
                    promoStore.setPromoID(promotion.getId());
                    promoStore.setStoreID(idnum);
                    promoStore.setStatus("Not completed");
                    promoStoreService.savePromotionStore(promoStore);

                }

            }
        }

        return "redirect:/promotion/" + promotion.getId();
    }
    @RequestMapping("promotion/{id}/store/{storeID}")
    public String completePromo(@PathVariable("id") Integer id, @PathVariable("storeID") Integer storeID, Model model) {
        model.addAttribute("promostore", promoStoreService.findFirstByPromoIDAndStoreID(id, storeID));
        model.addAttribute("promotion", promoService.getPromoById(id));
        model.addAttribute("store", storeService.getStoreById(storeID));

        Date dateNow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(promoService.getPromoById(id).getEnd());
        } catch (ParseException e) {

        }
        String expired;
        if(d != null && dateNow.compareTo(d) > 0) {
            expired= "Expired or ends today";
        } else if (d == null){
            expired = "No end date given";
        } else {
            expired = "Not expired";
        }

        List<Product> products = new ArrayList<Product>();
        if(promoService.getPromoById(id).getProductIDs() != null) {
            for (Integer idnum : promoService.getPromoById(id).getProductIDs()) {
                products.add(productService.getProductById(idnum));
            }
        }

        model.addAttribute("expired", expired);
        model.addAttribute("products", products);
        return "completePromo";
    }

    @RequestMapping("*/promoform.html")
    public String newPromotion2(Model model){
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("stores", storeService.listAllStores());
        return "blank";
    }


  @RequestMapping(value = "promostore", method = RequestMethod.POST)
    public String uploadPromoImage(PromotionStore promoStore, @RequestParam("file") MultipartFile file){


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

      promoStore.setFieldLoc("/tmp/" + fileName);

      //check field against reference and set field status and set status to completed if OK
        promoStore.setStatus("Completed");
      promoStoreService.savePromotionStore(promoStore);

      return "redirect:/promotion/" + promoStore.getPromoID() + "/store" + promoStore.getStoreID();

  }

    @RequestMapping("promotion/send/{id}")
    public String sendPostRequest(@PathVariable("id") Integer id) throws IOException {

        Promotion promotion = promoService.getPromoById(id);

        String url = "http://in-cpaas.star2starglobal.com/hook/5776ae04690ee40800000016";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        List<Store> stores = new ArrayList<Store>();
        if(promoService.getPromoById(id).getStoreIDs() != null) {
            for (Integer idnum : promoService.getPromoById(id).getStoreIDs()) {
                stores.add(storeService.getStoreById(idnum));
            }
        }

        for (Store store: stores) {

            List<BasicNameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("Promotion_Name", promotion.getName()));
            urlParameters.add(new BasicNameValuePair("start_date", promotion.getStart()));
            urlParameters.add(new BasicNameValuePair("storeName", store.getName()));
            urlParameters.add(new BasicNameValuePair("storeID", store.getStoreNumber().toString()));
            urlParameters.add(new BasicNameValuePair("end_date", promotion.getEnd()));
            urlParameters.add(new BasicNameValuePair("description", promotion.getDescription()));
            urlParameters.add(new BasicNameValuePair("email", store.getEmail()));
            urlParameters.add(new BasicNameValuePair("link", "http://localhost:8080/promotion/" + id +"/store/" + store.getId()));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            org.apache.http.HttpResponse response = client.execute(post);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result.toString());
        }

        return "redirect:/promotion/" + promotion.getId();

    }



}
