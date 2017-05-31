package springboot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }
    
    /**
     * get current user
     * @return
     */
    public static SecurityUser getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
        	return (SecurityUser) authentication.getPrincipal();
        }
        return null;
    }
    
    /**
     * Get the uid of the current user.
     *
     * @return the login of the current user
     */
    public static Long getCurrentUid() {
    	SecurityUser user = getCurrentUser();
        return user == null ? null : user.getUid();
    }

    /**
     * Get the username of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentUsername() {
    	SecurityUser user = getCurrentUser();
        return user == null ? null : user.getUsername();
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
        	return authentication.isAuthenticated();
        }
        return false;
    }

    /**
     * If the current user has a specific authority (security role).
     *
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
        }
        return false;
    }
}
