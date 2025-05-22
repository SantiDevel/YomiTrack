package com.santiparra.yomitrack.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    public static String getRelativeTime(String isoDate) {
        try {
            // Convertir la fecha ISO
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(isoDate);

            // Obtener ahora y diferencia
            Date now = new Date();
            long diffMillis = now.getTime() - date.getTime();
            long diffSeconds = diffMillis / 1000;
            long diffMinutes = diffSeconds / 60;
            long diffHours = diffMinutes / 60;
            long diffDays = diffHours / 24;

            if (diffSeconds < 60) {
                return "Hace unos segundos";
            } else if (diffMinutes < 60) {
                return "Hace " + diffMinutes + " min";
            } else if (diffHours < 24 && isSameDay(now, date)) {
                return "Hace " + diffHours + " horas";
            } else if (isYesterday(now, date)) {
                return "Ayer";
            } else if (diffDays < 7) {
                return "Hace " + diffDays + " días";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
                return dateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return isoDate;
        }
    }

    private static boolean isSameDay(Date now, Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(now);
        cal2.setTime(date);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isYesterday(Date now, Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(now);
        cal2.setTime(date);
        cal1.add(Calendar.DAY_OF_YEAR, -1);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
