package libraryMS.utils.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/*
 * Service for retrieving localized messages.
 * <p>
 * This service utilizes Spring's {@link MessageSource} to fetch localized messages based on the current locale.
 */
@Service
public class MessageSourceService {

    private final MessageSource messageSource;

    public MessageSourceService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /*
     * Retrieves a localized message for the given message code.
     *
     * @param message the code of the message to retrieve
     * @return the localized message corresponding to the given message code
     */
    public String getMessage(String message) {
        return this.messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }
}