package no.law.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Ctrl_Laws {
    @GetMapping("/laws")
    public String main(@RequestParam(name="law", required=false) String law, Model model) {
        model.addAttribute("law", law);
        return "laws-gui";
    }
}