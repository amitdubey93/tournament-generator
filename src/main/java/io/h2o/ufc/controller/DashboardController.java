package io.h2o.ufc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.h2o.ufc.dto.DailyFreePlayMatchCount;
import io.h2o.ufc.dto.PlayerActivity;
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
    public String redirect() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String viewHomePage(Model model) throws JsonProcessingException {
        ObjectMapper Obj = new ObjectMapper();

        List<DailyFreePlayMatchCount> dailyFreePlayMatchCounts = freePlayMatchService.getDailyFreePlayMatchCounts();
        List<PlayerActivity> playerActivity = freePlayMatchService.getPlayerWiseMatchPlayedPercent();
//        dailyFreePlayMatchCounts.stream().filter(data -> data.getMatchCount() > 10).toList();
//        dailyFreePlayMatchCounts.stream().filter(data -> Objects.equals(data.getMatchDate(), "2024-06-18") || Objects.equals(data.getMatchDate(), "2024-06-122")).toList();

        String dailyFreePlayMatchCountsJsonStr = Obj.writeValueAsString(dailyFreePlayMatchCounts);
        String playerActivityJsonStr = Obj.writeValueAsString(playerActivity);
//        System.err.println(dailyFreePlayMatchCountsJsonStr);
//        System.err.println(dailyFreePlayMatchCounts);
//        System.err.println("playerActivity>>>   " + playerActivity);
//        System.err.println("playerActivityJsonStr>>>   " + playerActivityJsonStr);

        model.addAttribute("dailyFreePlayMatchCounts", dailyFreePlayMatchCountsJsonStr);
        model.addAttribute("playerActivity", playerActivityJsonStr);

        return "index";
    }
}
