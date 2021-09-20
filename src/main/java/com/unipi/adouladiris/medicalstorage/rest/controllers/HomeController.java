package com.unipi.adouladiris.medicalstorage.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class HomeController {

    //********************************************************************
    // Http request will be intercepted by Token filter before proceeding.
    //********************************************************************

    // Redirect to swagger Home page.
    @ApiIgnore
    @GetMapping(value = "")
    public ModelAndView redirectUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/swagger-ui.html?urls.primaryName=Medical%20Storage%20Api%20v1%20-%20User#/User", model);
    }

}
