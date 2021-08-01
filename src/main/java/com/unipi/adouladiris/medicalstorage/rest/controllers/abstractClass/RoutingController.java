package com.unipi.adouladiris.medicalstorage.rest.controllers.abstractClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1")
public abstract class RoutingController {

//    @ApiIgnore
//    @GetMapping("")
//    @PreAuthorize("hasAnyRole('admin', 'customer')")
//    public ModelAndView redirectUsingRedirectPrefix(ModelMap model) {
//        return new ModelAndView("redirect:/swagger-ui.html#/product-controller", model);
//    }

    protected String objectToJSON(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        return json;
    }



}
