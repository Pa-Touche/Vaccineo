package lu.pokevax.ui.components;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import lu.pokevax.ui.views.DashboardView;
import lu.pokevax.ui.views.ProfileView;

public class ComponentsFactory {

    public static MenuBar menuBar(MenuEntry currentDisplayed) {
        MenuBar menuBar = new MenuBar();
        menuBar.setWidthFull();
        MenuBar.Command menuCommand = menu ->
                UI.getCurrent().getNavigator().navigateTo(DashboardView.VIEW_PATH);
        MenuBar.MenuItem dashboardMenuItem = menuBar.addItem("💉 Carte de vaccination", currentDisplayed == MenuEntry.VACCINE_DASHBOARD ? null : menuCommand);

        dashboardMenuItem.setStyleName("menu-selected");

        MenuBar.Command profileCommand = menu ->
                UI.getCurrent().getNavigator().navigateTo(ProfileView.UI_PATH);
        menuBar.addItem("🧑 Mon profil", currentDisplayed == MenuEntry.PROFILE ? null : profileCommand);


        return menuBar;
    }

    public enum MenuEntry {
        VACCINE_DASHBOARD,
        PROFILE
    }
}
