/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package lu.vaccineo.ui.views;

import com.vaadin.annotations.Title;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import lu.vaccineo.business.notification.VaccineNotificationResponse;
import lu.vaccineo.business.notification.VaccineNotificationResponseWrapper;
import lu.vaccineo.business.vaccine.VaccineController;
import lu.vaccineo.business.vaccine.administered.VaccineSortableField;
import lu.vaccineo.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.vaccineo.business.vaccine.administered.requests.SearchVaccineCriteria;
import lu.vaccineo.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.vaccineo.business.vaccine.administered.requests.SortRequest;
import lu.vaccineo.business.vaccine.administered.responses.AdministeredVaccineResponse;
import lu.vaccineo.business.vaccine.administered.responses.AdministeredVaccineResponseWrapper;
import lu.vaccineo.business.vaccine.administered.responses.VaccineTypeResponseWrapper;
import lu.vaccineo.ui.RootUI;
import lu.vaccineo.ui.components.ComponentsFactory;
import lu.vaccineo.ui.helpers.ErrorNotificationUtils;
import lu.vaccineo.ui.helpers.HttpClientHelper;
import lu.vaccineo.ui.helpers.SessionWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringView(name = DashboardView.VIEW_PATH)
@Title("Dashboard | Vaccineo")
@RequiredArgsConstructor
public class DashboardView extends VerticalLayout implements View {

    public static final String VIEW_PATH = "dashboard";
    public static final SearchVaccineRequest DEFAULT_SEARCH = new SearchVaccineRequest();
    public static final int LIMIT_VACCINES_SHOW_IN_NOTIFICATION = 5;

    private final HttpClientHelper httpClientHelper;

    private final SessionWrapper sessionWrapper;

    private final Grid<AdministeredVaccineResponse> vaccineGrid = new Grid<>(AdministeredVaccineResponse.class);

    @PostConstruct
    public void init() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        addComponent(ComponentsFactory.menuBar(ComponentsFactory.MenuEntry.VACCINE_DASHBOARD));

        Label title = new Label("Vaccins effectués");
        title.addStyleName(ValoTheme.LABEL_H1);

        Button addVaccineBtn = new Button("Ajouter un vaccin");
        addVaccineBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addVaccineBtn.addClickListener(ignored -> openAddVaccineModal());

        ComboBox<String> filterVaccineName = buildVaccineNameSelector();
        filterVaccineName.setPlaceholder("Filtrer par nom");
        filterVaccineName.setWidth("590px");

        DateField filterAdministeredDate = buildFilterDate();
        filterAdministeredDate.setWidth("430px");

        TextField filterDoseNumber = ComponentsFactory.numberOnlyField();
        filterDoseNumber.setPlaceholder("Filtrer par numéro de dose");
        filterDoseNumber.setWidth("403px");

        Button applyFilters = buildApplyFiltersButton(filterVaccineName, filterAdministeredDate, filterDoseNumber);

        applyFilters.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        Button resetFilters = new Button("Réinitialiser les filtres", e -> {
            filterVaccineName.clear();
            filterAdministeredDate.clear();
            filterDoseNumber.clear();

            applyFilters.click();
        });

        HorizontalLayout filtersLayout = new HorizontalLayout(filterVaccineName, filterAdministeredDate, filterDoseNumber, applyFilters, resetFilters);

        HorizontalLayout header = buildHeader(title, addVaccineBtn);

        addComponents(header, filtersLayout, vaccineGrid);
        setExpandRatio(vaccineGrid, 1);

        configureGrid();
        loadVaccines();

