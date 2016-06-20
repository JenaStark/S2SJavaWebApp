package guru.springframework.controllers;

import guru.springframework.domain.Promotion;
import guru.springframework.services.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PromotionController {

    private PromoService promoService;

    @Autowired
    public void setPromoService(PromoService promoService) {
        this.promoService = promoService;
    }

    @RequestMapping(value = "/promotions", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("promotions", promoService.listAllPromotions());
        System.out.println("Returning promotions:");
        return "promotions";
    }

    @RequestMapping("promotion/{id}")
    public String showPromotion(@PathVariable Integer id, Model model){
        model.addAttribute("promotion", promoService.getPromoById(id));
        return "promoshow";
    }

    @RequestMapping("promotion/edit/{id}")
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

}
