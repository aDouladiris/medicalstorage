package com.unipi.adouladiris.medicalstorage.rest.controllers.home;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class HomeController {

    @ApiIgnore
    @GetMapping(value = "")
    @PreAuthorize("permitAll()")
    public ModelAndView redirectUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/swagger-ui.html?urls.primaryName=Medical%20Storage%20Api%20v1%20-%20User#/User", model);
    }

}
