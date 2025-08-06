package lu.vaccineo.ui;

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
import lu.vaccineo.ui.helpers.GlobalErrorHandler;
import lu.vaccineo.ui.helpers.SessionWrapper;

import java.time.format.DateTimeFormatter;

@SpringUI(path = "") // Root path
@Theme("valo")
@RequiredArgsConstructor
@Slf4j
@Push
public class RootUI extends UI {

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

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


        // TODO: enhance this by using adequate css files
        Page.getCurrent().getStyles().add(
                ".menu-selected { font-weight: bold !important; text-decoration: underline; }" +
                        ".v-menubar .v-menubar-menuitem { width: 50%; text-align: center; }" +
                        ".no-margin-top { margin-top: 0 !important; }" +

                        ".notification-bell-container {" +
                        "position: relative;" +
                        "cursor: pointer;" +
                        "}" +
                        ".notification-bell {" +
                        "font-size: 24px;" +
                        "color: #444;" +
                        "}" +
                        ".notification-badge {" +
                        "position: absolute;" +
                        "top: 40px;" +
                        "right: -5px;" +
                        "background-color: red;" +
                        "color: white;" +
                        "border-radius: 50%;" +
                        "font-size: 12px;" +
                        "width: 18px;" +
                        "height: 18px;" +
                        "line-height: 18px;" +
                        "text-align: center;" +
                        "font-weight: bold;" +
                        "pointer-events: none;" +
                        "}"
        );
    }
}
