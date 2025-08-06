package lu.vaccineo.business.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.technical.ValidatedRestController;
import lu.vaccineo.technical.web.WebTokenExtractor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ValidatedRestController
@RequestMapping(VaccineNotificationController.URI)
@RequiredArgsConstructor
@Slf4j
public class VaccineNotificationController {
    public static final String URI = "/api/vaccines/notifications";

    private final VaccineNotificationService service;
    private final WebTokenExtractor webTokenExtractor;

    @GetMapping
    public VaccineNotificationResponseWrapper retrieveNotification(HttpServletRequest httpServletRequest) {
        List<VaccineNotificationResponse> content = service.retrieveNotificationsFor(webTokenExtractor.extractUserIdOrThrowException(httpServletRequest));
        return VaccineNotificationResponseWrapper.builder()
                .content(content)
                .build();
    }
}
