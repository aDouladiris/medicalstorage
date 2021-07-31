package com.unipi.adouladiris.medicalstorage.rest.controller.abstractClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;


// @RestController => {@Controller, @ResponseBody}

//@RestController
public abstract class RoutingController {

//    @ApiIgnore
//    @GetMapping("")
//    public ModelAndView redirectUsingRedirectPrefix(ModelMap model) {
//        return new ModelAndView("redirect:/swagger-ui.html#/product-controller", model);
//    }

    protected String objectToJSON(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        return json;
    }

}
