package lu.pokevax.ui.helpers;

import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.ui.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalErrorHandler implements ErrorHandler {

    @Override
    public void error(ErrorEvent event) {
        Throwable throwable = event.getThrowable();

        log.error("Unhandled exception in Vaadin UI", throwable);

        Throwable cause = findRelevantCause(throwable);

        Notification.show("Une erreur est survenue : " + cause.getMessage(), Notification.Type.ERROR_MESSAGE);
    }

    private Throwable findRelevantCause(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }
}