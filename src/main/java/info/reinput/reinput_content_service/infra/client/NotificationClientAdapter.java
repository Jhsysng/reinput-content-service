package info.reinput.reinput_content_service.infra.client;

import info.reinput.reinput_content_service.application.dto.ReminderDto;
import info.reinput.reinput_content_service.infra.client.feign.dto.ReminderCreateRes;

public interface NotificationClientAdapter {
    ReminderCreateRes saveReminder(final ReminderDto reminderDto);
}
