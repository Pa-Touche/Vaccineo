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
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
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
import lu.vaccineo.ui.helpers.HttpClientHelper;
import lu.vaccineo.ui.helpers.SessionWrapper;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SpringView(name = DashboardView.VIEW_PATH)
@Title("Dashboard | Vaccineo")
@RequiredArgsConstructor
public class DashboardView extends VerticalLayout implements View {

    public static final String VIEW_PATH = "dashboard";
    public static final SearchVaccineRequest DEFAULT_SEARCH = new SearchVaccineRequest();

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

        TextField filterVaccineName = new TextField();
        filterVaccineName.setPlaceholder("Filtrer par nom");

        DateField filterAdministeredDate = buildFilterDate();

        TextField filterDoseNumber = new TextField();
        filterDoseNumber.setPlaceholder("Filtrer par dose");

        Button applyFilters = buildApplyFiltersButton(filterVaccineName, filterAdministeredDate, filterDoseNumber);

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
        UI.getCurrent().access(this::fetchAndDisplayNotifications);
    }

    private static HorizontalLayout buildHeader(Label title, Button addVaccineBtn) {
        HorizontalLayout header = new HorizontalLayout(title, addVaccineBtn);

        header.setWidthFull();
        header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(addVaccineBtn, Alignment.MIDDLE_RIGHT);
        header.setExpandRatio(title, 1);

        return header;
    }

    private Button buildApplyFiltersButton(TextField filterName, DateField filterDate, TextField filterDose) {
        return new Button("Appliquer les filtres", e -> {
            SearchVaccineCriteria criteria = SearchVaccineCriteria.builder()
                    .vaccineName(filterName.getValue().isEmpty() ? null : filterName.getValue())
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
    private void fetchAndDisplayNotifications() {
        UI current = UI.getCurrent();

        String baseUrl = HttpClientHelper.getBaseUrl();

        String token = sessionWrapper.getToken();

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
                if (notifications != null && !notifications.isEmpty()) {
                    StringBuilder message = new StringBuilder("Rappels de vaccination:\n");

                    for (VaccineNotificationResponse notification : notifications) {
                        message.append("- ").append(String.format("Dose %s du vaccin %s avant %s", notification.getDoseNumber(), notification.getVaccineName(), notification.getDeadline())).append("\n");
                    }

                    current.access(() ->
                            Notification.show("Notifications de vaccins", message.toString(), Notification.Type.TRAY_NOTIFICATION)
                    );
                }

            } catch (Exception e) {
                current.access(() ->
                        Notification.show("Erreur de chargement des notifications: " + e.getMessage(), Notification.Type.ERROR_MESSAGE)
                );
            }
        });
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

        ComboBox<String> vaccineNameSelector = buildVaccineNameSelector();

        DateField adminDate = buildAdministeredDateField();

        TextField doseNumber = new TextField("Numéro de la dose");

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

                httpClientHelper.post(HttpClientHelper.PostRequest.<CreateAdministeredVaccineRequest, Integer>builder()
                        .endpoint(VaccineController.URI)
                        .body(CreateAdministeredVaccineRequest.builder()
                                .administrationDate(adminDate.getValue())
                                .doseNumber(Integer.parseInt(doseNumber.getValue()))
                                .comment(comment.getValue())
                                .vaccineName(vaccineNameSelector.getValue())
                                .build())
                        .returnType(Integer.class)
                        .build());


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
        ComboBox<String> vaccineName = new ComboBox<>("Nom vaccin");

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