package io.pivotal.shinyay.controller

import io.pivotal.shinyay.service.FrontendService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttributes

@Controller
@SessionAttributes("name")
class FrontendController(val frontendService: FrontendService) {

    @GetMapping("/")
    fun index(model: Model): String {
        if(model.containsAttribute("name")) {
            val name: String = model.asMap().get("name") as String
            model.addAttribute("greeing", frontendService.greeting(name))
        }
        return "index"
    }
}