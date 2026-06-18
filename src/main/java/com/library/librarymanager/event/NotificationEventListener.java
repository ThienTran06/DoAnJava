package com.library.librarymanager.event;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationEvent event) {
        messagingTemplate.convertAndSend((String) "/topic/notifications", (Object) Map.of(
                "type", event.getType(),
                "title", event.getTitle(),
                "message", event.getMessage(),
                "href", event.getHref()
        ));
    }
}
