package io.h2o.ufc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.h2o.ufc.dto.DailyFreePlayMatchCount;
import io.h2o.ufc.service.FreePlayMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private FreePlayMatchService freePlayMatchService;

    @GetMapping("/")
    public String viewHomePage(Model model) throws JsonProcessingException {
        ObjectMapper Obj = new ObjectMapper();

        List<DailyFreePlayMatchCount> dailyFreePlayMatchCounts = freePlayMatchService.getDailyFreePlayMatchCounts();

        String dailyFreePlayMatchCountsJsonStr = Obj.writeValueAsString(dailyFreePlayMatchCounts);
        System.err.println(dailyFreePlayMatchCountsJsonStr);
        System.err.println(dailyFreePlayMatchCounts);

        model.addAttribute("dailyFreePlayMatchCounts", dailyFreePlayMatchCountsJsonStr);

        return "index";
    }
}
