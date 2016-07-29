package guru.springframework.controllers;

import guru.springframework.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import guru.springframework.domain.Promotion;
import guru.springframework.domain.Store;
import guru.springframework.domain.PromotionStore;
import guru.springframework.domain.Campaign;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


@Controller
public class IndexController {

    private PromoService promoService;
    private StoreService storeService;
    private PromoStoreService promoStoreService;
    private CampaignService campaignService;


    @Autowired
    public void setPromoService(PromoService promoService) {
        this.promoService = promoService;
    }

    @Autowired
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Autowired
    public void setPromoStoreService(PromoStoreService promoStoreService) {
        this.promoStoreService = promoStoreService;
    }

    @Autowired
    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @RequestMapping("/")
    public String index(Model model){
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

        model.addAttribute("date1", promoService.findByPostedString(stringDate1).size());
        model.addAttribute("date2", promoService.findByPostedString(stringDate2).size());
        model.addAttribute("date3", promoService.findByPostedString(stringDate3).size());
        model.addAttribute("date4", promoService.findByPostedString(stringDate4).size());
        model.addAttribute("date5", promoService.findByPostedString(stringDate5).size());
        model.addAttribute("date6", promoService.findByPostedString(stringDate6).size());
        model.addAttribute("date7", promoService.findByPostedString(stringDate7).size());

        model.addAttribute("date1String", stringDate1);
        model.addAttribute("date2String", stringDate2);
        model.addAttribute("date3String", stringDate3);
        model.addAttribute("date4String", stringDate4);
        model.addAttribute("date5String", stringDate5);
        model.addAttribute("date6String", stringDate6);
        model.addAttribute("date7String", stringDate7);


        model.addAttribute("campaignNum", campaignService.count());
        model.addAttribute("promoNum", promoService.count());
        model.addAttribute("storeNum", storeService.count());
        model.addAttribute("recentPromos", promoService.findTop3ByOrderByPostedDesc());

        return "index";
    }

    @RequestMapping("/index.html")
    public String indexhtml(Model model) {
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

        model.addAttribute("date1", promoService.findByPostedString(stringDate1).size());
        model.addAttribute("date2", promoService.findByPostedString(stringDate2).size());
        model.addAttribute("date3", promoService.findByPostedString(stringDate3).size());
        model.addAttribute("date4", promoService.findByPostedString(stringDate4).size());
        model.addAttribute("date5", promoService.findByPostedString(stringDate5).size());
        model.addAttribute("date6", promoService.findByPostedString(stringDate6).size());
        model.addAttribute("date7", promoService.findByPostedString(stringDate7).size());

        model.addAttribute("date1String", stringDate1);
        model.addAttribute("date2String", stringDate2);
        model.addAttribute("date3String", stringDate3);
        model.addAttribute("date4String", stringDate4);
        model.addAttribute("date5String", stringDate5);
        model.addAttribute("date6String", stringDate6);
        model.addAttribute("date7String", stringDate7);

        model.addAttribute("campaignNum", campaignService.count());
        model.addAttribute("promoNum", promoService.count());
        model.addAttribute("storeNum", storeService.count());
        model.addAttribute("recentPromos", promoService.findTop3ByOrderByPostedDesc());

        return "index";
    }

    @RequestMapping("/home")
    public String home(Model model) {
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

        model.addAttribute("date1", promoService.findByPostedString(stringDate1).size());
        model.addAttribute("date2", promoService.findByPostedString(stringDate2).size());
        model.addAttribute("date3", promoService.findByPostedString(stringDate3).size());
        model.addAttribute("date4", promoService.findByPostedString(stringDate4).size());
        model.addAttribute("date5", promoService.findByPostedString(stringDate5).size());
        model.addAttribute("date6", promoService.findByPostedString(stringDate6).size());
        model.addAttribute("date7", promoService.findByPostedString(stringDate7).size());

        model.addAttribute("date1String", stringDate1);
        model.addAttribute("date2String", stringDate2);
        model.addAttribute("date3String", stringDate3);
        model.addAttribute("date4String", stringDate4);
        model.addAttribute("date5String", stringDate5);
        model.addAttribute("date6String", stringDate6);
        model.addAttribute("date7String", stringDate7);

        model.addAttribute("campaignNum", campaignService.count());
        model.addAttribute("promoNum", promoService.count());
        model.addAttribute("storeNum", storeService.count());
        model.addAttribute("recentPromos", promoService.findTop3ByOrderByPostedDesc());

        return "index";
    }


    @RequestMapping("/about")
    String about() {
        return "about";
    }


    @RequestMapping("/login")
    String login() {
        return "login";
    }

    @RequestMapping("/lock")
    String lock() {
        return "lock";
    }
}
