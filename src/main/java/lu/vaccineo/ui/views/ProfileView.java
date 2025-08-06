package lu.vaccineo.ui.views;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import lu.vaccineo.business.user.UserController;
import lu.vaccineo.business.user.responses.UserResponse;
import lu.vaccineo.ui.components.ComponentsFactory;
import lu.vaccineo.ui.helpers.HttpClientHelper;
import lu.vaccineo.ui.helpers.SessionWrapper;

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
        title.addStyleName("no-margin-top");

        FormLayout form = new FormLayout();
        form.setWidthFull();

        Button deleteBtn = null;
        try {
            HttpClientHelper.HttpResponse<UserResponse> response = httpClientHelper.get(HttpClientHelper.GetRequest.<UserResponse>builder()
                    .endpoint(String.format("%s/%s", UserController.URI, sessionWrapper.getCurrentUserId()))
                    .returnType(UserResponse.class)
                    .build());

            setUserDataIntoFormLabels(response.getData(), form);

            deleteBtn = buildDeleteUserBtn(response.getData());
        } catch (Exception e) {
            Notification.show("Erreur lors du chargement du profil: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }

        HorizontalLayout header = new HorizontalLayout(title, deleteBtn);
        header.setWidthFull();
        header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        header.setMargin(false);
        header.setExpandRatio(title, 1);

        addComponent(header);

        addComponent(form);
    }

    private static void setUserDataIntoFormLabels(UserResponse user, FormLayout form) {
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
    }

    private Button buildDeleteUserBtn(UserResponse user) {
        Button deleteBtn;
        deleteBtn = new Button("Supprimer mon compte", evt -> {
            try {

                Window confirmDialog = buildConfirmationDialog();

                VerticalLayout dialogLayout = buildVerticalLayout();

                Label message = new Label("Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irrévocable.");
                message.setWidth("100%");

                Button userDeletionBtn = buildConfirmUserDeletionBtn(user, confirmDialog);

                Button buildCancelDeletionBtn = buildCancelDeletionBtn(confirmDialog);

                HorizontalLayout buttons = new HorizontalLayout(userDeletionBtn, buildCancelDeletionBtn);
                buttons.setSpacing(true);

                dialogLayout.addComponents(message, buttons);
                confirmDialog.setContent(dialogLayout);

                UI.getCurrent().addWindow(confirmDialog);

            } catch (Exception e) {
                Notification.show("Échec de la suppression: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        deleteBtn.addStyleName(ValoTheme.BUTTON_DANGER);

        return deleteBtn;
    }

    private static Button buildCancelDeletionBtn(Window confirmDialog) {
        Button cancelBtn = new Button("Annuler");
        cancelBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        cancelBtn.addClickListener(event -> confirmDialog.close());
        return cancelBtn;
    }

    private Button buildConfirmUserDeletionBtn(UserResponse user, Window confirmDialog) {
        Button confirmBtn = new Button("Oui, supprimer");
        confirmBtn.addStyleName(ValoTheme.BUTTON_DANGER);

        confirmBtn.addClickListener(event -> {
            try {
                httpClientHelper.delete(UserController.URI + String.format("/%s", user.getId()));
                Notification.show("Compte supprimé", Notification.Type.WARNING_MESSAGE);
                confirmDialog.close();
                UI.getCurrent().getNavigator().navigateTo(LoginSignupView.VIEW_PATH);

            } catch (Exception e) {
                Notification.show("Échec de la suppression: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                confirmDialog.close();
            }
        });
        return confirmBtn;
    }

    private static VerticalLayout buildVerticalLayout() {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setMargin(true);
        dialogLayout.setSpacing(true);
        return dialogLayout;
    }

    private static Window buildConfirmationDialog() {
        Window confirmDialog = new Window("Confirmation");
        confirmDialog.setModal(true);
        confirmDialog.setClosable(false);
        confirmDialog.setResizable(false);
        confirmDialog.setWidth("400px");
        return confirmDialog;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // Called when entering the view
    }
}
