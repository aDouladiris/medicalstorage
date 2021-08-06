package com.unipi.adouladiris.medicalstorage.rest.controllers.abstractClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@Controller
//@RequestMapping("/api/v1")
public class RoutingController {

    @ApiIgnore
    @GetMapping(value = "")
    @PreAuthorize("permitAll()")
    public ModelAndView redirectUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/swagger-ui.html?urls.primaryName=Medical%20Storage%20API%20v1%20-%20User#/user-controller", model);
    }

//    protected String objectToJSON(Object object) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//        return json;
//    }



}
