package springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import springboot.dto.Msg;

@Controller
public class IndexController {
	
	@Value("${application.hello:Hello Friend}")
	private String hello;
	
	@RequestMapping("/")
	public String index(Model model){
		Msg msg = new Msg("测试标题", "测试内容", "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
		model.addAttribute("hello", hello);
		return "index";
	}

}
