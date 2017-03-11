package springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@Value("${application.hello:Hello Friend}")
	private String hello;
	
	@RequestMapping("/")
	public String index(ModelMap map){
		map.addAttribute("hello", hello);
		return "index";
	}

}
