package nikita.webapp.web.controller.application.saml;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Profile("security-saml")
@Controller
public class LandingController {

    @RequestMapping("/landing")
    public String landing(@CurrentUser User user, Model model) {
        model.addAttribute("username", user.getUsername());
        return "landing";
    }

}
