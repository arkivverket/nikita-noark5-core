package nikita.webapp.web.controller.browser;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static nikita.common.config.Constants.SLASH;

/**
 * Created by tsodring on 5/5/17.
 */
@RestController("BrowserApplicationController")
@RequestMapping(value = SLASH)
public class BrowserApplicationController {
/*
    @GetMapping
    public ModelAndView identifyForBrowser(HttpServletRequest request) {
        return new ModelAndView("webapp/browser/applicationlist", "applicationDetails", getApplicationDetails());
    }
*/
}
