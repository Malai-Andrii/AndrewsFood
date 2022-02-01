package com.site.andrewsfood.Controller.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
    @GetMapping("/firstArticle")
    public String firstArticleGet(Model model) {
        return "tempNoAuthorization/firstArticle";
    }
    @GetMapping("/secondArticle")
    public String secondArticleGet(Model model) {
        return "tempNoAuthorization/secondArticle";
    }
    @GetMapping("/thirdArticle")
    public String thirdArticleGet(Model model) {
        return "tempNoAuthorization/thirdArticle";
    }
}
