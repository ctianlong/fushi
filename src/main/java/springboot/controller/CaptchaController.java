package springboot.controller;

import java.awt.Font;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springboot.utils.GifCaptcha;

/**
 * Created with IntelliJ IDEA.
 * User: 陈浩翔.
 * Date: 2017/3/6.
 * Time: 下午 8:26.
 * Explain:演示GIF验证码的控制器
 */
@Controller
public class CaptchaController {
    private Logger logger = LoggerFactory.getLogger(CaptchaController.class);
    /**
     * 获取Gif验证码
     * @param response
     */
    @RequestMapping(value = "/gifCaptcha", method = RequestMethod.GET)
    public void getGifCaptcha(HttpServletResponse response,HttpServletRequest request){
        //告诉客户端，输出的格式
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/gif");
        GifCaptcha gifCaptcha =  new GifCaptcha(200,80,new Font("宋体", Font.BOLD, 40),100);
        try {
        	HttpSession session = request.getSession();
            gifCaptcha.out(response.getOutputStream());
            //存入Session
            session.setAttribute("captchaWord",gifCaptcha.getWord());
            logger.info("获取验证码！验证码字符为："+gifCaptcha.getWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
