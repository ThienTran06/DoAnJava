package com.library.librarymanager.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationEvent {
    private final String type;
    private final String title;
    private final String message;
    private final String href;
}
