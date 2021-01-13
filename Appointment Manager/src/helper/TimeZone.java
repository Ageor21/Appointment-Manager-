package helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeZone {

    public final static ZoneId MAIN_OFFICE_ZONE = ZoneId.of("America/New_York");

    public static Date toMainOfficeZone(Date date) {
        if (date == null) {
            return null;
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        ZonedDateTime zdt = ldt.atZone(MAIN_OFFICE_ZONE);
        Date output = Date.from(zdt.toInstant());
        return output;
    }

    public static Date toCurrentZone(Date date) {
        if (date == null) {
            return null;
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), MAIN_OFFICE_ZONE);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date output = Date.from(zdt.toInstant());
        return output;
    }
}
