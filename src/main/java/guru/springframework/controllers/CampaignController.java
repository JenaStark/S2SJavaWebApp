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
        if(campaignService.getCampaignById(id).getPromoIDs() != null) {
            for (Integer idnum : campaignService.getCampaignById(id).getPromoIDs()) {
                promotions.add(promoService.getPromoById(idnum));
            }
        }
        model.addAttribute("promotions", promotions);
        return "campaignshow";
    }

    @RequestMapping("campaign/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        model.addAttribute("campaign", campaignService.getCampaignById(id));
        List<Promotion> promotions = new ArrayList<Promotion>();
        if(campaignService.getCampaignById(id).getPromoIDs() != null) {
            for (Integer idnum : campaignService.getCampaignById(id).getPromoIDs()) {
                promotions.add(promoService.getPromoById(idnum));
            }
        }
        model.addAttribute("promotionsCurrent", promotions);
        model.addAttribute("promotionsAll", promoService.listAllPromotions());
        return "campaignform";
    }

    @RequestMapping("campaign/new")
    public String newCampaign(Model model){
        model.addAttribute("campaign", new Campaign());
        model.addAttribute("promotionsAll", promoService.listAllPromotions());
        return "campaignform";
    }

    @RequestMapping(value = "campaign", method = RequestMethod.POST)
    public String saveCampaign(Campaign campaign){

        campaignService.saveCampaign(campaign);

        return "redirect:/campaign/" + campaign.getId();
    }

}
