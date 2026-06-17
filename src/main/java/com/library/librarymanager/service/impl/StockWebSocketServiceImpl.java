package com.library.librarymanager.service.impl;
import com.library.librarymanager.service.Interface.StockWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockWebSocketServiceImpl implements StockWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyStockChanged() {
        messagingTemplate.convertAndSend(
                "/topic/stock",
                "{\"type\":\"STOCK_CHANGED\"}"
        );
    }
}