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
package lu.pokevax.ui.views;

import com.vaadin.annotations.Title;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import lu.pokevax.business.notification.VaccineNotificationResponse;
import lu.pokevax.business.notification.VaccineNotificationResponseWrapper;
import lu.pokevax.business.vaccine.VaccineController;
import lu.pokevax.business.vaccine.administered.VaccineSortableField;
import lu.pokevax.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineCriteria;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.pokevax.business.vaccine.administered.requests.SortRequest;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponse;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponseWrapper;
import lu.pokevax.business.vaccine.administered.responses.VaccineTypeResponseWrapper;
import lu.pokevax.ui.components.ComponentsFactory;
import lu.pokevax.ui.helpers.HttpClientHelper;
import lu.pokevax.ui.helpers.SessionWrapper;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringView(name = DashboardView.VIEW_PATH)
@Title("Dashboard | Pokevax")
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

        TextField filterName = new TextField();
        filterName.setPlaceholder("Filtrer par nom");

        DateField filterDate = new DateField();
        filterDate.setPlaceholder("Filtrer par date");

        TextField filterDose = new TextField();
        filterDose.setPlaceholder("Filtrer par dose");

        Button applyFilters = new Button("Appliquer les filtres", e -> {
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

        Button resetFilters = new Button("Réinitialiser les filtres", e -> {
            filterName.clear();
            filterDate.clear();
            filterDose.clear();

            applyFilters.click();
        });

        HorizontalLayout filtersLayout = new HorizontalLayout(filterName, filterDate, filterDose, applyFilters, resetFilters);

        HorizontalLayout header = new HorizontalLayout(title, addVaccineBtn);
        header.setWidthFull();

        header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(addVaccineBtn, Alignment.MIDDLE_RIGHT);
        header.setExpandRatio(title, 1);

        addComponents(header, filtersLayout, vaccineGrid);
        setExpandRatio(vaccineGrid, 1);

        configureGrid();
        loadVaccines();

        // must be performed in async manner to not block the main thread
        UI.getCurrent().access(this::fetchAndDisplayNotifications);
    }

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

                    // Update UI in the UI thread
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

        vaccineGrid.setSizeFull();
        String vaccineNameColumn = "vaccineName";
        String administrationDateColumn = "administrationDate";
        String doseNumberColumn = "doseNumber";
        String commentColumn = "comment";

        vaccineGrid.setColumns(vaccineNameColumn, administrationDateColumn, doseNumberColumn, commentColumn);

        HashMap<String, VaccineSortableField> dictionary = new HashMap<>();
        dictionary.put(vaccineNameColumn, VaccineSortableField.VACCINE_NAME);
        dictionary.put(administrationDateColumn, VaccineSortableField.ADMINISTRATION_DATE);
        dictionary.put(doseNumberColumn, VaccineSortableField.DOSE_NUMBER);
        dictionary.put(commentColumn, VaccineSortableField.COMMENT);

        vaccineGrid.getColumn(vaccineNameColumn).setCaption("Nom du vaccin");
        vaccineGrid.getColumn(administrationDateColumn).setCaption("Date d'administration");
        vaccineGrid.getColumn(doseNumberColumn).setCaption("Numéro de dose");
        vaccineGrid.getColumn(commentColumn).setCaption("Commentaire");

        vaccineGrid.setColumnReorderingAllowed(true);
        vaccineGrid.setSelectionMode(Grid.SelectionMode.NONE);

        // Add sorting listener
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

        ComboBox<String> vaccineName = new ComboBox<>("Nom vaccin");
        vaccineName.setWidthFull();
        vaccineName.setEmptySelectionAllowed(false);  // force user to select

        try {
            HttpClientHelper.HttpResponse<VaccineTypeResponseWrapper> response = httpClientHelper.get(HttpClientHelper.GetRequest.<VaccineTypeResponseWrapper>builder()
                    .endpoint(String.format("%s/types", VaccineController.URI))
                    .returnType(VaccineTypeResponseWrapper.class)
                    .build());
            vaccineName.setItems(response.getData().getContent());
        } catch (Exception e) {
            Notification.show("Failed to load vaccine names: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }


        DateField adminDate = new DateField("Date d'administration");
        adminDate.setRangeEnd(LocalDate.now());
        adminDate.setWidthFull();

        TextField doseNumber = new TextField("Numéro de la dose");
        doseNumber.setWidthFull();
        TextField comment = new TextField("Commentaire");
        comment.setWidthFull();

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
                                .vaccineName(vaccineName.getValue())
                                .build())
                        .returnType(Integer.class)
                        .build());


                Notification.show("Vaccin ajouté avec succès", Notification.Type.TRAY_NOTIFICATION);
                modal.close();
                loadVaccines();
            } catch (Exception ex) {
                Notification.show("Le vaccin n'a pas pu être enregistré: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        form.addComponents(vaccineName, adminDate, doseNumber, comment, submit);

        modal.setContent(form);
        UI.getCurrent().addWindow(modal);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // View entry point if needed
    }
}