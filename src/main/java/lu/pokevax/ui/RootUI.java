package lu.pokevax.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.ui.helpers.GlobalErrorHandler;
import lu.pokevax.ui.helpers.SessionWrapper;

@SpringUI(path = "") // Root path
@Theme("valo")
@RequiredArgsConstructor
@Slf4j
public class RootUI extends UI {

    private final ViewProvider viewProvider;
    private final SessionWrapper sessionWrapper;
    private final GlobalErrorHandler globalErrorHandler;

    @Override
    protected void init(VaadinRequest request) {
        setErrorHandler(globalErrorHandler);

        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);

        // Automatically show login
        if (!sessionWrapper.isAuthenticated()) {
            getNavigator().navigateTo("login");
        } else {
            getNavigator().navigateTo("dashboard");
        }
    }
}
