package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.model.Post;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public class SqlRuParse {

    public static final String URL = "https://www.sql.ru/forum/job-offers/";

    public static void main(String[] args) {
        Document doc = connectAndReadPages(URL, 5);
        parsePost(doc);
    }

    private static void parsePost(Document doc) {
        Elements select = doc.select("tr");
        for (Element ele : select) {
            Elements posts = ele.select(".postslisttopic");
            Elements alts = ele.select(".altCol");
            if (posts.size() != 0) {
                String headline = posts.first().child(0).text();
                String link = posts.first().child(0).attr("href");
                LocalDateTime date = parseLocalDateTime(alts.last().text());
                Document descPage = connectAndReadPage(link);
                String descText = descPage.select(".msgBody").last().text();
                Post post = new Post(headline, date, link, descText);
                System.out.println(post);
            }
        }
    }

    private static Document connectAndReadPages(String url, int countPage) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= countPage; i++) {
            Document doc = connectAndReadPage(url + i);
            String html = doc.html();
            builder.append(html).append(System.lineSeparator());
        }
        return Jsoup.parse(builder.toString());
    }

    private static Document connectAndReadPage(String url) {
        Document doc = null;
        try {
                doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(doc);
    }

    private static LocalDateTime parseLocalDateTime(String date) {
        LocalDateTime ldt = null;
        final String[] split = date.split("[\\p{Punct}\\s]+");
        if (split.length == 5) {
            ldt = parseLocalDateTime(split);
        } else if (split.length == 3) {
            String spt0 = split[0];
            int hour = Integer.parseInt(split[1]);
            int min = Integer.parseInt(split[2]);
            if (spt0.equals("сегодня")) {
                ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, min));
            } else if (spt0.equals("вчера")) {
                ldt = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(hour, min));
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