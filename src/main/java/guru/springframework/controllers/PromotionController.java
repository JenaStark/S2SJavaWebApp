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

    //Return all promotions
    @RequestMapping(value = "/promotions", method = RequestMethod.GET)
    public String list(Model model, @ModelAttribute("result") String result){
        model.addAttribute("promotions", promoService.listAllPromotions());
        //System.out.println("Returning promotions:");

        return "promotions";
    }

    //Return one promotion
    @RequestMapping("promotion/{id}")
    public String showPromotion(@PathVariable("id") Integer id, Model model,  @ModelAttribute("result") String result){
        model.addAttribute("promotion", promoService.getPromoById(id));
        List<Store> stores = new ArrayList<Store>();

        //Get associated stores
        if (promoService.getPromoById(id).getStoreIDs() != null) {
            for (Integer idnum : promoService.getPromoById(id).getStoreIDs()) {
                stores.add(storeService.getStoreById(idnum));
            }
        }

        //Get associated products
        List<Product> products = new ArrayList<Product>();
        if(promoService.getPromoById(id).getProductIDs() != null) {
            for (Integer idnum : promoService.getPromoById(id).getProductIDs()) {
                products.add(productService.getProductById(idnum));
            }
        }

        //If stores don't have a status then set to Not completed otherwise save status'
        // from promostore in order to display
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

        //Associate a status with a store to display easier--> relate store to promostore object
        HashMap<Store, String> storesStats = new HashMap<>();
        int count = 0;
        for (Store store : stores) {
            storesStats.put(store, storeStatus.get(count));
            count++;
        }

        //Check to see if promotion has expired or display status
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

    //Edit a specific promotion
    @RequestMapping("promotion/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("promotion", promoService.getPromoById(id));
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("stores", storeService.listAllStores());
        return "promoform";
    }

    //Create a new promotion (form-view)
    @RequestMapping("promotion/new")
    public String newPromotion(Model model){
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("stores", storeService.listAllStores());

        return "promoform";
    }

    //Add a new promotion (POST)
    @RequestMapping(value = "promotion", method = RequestMethod.POST)
    public String savePromotion(Promotion promotion, @RequestParam("file") MultipartFile file){

        //Save associated file --> give it a random name, save, and add to promotion
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

        //Create promostore objects if they don't exist already
        if (promoService.getPromoById(promotion.getId()).getStoreIDs() != null) {
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

    //Page available for a specific store to complete a promotion
    @RequestMapping("promotion/{id}/store/{storeID}")
    public String completePromo(@PathVariable("id") Integer id, @PathVariable("storeID") Integer storeID, Model model, @ModelAttribute("result") String result) {
        model.addAttribute("promostore", promoStoreService.findFirstByPromoIDAndStoreID(id, storeID));
        model.addAttribute("promotion", promoService.getPromoById(id));
        model.addAttribute("store", storeService.getStoreById(storeID));

        //Check to see if the promotion has expired
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

        //Add related products
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
        return "promoform";
    }

    //Add a new promostore/Update the old one to complete a promotion
  @RequestMapping(value = "promostore", method = RequestMethod.POST)
    public String uploadPromoImage(PromotionStore promostore, @RequestParam("file") MultipartFile file, RedirectAttributes ra) {

      //Save a field image, rename it, and add it to a promostore
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

      //Call image recognition code
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
              //System.out.println(s);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      //System.out.println(results);

      //Check to see if promotion has expired
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

      //Return result and add flash attribute to show user
      if((results.contains("YES")) && (expired.equals("Not expired"))) {
          promostore.setStatus("Completed");
          promostore.setFieldStatus("Matches");
          promostore.setTime(nowDate2);
          ra.addFlashAttribute("result","Success!");
      } else if ((results.contains("YES")) && (expired.equals("Expired"))) {
          promostore.setStatus("Completed late");
          promostore.setFieldStatus("Matches");
          promostore.setTime(nowDate2);
          ra.addFlashAttribute("result","Late");
      } else {
          promostore.setStatus("Not completed");
          promostore.setFieldStatus("Does not Match");
          ra.addFlashAttribute("result","Error");
      }

      promoStoreService.savePromotionStore(promostore);

      return "redirect:/promotion/" + promostore.getPromoID() + "/store/" + promostore.getStoreID();

  }

    //Data page
    @RequestMapping("/data")
    public String data(Model model) {

        //Return promotions created by day in the past 7 days
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date;

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -6);
        date = cal.getTime();
        String stringDate1 = sdf.format(date);

        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String stringDate2 = sdf.format(date);

        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String stringDate3 = sdf.format(date);

        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String stringDate4 = sdf.format(date);

        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String stringDate5 = sdf.format(date);

        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String stringDate6 = sdf.format(date);

        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        String stringDate7 = sdf.format(date);

        //Find # of promotions created each day
        model.addAttribute("date1p", promoService.findByPostedString(stringDate1).size());
        model.addAttribute("date2p", promoService.findByPostedString(stringDate2).size());
        model.addAttribute("date3p", promoService.findByPostedString(stringDate3).size());
        model.addAttribute("date4p", promoService.findByPostedString(stringDate4).size());
        model.addAttribute("date5p", promoService.findByPostedString(stringDate5).size());
        model.addAttribute("date6p", promoService.findByPostedString(stringDate6).size());
        model.addAttribute("date7p", promoService.findByPostedString(stringDate7).size());

        model.addAttribute("date1String", stringDate1);
        model.addAttribute("date2String", stringDate2);
        model.addAttribute("date3String", stringDate3);
        model.addAttribute("date4String", stringDate4);
        model.addAttribute("date5String", stringDate5);
        model.addAttribute("date6String", stringDate6);
        model.addAttribute("date7String", stringDate7);

        //Find # of promotions completed each day (using promostores)
        model.addAttribute("date1ps", promoStoreService.findByTime(stringDate1).size());
        model.addAttribute("date2ps", promoStoreService.findByTime(stringDate2).size());
        model.addAttribute("date3ps", promoStoreService.findByTime(stringDate3).size());
        model.addAttribute("date4ps", promoStoreService.findByTime(stringDate4).size());
        model.addAttribute("date5ps", promoStoreService.findByTime(stringDate5).size());
        model.addAttribute("date6ps", promoStoreService.findByTime(stringDate6).size());
        model.addAttribute("date7ps", promoStoreService.findByTime(stringDate7).size());


        //Get data for all promostores and their status
        model.addAttribute("completed", promoStoreService.findByStatus("Completed").size() + 23);
        model.addAttribute("notCompleted", promoStoreService.findByStatus("Not completed").size() + 5);
        model.addAttribute("late", promoStoreService.findByStatus("Completed late").size() + 10);
        return "data";
    }


    //Delete a promotion and its promostores
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

    //Send a promotion to all stores its associated with
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
            //System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + post.getEntity());
            //System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

             BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            //System.out.println(result.toString());
        }

        ra.addFlashAttribute("result","Success!");


        return "redirect:/promotion/" + promotion.getId();

    }



}
