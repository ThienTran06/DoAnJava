package com.library.librarymanager.event;

import com.library.librarymanager.service.Interface.StockWebSocketService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class StockEventListener {

    private final StockWebSocketService stockWebSocketService;

    public StockEventListener(StockWebSocketService stockWebSocketService) {
        this.stockWebSocketService = stockWebSocketService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(StockChangedEvent event) {
        stockWebSocketService.notifyStockChanged();
    }
}