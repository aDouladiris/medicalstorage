package com.unipi.adouladiris.medicalstorage.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.logging.Filter;

@Controller
public class HomeController {

    @ApiIgnore
    @GetMapping(value = "")
    public ModelAndView redirectUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/swagger-ui.html?urls.primaryName=Medical%20Storage%20Api%20v1%20-%20User#/User", model);
    }

}
