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
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import lu.pokevax.business.vaccine.VaccineController;
import lu.pokevax.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponse;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponseWrapper;
import lu.pokevax.business.vaccine.administered.responses.VaccineTypeResponseWrapper;
import lu.pokevax.ui.components.ComponentsFactory;
import lu.pokevax.ui.helpers.HttpClientHelper;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@SpringView(name = DashboardView.VIEW_PATH)
@Title("Dashboard | Pokevax")
@RequiredArgsConstructor
public class DashboardView extends VerticalLayout implements View {

    public static final String VIEW_PATH = "dashboard";

    private final HttpClientHelper httpClientHelper;

    private Grid<AdministeredVaccineResponse> vaccineGrid = new Grid<>(AdministeredVaccineResponse.class);

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

        HorizontalLayout header = new HorizontalLayout(title, addVaccineBtn);
        header.setWidthFull();
        ;
        header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(addVaccineBtn, Alignment.MIDDLE_RIGHT);
        header.setExpandRatio(title, 1);

        addComponents(header, vaccineGrid);
        setExpandRatio(vaccineGrid, 1);

        configureGrid();
        loadVaccines();
    }

    private void configureGrid() {
        vaccineGrid.setSizeFull();
        vaccineGrid.setColumns("vaccineName", "administrationDate", "doseNumber", "comment");

//        vaccineGrid.getColumn("administrationDate")
//                .setRenderer(new DateRenderer("yyyy-MM-dd"));

        vaccineGrid.setColumnReorderingAllowed(true);
//        vaccineGrid.setSortable(true);
        vaccineGrid.setSelectionMode(Grid.SelectionMode.NONE);
    }

    private void loadVaccines() {
        try {
            HttpClientHelper.HttpResponse<AdministeredVaccineResponseWrapper> response = httpClientHelper.post(HttpClientHelper.PostRequest.<SearchVaccineRequest, AdministeredVaccineResponseWrapper>builder()
                    .endpoint(VaccineController.URI + "/search")
                    .body(new SearchVaccineRequest())
                    .returnType(AdministeredVaccineResponseWrapper.class)
                    .build());

            // TODO: handle error.

            vaccineGrid.setItems(response.getData().getContent());
        } catch (Exception e) {
            Notification.show("Failed to load vaccines: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void openAddVaccineModal() {
        Window modal = new Window("Add Vaccine");
        modal.setModal(true);
        modal.setWidth("400px");

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

        TextField doseNumber = new TextField("Numéro de la dose");
        TextField comment = new TextField("Commentaire");

        Button submit = new Button("Sauver");
        submit.addStyleName(ValoTheme.BUTTON_PRIMARY);

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