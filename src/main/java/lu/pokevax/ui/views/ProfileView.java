package lu.pokevax.ui.views;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import lu.pokevax.business.user.UserController;
import lu.pokevax.business.user.responses.UserResponse;
import lu.pokevax.ui.components.ComponentsFactory;
import lu.pokevax.ui.helpers.HttpClientHelper;
import lu.pokevax.ui.helpers.SessionWrapper;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;

@SpringView(name = ProfileView.UI_PATH)
@Title("Mon Profil | Pokevax")
@RequiredArgsConstructor
public class ProfileView extends VerticalLayout implements View {

    public static final String UI_PATH = "profile";

    private final HttpClientHelper httpClientHelper;
    private final SessionWrapper sessionWrapper;

    @PostConstruct
    public void init() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        addComponent(ComponentsFactory.menuBar(ComponentsFactory.MenuEntry.PROFILE));

        Label title = new Label("Mon profil utilisateur");
        title.addStyleName(ValoTheme.LABEL_H1);
        addComponent(title);

        FormLayout form = new FormLayout();
        form.setWidthFull();

        Button deleteBtn = null;
        try {
            HttpClientHelper.HttpResponse<UserResponse> response = httpClientHelper.get(HttpClientHelper.GetRequest.<UserResponse>builder()
                    .endpoint(String.format("%s/%s", UserController.URI, sessionWrapper.getCurrentUserId()))
                    .returnType(UserResponse.class)
                    .build());

            UserResponse user = response.getData();

            Label lastName = new Label(user.getName());
            lastName.setCaption("Nom de famille");
            form.addComponent(lastName);

            Label surname = new Label(user.getSurname());
            surname.setCaption("Prénom");
            form.addComponent(surname);

            Label birthDate = new Label(user.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            birthDate.setCaption("Date de naissance");
            form.addComponent(birthDate);

            Label email = new Label(user.getEmail());
            email.setCaption("Email");
            form.addComponent(email);

            form.addComponents(lastName, surname, birthDate, email);

            deleteBtn = new Button("Supprimer mon compte", evt -> {
                try {
                    httpClientHelper.delete(UserController.URI + String.format("/%s", user.getId()));
                    Notification.show("Compte supprimé", Notification.Type.WARNING_MESSAGE);
                    UI.getCurrent().getPage().setLocation("/");
                } catch (Exception e) {
                    Notification.show("Échec de la suppression: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            });
            deleteBtn.addStyleName(ValoTheme.BUTTON_DANGER);
        } catch (Exception e) {
            Notification.show("Erreur lors du chargement du profil: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }


        HorizontalLayout topLayout = new HorizontalLayout(form, deleteBtn);
        topLayout.setWidth("60%");
        topLayout.setComponentAlignment(deleteBtn, Alignment.TOP_RIGHT);
        topLayout.setExpandRatio(form, 1);  // form takes all available space

        addComponent(topLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // Called when entering the view
    }
}
