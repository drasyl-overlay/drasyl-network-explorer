package org.drasyl.networkexplorer;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {
    @RequestMapping(value = "/error")
    public String error() {
        return "forward:/index.html";
    }
}
