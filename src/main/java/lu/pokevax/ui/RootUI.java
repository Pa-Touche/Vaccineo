package lu.pokevax.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
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
@Push
public class RootUI extends UI {

    public static final String DATE_FORMAT = "dd/MM/yyyy";

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

        // not perfect, somewhat still does some calls.
        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                boolean isAuthenticated = sessionWrapper.isAuthenticated();
                String viewName = event.getViewName();

                if (!isAuthenticated && !viewName.equals("login")) {
                    navigator.navigateTo("login");
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {
                // Optional: actions after view change
            }
        });


        Page.getCurrent().getStyles().add(
                ".menu-selected { font-weight: bold !important; text-decoration: underline; }" +
                        ".v-menubar .v-menubar-menuitem { width: 50%; text-align: center; }" +
                        ".no-margin-top { margin-top: 0 !important; }"
        );
    }
}
