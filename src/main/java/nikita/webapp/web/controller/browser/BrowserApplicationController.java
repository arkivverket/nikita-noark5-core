package nikita.webapp.web.controller.browser;

import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tsodring on 5/5/17.
 */
@RestController("BrowserApplicationController")
@RequestMapping(value = "/")
public class BrowserApplicationController
        extends NoarkController {
/*
    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView identifyForBrowser(HttpServletRequest request) {
        return new ModelAndView("webapp/browser/applicationlist", "applicationDetails", getApplicationDetails());
    }
*/
}
