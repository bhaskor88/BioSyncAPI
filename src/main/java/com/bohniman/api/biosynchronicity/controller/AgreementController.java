package com.bohniman.api.biosynchronicity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AgreementController {

    @RequestMapping(value = "/page1", method = RequestMethod.GET)
    public ModelAndView getPage1(ModelAndView mv) {
        mv.setViewName("Page1");
        return mv;
    }

    @RequestMapping(value = "/page2", method = RequestMethod.GET)
    public ModelAndView getPage2(ModelAndView mv) {
        mv.setViewName("Page2");
        return mv;
    }
}