        // must be performed in async manner to not block the main thread
        UI.getCurrent().access(() -> fetchAndDisplayNotifications(header));
    }

    private Component createNotificationBell(List<VaccineNotificationResponse> notifications) {
        CssLayout wrapper = new CssLayout();
        wrapper.addStyleName("notification-bell-wrapper");

        Button bell = new Button(VaadinIcons.BELL);
        bell.addStyleName("notification-bell");
        bell.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        bell.setDescription("Voir les notifications");

        Label badge = new Label(String.valueOf(notifications.size()));
        badge.addStyleName("notification-badge");

        wrapper.addComponents(bell, badge);

        bell.addClickListener(e -> showNotificationsModal(notifications));

        return wrapper;
    }

    private void showNotificationsModal(List<VaccineNotificationResponse> notifications) {
        Window modal = new Window("Notifications de vaccination");
        modal.setModal(true);
        modal.setResizable(false);
        modal.setClosable(true);
        modal.setWidth("1000px");

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);

        if (notifications.isEmpty()) {
            return;
        }

        for (VaccineNotificationResponse notif : notifications) {
            Label label = new Label(formatNotificationText(notif));
            label.setWidthFull();
            content.addComponent(label);
        }


        modal.setContent(content);
        UI.getCurrent().addWindow(modal);
    }

    private static HorizontalLayout buildHeader(Label title, Button addVaccineBtn) {
        HorizontalLayout header = new HorizontalLayout(title, addVaccineBtn);

        header.setWidthFull();
        header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(addVaccineBtn, Alignment.MIDDLE_RIGHT);
        header.setExpandRatio(title, 1);

        return header;
    }

    private Button buildApplyFiltersButton(ComboBox<String> filterName, DateField filterDate, TextField filterDose) {
        return new Button("Appliquer les filtres", e -> {
            SearchVaccineCriteria criteria = SearchVaccineCriteria.builder()
                    .vaccineName(StringUtils.isBlank(filterName.getValue()) ? null : filterName.getValue())
                    .administrationDate(filterDate.getValue())
                    .doseNumber(parseDoseNumber(filterDose.getValue()))
                    .build();

            SearchVaccineRequest req = SearchVaccineRequest.builder()
                    .criteria(criteria)
                    .build();

            loadVaccines(req);
        });
    }

    private static DateField buildFilterDate() {
        DateField filterDate = new DateField();

        filterDate.setDateFormat(RootUI.DATE_FORMAT);
        filterDate.setPlaceholder("Filtrer par date");

        return filterDate;
    }

    /**
     * Notifications are called asyncronously to not block the main thread.
     */
    private void fetchAndDisplayNotifications(HorizontalLayout header) {
        UI current = UI.getCurrent();

        String baseUrl = HttpClientHelper.getBaseUrl();

        String token = sessionWrapper.getToken();
        Boolean wasVaccineNotificationAlreadyDisplayed = sessionWrapper.getVaccineNotificationAlreadyDisplayed();

        CompletableFuture.runAsync(() -> {
            try {
                HttpClientHelper.HttpResponse<VaccineNotificationResponseWrapper> response = httpClientHelper.get(
                        HttpClientHelper.GetRequest.<VaccineNotificationResponseWrapper>builder()
                                .fullEndpoint(baseUrl + "/api/vaccines/notifications")
                                .returnType(VaccineNotificationResponseWrapper.class)
                                .token(token)
                                .build()
                );

                List<VaccineNotificationResponse> notifications = response.getData().getContent();


                if (CollectionUtils.isEmpty(notifications)) {
                    return;
                }

                StringBuilder message = new StringBuilder(String.format("Vous avez %s vaccins à effectuer.\n", notifications.size()));

                notifications.stream()
                        .limit(LIMIT_VACCINES_SHOW_IN_NOTIFICATION)
                        .forEach(notification -> message.append(formatNotificationText(notification)));

                if (notifications.size() > LIMIT_VACCINES_SHOW_IN_NOTIFICATION) {
                    message.append("\n\n... Cliquer sur la cloche pour tous les visualiser");
                }


                current.access(() -> {
                    Component notificationBell = createNotificationBell(notifications);
                    header.addComponent(notificationBell);
                    header.setComponentAlignment(notificationBell, Alignment.MIDDLE_LEFT);

                    if (!wasVaccineNotificationAlreadyDisplayed) {
                        Notification notification = new Notification("Notifications de vaccins", message.toString(), Notification.Type.TRAY_NOTIFICATION);
                        notification.setDelayMsec(6000);
                        notification.show(Page.getCurrent());
                    }

                    sessionWrapper.setVaccineNotificationAlreadyDisplayed(true);
                });

            } catch (Exception e) {
                current.access(() ->
                        Notification.show("Erreur de chargement des notifications: " + e.getMessage(), Notification.Type.ERROR_MESSAGE)
                );
            }
        });
    }

    private static String formatNotificationText(VaccineNotificationResponse notification) {
        return String.format("- Dose %s du vaccin %s avant %s\n", notification.getDoseNumber(), notification.getVaccineName(), RootUI.FORMATTER.format(notification.getDeadline()));
    }

    private void configureGrid() {
        String vaccineNameColumn = "vaccineName";
        String administrationDateColumn = "administrationDate";
        String doseNumberColumn = "doseNumber";
        String commentColumn = "comment";

        vaccineGrid.setColumns(vaccineNameColumn, administrationDateColumn, doseNumberColumn, commentColumn);

        vaccineGrid.getColumn(vaccineNameColumn).setCaption("Nom du vaccin");
        vaccineGrid.getColumn(administrationDateColumn).setCaption("Date d'administration");
        vaccineGrid.getColumn(doseNumberColumn).setCaption("Numéro de dose");
        vaccineGrid.getColumn(commentColumn).setCaption("Commentaire");

        vaccineGrid.setColumnReorderingAllowed(true);
        vaccineGrid.setSelectionMode(Grid.SelectionMode.NONE);

        vaccineGrid.setSizeFull();

        HashMap<String, VaccineSortableField> dictionary = new HashMap<>();
        dictionary.put(vaccineNameColumn, VaccineSortableField.VACCINE_NAME);
        dictionary.put(administrationDateColumn, VaccineSortableField.ADMINISTRATION_DATE);
        dictionary.put(doseNumberColumn, VaccineSortableField.DOSE_NUMBER);
        dictionary.put(commentColumn, VaccineSortableField.COMMENT);

        // Add sorting listener
        addSortListenerOnVaccineGrid(dictionary);
    }

    private void addSortListenerOnVaccineGrid(HashMap<String, VaccineSortableField> dictionary) {
        vaccineGrid.addSortListener(event -> {
            if (!event.getSortOrder().isEmpty()) {
                GridSortOrder<AdministeredVaccineResponse> order = event.getSortOrder().get(0);
                String sortProperty = order.getSorted().getId(); // should match VaccineSortableField

                Sort.Direction direction = order.getDirection() == SortDirection.ASCENDING
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

                SearchVaccineRequest request = SearchVaccineRequest.builder()
                        .sort(SortRequest.builder()
                                .direction(direction)
                                .fields(Collections.singletonList(
                                        dictionary.get(sortProperty) // ensure names match!
                                ))
                                .build())
                        .build();

                loadVaccines(request);
            }
        });
    }

    private Integer parseDoseNumber(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null; // or show a warning
        }
    }


    private void loadVaccines() {
        loadVaccines(DEFAULT_SEARCH);
    }

    private void loadVaccines(SearchVaccineRequest request) {
        try {
            HttpClientHelper.HttpResponse<AdministeredVaccineResponseWrapper> response = httpClientHelper.post(HttpClientHelper.PostRequest.<SearchVaccineRequest, AdministeredVaccineResponseWrapper>builder()
                    .endpoint(VaccineController.URI + "/search")
                    .body(request)
                    .returnType(AdministeredVaccineResponseWrapper.class)
                    .build());
            // TODO: handle error.

            vaccineGrid.setDataProvider(new ListDataProvider<>(response.getData().getContent()));
        } catch (Exception e) {
            Notification.show("Failed to load vaccines: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void openAddVaccineModal() {
        Window modal = new Window("Ajouter un vaccin");
        modal.setModal(true);
        modal.setWidth("60%");

        FormLayout form = new FormLayout();

        ComboBox<String> vaccineNameSelector = buildVaccineNameSelector("Nom vaccin");

        DateField adminDate = buildAdministeredDateField();

        TextField doseNumber = ComponentsFactory.numberOnlyField("Numéro de la dose");

        TextField comment = new TextField("Commentaire");

        Stream.of(
                vaccineNameSelector,
                adminDate,
                doseNumber,
                comment
        ).forEach(component -> {
            component.setWidthFull();

            if (component != comment) {
                ((HasValue) component).setRequiredIndicatorVisible(true);
            }
        });

        Button submit = new Button("Sauver");
        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submit.setWidth("30%");

        submit.addClickListener(event -> {
            try {
                if (Stream.of(
                                adminDate,
                                doseNumber
                        )
                        .anyMatch(HasValue::isEmpty)) {
                    Notification.show("Les champs marqués d'une étoile sont obligatoires", Notification.Type.WARNING_MESSAGE);
                    return;
                }

                HttpClientHelper.HttpResponse<Integer> response = httpClientHelper.post(HttpClientHelper.PostRequest.<CreateAdministeredVaccineRequest, Integer>builder()
                        .endpoint(VaccineController.URI)
                        .body(CreateAdministeredVaccineRequest.builder()
                                .administrationDate(adminDate.getValue())
                                .doseNumber(Integer.parseInt(doseNumber.getValue()))
                                .comment(comment.getValue())
                                .vaccineName(vaccineNameSelector.getValue())
                                .build())
                        .returnType(Integer.class)
                        .build());

                if (response.hasError()) {
                    Map<String, String> validationError = response.getValidationError();
                    if (MapUtils.isNotEmpty(validationError)) {
                        Notification.show("Le vaccin n'a pas pu être créer car la validation de vos données à échoué, voici le résultat de la validation:\n" + ErrorNotificationUtils.buildReadableValidationError(validationError), Notification.Type.ERROR_MESSAGE);
                    } else {
                        Notification.show(response.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
                    }

                    return;
                }

                Notification.show("Vaccin ajouté avec succès", Notification.Type.TRAY_NOTIFICATION);
                modal.close();

                // once a new vaccine was added, simply call the backend again.
                loadVaccines();
            } catch (Exception ex) {
                Notification.show("Le vaccin n'a pas pu être enregistré: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        form.addComponents(vaccineNameSelector, adminDate, doseNumber, comment, submit);

        modal.setContent(form);
        UI.getCurrent().addWindow(modal);
    }



    private static DateField buildAdministeredDateField() {
        DateField adminDate = new DateField("Date d'administration");
        adminDate.setRangeEnd(LocalDate.now());
        adminDate.setDateFormat(RootUI.DATE_FORMAT);
        adminDate.setWidthFull();
        return adminDate;
    }

    private ComboBox<String> buildVaccineNameSelector() {
        return buildVaccineNameSelector(null);
    }

    private ComboBox<String> buildVaccineNameSelector(String caption) {
        ComboBox<String> vaccineName = new ComboBox<>(caption);

        vaccineName.setEmptySelectionAllowed(false);  // force user to select

        fillVaccineNamesOn(vaccineName);

        return vaccineName;
    }

    /**
     * Call is done to backend to fetch Vaccine names: could be done async.
     *
     * @param vaccineName combox to set values in.
     */
    private void fillVaccineNamesOn(ComboBox<String> vaccineName) {
        try {
            HttpClientHelper.HttpResponse<VaccineTypeResponseWrapper> response = httpClientHelper.get(HttpClientHelper.GetRequest.<VaccineTypeResponseWrapper>builder()
                    .endpoint(String.format("%s/types", VaccineController.URI))
                    .returnType(VaccineTypeResponseWrapper.class)
                    .build());
            vaccineName.setItems(response.getData().getContent());
        } catch (Exception e) {
            Notification.show("Failed to load vaccine names: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // View entry point if needed
    }
}