package springboot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {
	
	@RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8")
	public String index(){
		return "Hello World";
	}

}
