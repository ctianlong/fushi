package springboot.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloResource {
	
	@RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8")
	public String sayHello(){
		return "Hello World";
	}

}
