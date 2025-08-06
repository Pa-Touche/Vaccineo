package lu.vaccineo.ui.views;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lu.vaccineo.business.user.UserController;
import lu.vaccineo.business.user.login.LoginController;
import lu.vaccineo.business.user.login.LoginRequest;
import lu.vaccineo.business.user.login.LoginResponse;
import lu.vaccineo.business.user.requests.CreateUserRequest;
import lu.vaccineo.ui.RootUI;
import lu.vaccineo.ui.helpers.ErrorNotificationUtils;
import lu.vaccineo.ui.helpers.HttpClientHelper;
import lu.vaccineo.ui.helpers.SessionWrapper;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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

        addComponent(buildTitle());

        TabSheet tabs = new TabSheet();
        tabs.setWidth("600px");
        tabs.setHeight("100%");

        VerticalLayout loginTab = buildLoginTab();
        VerticalLayout signupTab = buildSignupTab();

        tabs.addTab(loginTab, "Login");
        tabs.addTab(signupTab, "Créer un compte");
        tabs.setSelectedTab(loginTab);

        addComponent(tabs);
    }

    private static Label buildTitle() {
        Label title = new Label("Vaccineo");

        title.setStyleName(ValoTheme.LABEL_H1 + " " + ValoTheme.LABEL_COLORED);
        title.setHeight("50%");

        return title;
    }

    private VerticalLayout buildLoginTab() {
        VerticalLayout layout = new VerticalLayout();

        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidthFull();

        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Mot de passe");
        Button loginBtn = new Button("Connexion");

        Stream.of(email, password, loginBtn).forEach(AbstractComponent::setWidthFull);

        loginBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);

        loginBtn.addClickListener(clickEvent -> performLogin(StringUtils.toRootLowerCase(StringUtils.trim(email.getValue())), password.getValue()));

        // simulate same behavior when hitting enter, than when clicking loginBtn.
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

            Map<String, String> validationError = response.getValidationError();
            if (MapUtils.isNotEmpty(validationError)) {
                Notification.show("Certains champs ont été mal renseigné et ne respecte pas le format demandé, voici le résultat de la validation:\n" + ErrorNotificationUtils.buildReadableValidationError(validationError), Notification.Type.ERROR_MESSAGE);
            } else {
                Notification.show(response.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
            }

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

        TextField surnameField = new TextField("Prénom");

        DateField birthDateField = buildBirthDateField();

        TextField emailField = new TextField("Email");

        PasswordField passwordField = new PasswordField("Mot de passe");

        Button signupBtn = new Button("Créer un compte");
        Supplier<Stream<? extends AbstractField<? extends Serializable>>> fields = () -> Stream.of(nameField, surnameField, birthDateField, emailField, passwordField);

        fields.get().forEach(field -> {
            field.setWidthFull();
            field.setRequiredIndicatorVisible(true);
        });

        signupBtn.addClickListener(e -> {
            if (fields.get()
                    .anyMatch(HasValue::isEmpty)) {
                Notification.show("Tous les champs sont obligatoires", Notification.Type.WARNING_MESSAGE);
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
                    Map<String, String> validationError = response.getValidationError();
                    if (MapUtils.isNotEmpty(validationError)) {
                        Notification.show("Le compte n'a pas pu être créer car la validation de vos données à échoué, voici le résultat de la validation:\n" + ErrorNotificationUtils.buildReadableValidationError(validationError), Notification.Type.ERROR_MESSAGE);
                    } else {
                        Notification.show(response.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
                    }

                    return;
                }

                Notification.show("Le compte à été créer avec succès", Notification.Type.TRAY_NOTIFICATION);

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

    private static DateField buildBirthDateField() {
        DateField birthDateField = new DateField("Date de naissance");

        birthDateField.setRequiredIndicatorVisible(true);
        birthDateField.setDateFormat(RootUI.DATE_FORMAT);
        birthDateField.setRangeEnd(LocalDate.now());

        return birthDateField;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // Optionally reset form on enter
    }
}
