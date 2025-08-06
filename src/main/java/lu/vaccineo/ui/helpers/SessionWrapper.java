package lu.vaccineo.ui.helpers;

import com.vaadin.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SessionWrapper {
    private static final String TOKEN_ATTRIBUTE_KEY = "authToken";
    private static final String CURRENT_USER_ATTRIBUTE_KEY = "currentUser";


    public Integer getCurrentUserId() {
        return (Integer) VaadinSession.getCurrent().getAttribute(CURRENT_USER_ATTRIBUTE_KEY);
    }

    public void setCurrentUserId(Integer currentUserId) {
        VaadinSession.getCurrent().setAttribute(CURRENT_USER_ATTRIBUTE_KEY, currentUserId);
    }

    public String getToken() {
        return (String) VaadinSession.getCurrent().getAttribute(TOKEN_ATTRIBUTE_KEY);
    }

    public void setToken(String token) {
        VaadinSession.getCurrent().setAttribute(TOKEN_ATTRIBUTE_KEY, token);
    }

    public boolean isAuthenticated() {
        return getToken() != null;
    }
}
