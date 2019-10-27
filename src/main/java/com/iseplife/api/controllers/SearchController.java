package com.iseplife.api.controllers;


import com.iseplife.api.dto.view.SearchItemView;
import com.iseplife.api.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

  @Autowired
  SearchService searchService;

  @GetMapping
  public List<SearchItemView> globalSearch(@RequestParam String filter){
    return searchService.globalSearch(filter);
  }

  @GetMapping("/user")
  public List<SearchItemView> userSearch(@RequestParam String filter){
    return searchService.searchUser(filter);
  }

  @GetMapping("/club")
  public List<SearchItemView> clubSearch(@RequestParam String filter){
    return searchService.searchClub(filter);
  }

  @GetMapping("/event")
  public List<SearchItemView> eventSearch(@RequestParam String filter){
    return searchService.searchEvent(filter);
  }

}
