package ifonlygram.rest;

import ifonlygram.dto.BlogCategory;
import ifonlygram.dto.Profile;
import ifonlygram.dto.RequestInfo;
import ifonlygram.service.ProfileGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class IfOnlyGramRest {

    @Autowired
    private ProfileGenerateService profileGenerateService;

    @RequestMapping(value = "/generateProfile", method = GET)
    public Profile getProfile(@RequestParam("name") final String name, @RequestParam("blogCategory") final BlogCategory blogCategory) {
        return profileGenerateService.generateProfile(name, blogCategory);
    }

    @RequestMapping(value = "/testAPI", method = GET)
    public String testApi() {
        return "BOO!!";
    }
}
