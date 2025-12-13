package org.example.springbootwebmvc_user_andpets.errorDto;

import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {
}
