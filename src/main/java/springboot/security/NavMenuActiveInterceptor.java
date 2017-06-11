package springboot.security;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * @author Tianlong
 *
 */
public class NavMenuActiveInterceptor extends HandlerInterceptorAdapter {
	
	private static final Set<String> MENU_URLS = new HashSet<>();
	
	static {
		MENU_URLS.addAll(Arrays.asList("/students/info", "/students/score", "/users", "/users/teachers"));
	}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	if (SecurityUtil.isCurrentUserInRole(RoleConstants.ADMIN)) {
    		String currentUrl=request.getServletPath();
    		MENU_URLS.parallelStream().filter((s) -> currentUrl.startsWith(s))
    			.findFirst().ifPresent((s) -> request.setAttribute("currentMenu", s));
    	}
        return super.preHandle(request, response, handler);
    }
}
