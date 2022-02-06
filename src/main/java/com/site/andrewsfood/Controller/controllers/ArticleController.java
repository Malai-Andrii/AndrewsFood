package com.site.andrewsfood.Controller.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ArticleController {
    @GetMapping("/firstArticle")
    public String firstArticleGet(Model model) {
        log.info("firstArticle GET");
        return "tempNoAuthorization/firstArticle";
    }
    @GetMapping("/secondArticle")
    public String secondArticleGet(Model model) {

        log.info("secondArticle GET");
        return "tempNoAuthorization/secondArticle";
    }
    @GetMapping("/thirdArticle")
    public String thirdArticleGet(Model model) {
        log.info("thirdArticle GET");
        return "tempNoAuthorization/thirdArticle";
    }
}
