package guru.springframework.controllers;

import guru.springframework.domain.Campaign;
import guru.springframework.services.CampaignService;
import guru.springframework.domain.Promotion;
import guru.springframework.services.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CampaignController {

    private CampaignService campaignService;
    private PromoService promoService;


    @Autowired
    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Autowired
    public void setPromoService(PromoService promoService) {
        this.promoService = promoService;
    }


    @RequestMapping(value = "/campaigns", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("campaigns", campaignService.listAllCampaigns());
        System.out.println("Returning campaigns:");
        return "campaigns";
    }

    @RequestMapping("campaign/{id}")
    public String showCampaign(@PathVariable Integer id, Model model){
        model.addAttribute("campaign", campaignService.getCampaignById(id));
        List<Promotion> promotions = new ArrayList<Promotion>();
        for (Integer idnum: campaignService.getCampaignById(id).getPromoIDs()) {
            promotions.addAll(promoService.findById(idnum));
        }
        model.addAttribute("promotions", promotions);
        return "campaignshow";
    }

    /*@RequestMapping("promotion/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("promotion", promoService.getPromoById(id));
        return "blank";
    }

    @RequestMapping("promotion/new")
    public String newPromotion(Model model){
        model.addAttribute("promotion", new Promotion());
        return "blank";
    }

    @RequestMapping(value = "promotion", method = RequestMethod.POST)
    public String savePromotion(Promotion promotion){

        promoService.savePromo(promotion);

        return "redirect:/promotion/" + promotion.getId();
    }

    @RequestMapping(value = "*")
    public String noValue(){
        return "errorPage";
    }*/

}
