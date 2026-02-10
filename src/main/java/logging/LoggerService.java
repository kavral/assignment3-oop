package logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logging service implemented as a Singleton.
 *
 * In a real project you would typically use SLF4J/Logback, but this class
 * exists to make the Singleton pattern explicit for documentation and UML.
 */
public final class LoggerService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static volatile LoggerService instance;

    private LoggerService() {
    }

    public static LoggerService getInstance() {
        if (instance == null) {
            synchronized (LoggerService.class) {
                if (instance == null) {
                    instance = new LoggerService();
                }
            }
        }
        return instance;
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void error(String message, Throwable t) {
        log("ERROR", message + " - " + t.getMessage());
    }

    private void log(String level, String message) {
        String ts = LocalDateTime.now().format(FORMATTER);
        System.out.printf("[%s] [%s] %s%n", ts, level, message);
    }
}

