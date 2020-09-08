package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements select = doc.select("tr");
        for (Element ele : select) {
            Elements posts = ele.select(".postslisttopic");
            Elements alts = ele.select(".altCol");
            if (posts.size() != 0) {
                String headline = posts.get(0).child(0).text();
                String href = posts.get(0).child(0).attr("href");
                String date = parseLocalDateTime(alts.get(1).text()).toString();
                System.out.printf("%s%n%s%n%s%n%n", headline, href, date);

            }
        }
    }

    private static LocalDateTime parseLocalDateTime(String date) {
        LocalDateTime ldt = null;
        final String[] split = date.split("[\\p{Punct}\\s]+");
        if (split.length == 5) {
            ldt = parseLocalDateTime(split);
        } else if (split.length == 3) {
            String spt0 = split[0];
            int spt1 = Integer.parseInt(split[1]);
            int spt2 = Integer.parseInt(split[2]);
            if (spt0.equals("сегодня")) {
                ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(spt1, spt2));
            } else if (spt0.equals("вчера")) {
                ldt = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(spt1, spt2));
            }
        }
        return Objects.requireNonNull(ldt);
    }

    private static LocalDateTime parseLocalDateTime(String[] date) {
        int day = Integer.parseInt(date[0]);
        Month month = parseMonth(date[1]);
        int year = Integer.parseInt("20" + date[2]);
        int hour = Integer.parseInt(date[3]);
        int min = Integer.parseInt(date[4]);
        return LocalDateTime.of(year, month, day, hour, min);
    }

    private static Month parseMonth(String month) {
        Month returnMonth = null;
        for (Month months : Month.values()) {
            final String monthShortRu = months.getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("ru"));
            if (monthShortRu.contains(month)) {
                returnMonth = months;
            }
        }
        return Objects.requireNonNull(returnMonth);
    }
}