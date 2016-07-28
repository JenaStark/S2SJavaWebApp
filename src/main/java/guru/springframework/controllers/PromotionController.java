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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PromotionController {

    private PromoService promoService;
    private StoreService storeService;
    private ProductService productService;
    private PromoStoreService promoStoreService;

    private final String USER_AGENT = "Mozilla/5.0";
    private int pPhotoCount = 0;
    private int sPhotoCount = 0;



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
    public String list(Model model, @ModelAttribute("result") String result){
        model.addAttribute("promotions", promoService.listAllPromotions());
        System.out.println("Returning promotions:");

        return "promotions";
    }

    @RequestMapping("promotion/{id}")
    public String showPromotion(@PathVariable("id") Integer id, Model model,  @ModelAttribute("result") String result){
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

        String nowDate2 = null;
        Date nowDate = null;
        try {
            nowDate2 = sdf.format(dateNow);
            nowDate = sdf.parse(nowDate2);
        } catch (ParseException e) {
        }

                String expired;
                if(d != null && nowDate.compareTo(d) > 0) {
                        expired= "Expired";
                    } else if (d == null){
                        expired = "No end date given";
                    } else if(nowDate.compareTo(d) < 0) {
                        expired = "Not expired";
                    } else {
                     expired = "Last day";
                }
        model.addAttribute("result", result);
        model.addAttribute("storeStats", storesStats);
        model.addAttribute("expired", expired);
        model.addAttribute("status", storeStatus);
        model.addAttribute("stores", stores);
        model.addAttribute("products",products);
        return "promoshow";
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
        Random random = new Random();
        int fileNum = random.nextInt(20001) + 10000;
        fileName = Integer.toString(fileNum) + Integer.toString(pPhotoCount++) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        if (!file.isEmpty()) {
            try {
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

        Date dateNow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String nowDate2 = null;
        Date nowDate = null;
        try {
            nowDate2 = sdf.format(dateNow);
            nowDate = sdf.parse(nowDate2);
        } catch (ParseException e) {
        }
        promotion.setPosted(dateNow);
        promotion.setPostedString(nowDate2);

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
    public String completePromo(@PathVariable("id") Integer id, @PathVariable("storeID") Integer storeID, Model model, @ModelAttribute("result") String result) {
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

        String nowDate2 = null;
        Date nowDate = null;
        try {
            nowDate2 = sdf.format(dateNow);
            nowDate = sdf.parse(nowDate2);
        } catch (ParseException e) {
        }

        String expired;
        if(d != null && nowDate.compareTo(d) > 0) {
            expired= "Expired";
        } else if (d == null){
            expired = "No end date given";
        } else if(nowDate.compareTo(d) < 0) {
            expired = "Not expired";
        } else {
            expired = "Ends today";
        }

        model.addAttribute("result", result);
        List<Product> products = new ArrayList<Product>();
        if(promoService.getPromoById(id).getProductIDs() != null) {
            for (Integer idnum : promoService.getPromoById(id).getProductIDs()) {
                products.add(productService.getProductById(idnum));
            }
        }
        result = null;

        model.addAttribute("expired", expired);
        model.addAttribute("products", products);
        return "completepromo";
    }

    @RequestMapping("*/promoform.html")
    public String newPromotion2(Model model){
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("stores", storeService.listAllStores());
        return "blank";
    }


  @RequestMapping(value = "promostore", method = RequestMethod.POST)
    public String uploadPromoImage(PromotionStore promostore, @RequestParam("file") MultipartFile file, RedirectAttributes ra) {

      String fileName = null;
      BufferedOutputStream buffStream = null;
      Random random = new Random();
      int fileNum = random.nextInt(20001) + 10000;
      fileName = Integer.toString(fileNum) + Integer.toString(sPhotoCount++) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
      if (!file.isEmpty()) {
          try {
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

      Promotion promotion =  promoService.getPromoById(promostore.getPromoID());
      promostore.setFieldLoc("/tmp/" + fileName);
      String pythonPath = "/private/tmp/new_index.py";
      String referencePath = promotion.getFileLoc();
      String storePath = promostore.getFieldLoc();

       String results = "";
      try {
          String[] cmd = new String[4];
          cmd[0] = "python";
          cmd[1] = pythonPath;
          cmd[2] = storePath;
          cmd[3] = referencePath;
          Process p = Runtime.getRuntime().exec(cmd);
          BufferedReader in = new BufferedReader(
                  new InputStreamReader(p.getInputStream()));
          BufferedReader stdError = new BufferedReader(new
                  InputStreamReader(p.getErrorStream()));
          p.waitFor();
          String line = null;

          while ((line = in.readLine()) != null) {
            results = results.concat(line);
          }
          String s = null;
          while ((s = stdError.readLine()) != null) {
              System.out.println(s);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      System.out.println(results);

      Date dateNow = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date d = null;
      try {
          d = sdf.parse(promotion.getEnd());
      } catch (ParseException e) {

      }

      String nowDate2 = null;
      Date nowDate = null;
      try {
          nowDate2 = sdf.format(dateNow);
          nowDate = sdf.parse(nowDate2);
      } catch (ParseException e) {
      }

      String expired;
      if(d != null && nowDate.compareTo(d) > 0) {
          expired= "Expired";
      } else if (d == null){
          expired = "No end date given";
      } else if(nowDate.compareTo(d) < 0) {
          expired = "Not expired";
      } else {
          expired = "Ends today";
      }

      if((results.contains("YES")) && (expired.equals("Not expired"))) {
          promostore.setStatus("Completed");
          promostore.setFieldStatus("Matches");
          promostore.setTime(nowDate2.toString());
          ra.addFlashAttribute("result","Success!");
      } else if ((results.contains("YES")) && (expired.equals("Expired"))) {
          promostore.setStatus("Completed late");
          promostore.setFieldStatus("Matches");
          promostore.setTime(nowDate.toString());
          ra.addFlashAttribute("result","Late");
      } else {
          promostore.setStatus("Not completed");
          promostore.setFieldStatus("Does not Match");
          ra.addFlashAttribute("result","Error");
      }

      promoStoreService.savePromotionStore(promostore);

      return "redirect:/promotion/" + promostore.getPromoID() + "/store/" + promostore.getStoreID();

  }

    @RequestMapping("/data")
    public String data(Model model) {
        model.addAttribute("completed", promoStoreService.findByStatus("Completed").size() + 23);
        model.addAttribute("notCompleted", promoStoreService.findByStatus("Not completed").size() + 5);
        model.addAttribute("late", promoStoreService.findByStatus("Completed late").size() + 10);
        return "data";
    }


    @RequestMapping(value = "promotion/delete/{id}")
    public String deletePromo(@PathVariable("id") Integer id) {
        if(promoService.getPromoById(id).getStoreIDs() != null) {
            for (Integer sid : promoService.getPromoById(id).getStoreIDs()) {
                promoStoreService.delete(promoStoreService.findFirstByPromoIDAndStoreID(id, sid));
            }
        }
        promoService.delete(promoService.getPromoById(id));

        return "redirect:/promotions";
    }

    @RequestMapping("promotion/send/{id}")
    public String sendPostRequest(@PathVariable("id") Integer id, RedirectAttributes ra) throws IOException {

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

        ra.addFlashAttribute("result","Success!");


        return "redirect:/promotion/" + promotion.getId();

    }



}
