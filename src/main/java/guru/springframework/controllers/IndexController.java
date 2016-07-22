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
        model.addAttribute("campaignNum", campaignService.count());
        model.addAttribute("promoNum", promoService.count());
        model.addAttribute("storeNum", storeService.count());
        model.addAttribute("recentPromos", promoService.findTop6ByOrderByPostedDesc());

        return "index";
    }

    @RequestMapping("/index.html")
    public String indexhtml(Model model) {
        model.addAttribute("campaignNum", campaignService.count());
        model.addAttribute("promoNum", promoService.count());
        model.addAttribute("storeNum", storeService.count());
        model.addAttribute("recentPromos", promoService.findTop6ByOrderByPostedDesc());

        return "index";
    }

    @RequestMapping("/home")
    public String home(Model model)
    {
        model.addAttribute("campaignNum", campaignService.count());
        model.addAttribute("promoNum", promoService.count());
        model.addAttribute("storeNum", storeService.count());
        model.addAttribute("recentPromos", promoService.findTop6ByOrderByPostedDesc());

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
