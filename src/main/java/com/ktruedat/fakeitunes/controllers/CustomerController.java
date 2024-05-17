package com.ktruedat.fakeitunes.controllers;

import com.ktruedat.fakeitunes.dao.CustomerDao;
import com.ktruedat.fakeitunes.models.product.items.Track;
import com.ktruedat.fakeitunes.models.product.items.TrackHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    CustomerDao customerDao;

    @GetMapping("/v1")
    public String index(Model model) {
        model.addAttribute("trackHelper", new TrackHelper());
        model.addAttribute("NUMBER_OF_RANDOM_ITEMS_TO_FETCH", CustomerDao.getNumberOfRandomItemsToFetch());
        model.addAttribute("artists", customerDao.getRandomArtists());
        model.addAttribute("songs", customerDao.getRandomSongs());
        model.addAttribute("genres", customerDao.getRandomGenres());
        return "index";
    }

    @PostMapping("/v1")
    public String search(@ModelAttribute TrackHelper trackHelper, BindingResult error, Model model) {
        model.addAttribute("trackName", trackHelper.getName());
        List<Track> allMatches = customerDao.getAllMatches(trackHelper.getName());
        boolean success = allMatches.size() != 0;
        if (success) {
            model.addAttribute("matches", allMatches);
        }
        return "search-results";
    }
}
