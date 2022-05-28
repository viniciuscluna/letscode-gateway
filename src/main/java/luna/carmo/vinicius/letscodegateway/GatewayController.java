package luna.carmo.vinicius.letscodegateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
    @GetMapping("/defaultFallback")
    public String defaultMessage(){
        return "There were some error while connecting to app. Please try again later";
    }
}
