package guru.springframework.controllers;

import guru.springframework.domain.Campaign;
import guru.springframework.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael on 6/16/16.
 */

@Controller
public class CampaignController {
    private CampaignService campaignService;

    @Autowired
    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @RequestMapping(value = "/campaigns", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("campaigns", campaignService.listAllCampaigns());
        System.out.println("Returning campaigns:");
        return "campaigns";
    }

    //Views Campaign
    @RequestMapping("campaign/{id}")
    public String showProduct(@PathVariable Integer id, Model model){
        model.addAttribute("campaign", campaignService.getCampaignById(id));
        return "campaignshow";
    }

    //Opens Campaign Edit
    @RequestMapping("campaign/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("campaign", campaignService.getCampaignById(id));
        return "campaignform";
    }

    //Add New Campaign
    @RequestMapping("campaign/new")
    public String newCampaign(Model model){
        model.addAttribute("campaign", new Campaign());
        return "campaignform";
    }

    @RequestMapping(value = "campaign", method = RequestMethod.POST)
    public String saveCampaign(Campaign campaign){

        campaignService.saveCampaign(campaign);

        //Opens Newly added Campaign
//        return "redirect:/campaigns" + campaign.getId();
        return "redirect:/campaigns";
    }



}
