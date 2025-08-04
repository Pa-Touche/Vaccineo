package lu.pokevax.business.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VaccineNotificationService {

    private final VaccineNotificationRepository repository;
    private final VaccineNotificationMapper mapper;

    public List<VaccineNotificationResponse> retrieveNotificationsFor(Integer userId) {
        return repository.findAllByUserId(userId, Sort.by(Sort.Direction.ASC, "deadline")).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
