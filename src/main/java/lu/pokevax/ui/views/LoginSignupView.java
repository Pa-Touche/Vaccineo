package lu.pokevax.ui.views;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lu.pokevax.business.user.UserController;
import lu.pokevax.business.user.login.LoginController;
import lu.pokevax.business.user.login.LoginRequest;
import lu.pokevax.business.user.login.LoginResponse;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.ui.helpers.HttpClientHelper;
import lu.pokevax.ui.helpers.SessionWrapper;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SpringView(name = LoginSignupView.VIEW_PATH)
public class LoginSignupView extends VerticalLayout implements View {

    public static final String VIEW_PATH = "login";

    private final HttpClientHelper httpClientHelper;
    private final SessionWrapper sessionWrapper;


    public LoginSignupView(HttpClientHelper httpClientHelper, SessionWrapper sessionWrapper) {
        this.httpClientHelper = httpClientHelper;
        this.sessionWrapper = sessionWrapper;

        setSizeFull();
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setSpacing(true);
        setMargin(true);

        // 💡 Add App Title
        Label title = new Label("Pokevax");
        title.setStyleName(ValoTheme.LABEL_H1 + " " + ValoTheme.LABEL_COLORED);
        title.setHeight("50%");
        addComponent(title);

        // 🧱 Wider tab container
        TabSheet tabs = new TabSheet();
        tabs.setWidth("600px"); // wider
        tabs.setHeight("100%");

        VerticalLayout loginTab = buildLoginTab();
        VerticalLayout signupTab = buildSignupTab();

        tabs.addTab(loginTab, "Login");
        tabs.addTab(signupTab, "Sign Up");
        tabs.setSelectedTab(loginTab);

        addComponent(tabs);
    }

    private VerticalLayout buildLoginTab() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidthFull();

        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Mot de passe");
        Button loginBtn = new Button("Connexion");

        // Consistent width and style
        email.setWidthFull();
        password.setWidthFull();
        loginBtn.setWidthFull();
        loginBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);

        loginBtn.addClickListener(clickEvent -> {
            performLogin(StringUtils.toRootLowerCase(StringUtils.trim(email.getValue())), password.getValue());
        });

        loginBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);


        layout.addComponents(email, password, loginBtn);
        return layout;
    }

    private void performLogin(String emailTextValue, String passwordTextValue) {
        HttpClientHelper.HttpResponse<LoginResponse> response = httpClientHelper.post(HttpClientHelper.PostRequest.<LoginRequest, LoginResponse>builder()
                .endpoint(LoginController.URI)
                .returnType(LoginResponse.class)
                .body(LoginRequest.builder()
                        .email(emailTextValue)
                        .password(passwordTextValue)
                        .build())
                .build());

        if (response.hasError()) {
            Notification.show(response.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        try {
            LoginResponse loginResponse = response.getData();
            sessionWrapper.setToken(loginResponse.getToken());
            sessionWrapper.setCurrentUserId(loginResponse.getUserId());
            getUI().getNavigator().navigateTo(DashboardView.VIEW_PATH);
        } catch (Exception ex) {
            Notification.show("La connexion a échoué", Notification.Type.ERROR_MESSAGE);
        }
    }

    private VerticalLayout buildSignupTab() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidthFull();

        TextField nameField = new TextField("Nom de famille");
        nameField.setRequiredIndicatorVisible(true);

        TextField surnameField = new TextField("Prénom");
        surnameField.setRequiredIndicatorVisible(true);

        DateField birthDateField = new DateField("Date de naissance");
        birthDateField.setRequiredIndicatorVisible(true);
        birthDateField.setDateFormat("dd/MM/yyyy");
        birthDateField.setRangeEnd(LocalDate.now());

        TextField emailField = new TextField("Email");
        emailField.setRequiredIndicatorVisible(true);

        PasswordField passwordField = new PasswordField("Mot de passe");
        passwordField.setRequiredIndicatorVisible(true);

        Button signupBtn = new Button("Créer un compte");
        Supplier<Stream<? extends AbstractField<? extends Serializable>>> fields = () -> Stream.of(nameField, surnameField, birthDateField, emailField, passwordField);

        fields.get().forEach(AbstractComponent::setWidthFull);

        signupBtn.addClickListener(e -> {
            // Basic validation
            if (fields.get()
                    .anyMatch(HasValue::isEmpty)) {
                Notification.show("Please fill in all fields", Notification.Type.WARNING_MESSAGE);
                return;
            }

            try {
                String emailTextValue = StringUtils.toRootLowerCase(StringUtils.trim(emailField.getValue()));
                String passwordTextValue = passwordField.getValue();
                HttpClientHelper.HttpResponse<Integer> response = httpClientHelper.post(HttpClientHelper.PostRequest.<CreateUserRequest, Integer>builder()
                        .endpoint(UserController.URI)
                        .body(CreateUserRequest.builder()
                                .name(nameField.getValue())
                                .surname(surnameField.getValue())
                                .birthDate(birthDateField.getValue())
                                .email(emailTextValue)
                                .password(passwordTextValue)
                                .build())
                        .returnType(Integer.class)
                        .build());

                if (response.hasError()) {
                    Notification.show("Le compte n'as pas pu être créer: " + response.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
                    return;
                }

                Notification.show("Le compte à été créer avec succès", Notification.Type.TRAY_NOTIFICATION);

//                UI.getCurrent().getNavigator().navigateTo(LoginSignupView.VIEW_PATH);

                performLogin(emailTextValue, passwordTextValue);

            } catch (Exception ex) {
                Notification.show("Le compte n'as pas pu être créer: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        // TODO: this doesn't work: I tried layout/button and same as approach as for signup
        layout.addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                signupBtn.click();
            }
        });

        layout.addComponents(
                nameField,
                surnameField,
                birthDateField,
                emailField,
                passwordField,
                signupBtn
        );

        return layout;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // Optionally reset form on enter
    }
}
