package com.unipi.adouladiris.medicalstorage.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unipi.adouladiris.medicalstorage.rest.controller.abstractClass.RoutingController;
import io.swagger.annotations.Authorization;
import org.json.simple.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.oauth2.client.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import com.google.gson.*;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1")
public class UserController extends RoutingController {


    @GetMapping(value = "/user")
//    @PreAuthorize("hasAuthority('retrieveAuthentication:user')")
    public Object retrieveAuthentication() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return objectToJSON(authentication);
    }



}