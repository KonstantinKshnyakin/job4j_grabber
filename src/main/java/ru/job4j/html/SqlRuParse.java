package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.model.Post;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SqlRuParse implements Parse {

    public static final String URL = "https://www.sql.ru/forum/job-offers/";

    public static void main(String[] args) {
        SqlRuParse parse = new SqlRuParse();
        List<Post> list = parse.list(URL);
        System.out.println(list);
    }

    public Post detail(Element ele) {
        Post post = null;
        Elements posts = ele.select(".postslisttopic");
        Elements alts = ele.select(".altCol");
        if (posts.size() != 0) {
            String title = posts.first().child(0).text();
            String link = posts.first().child(0).attr("href");
            LocalDateTime date = parseLocalDateTime(alts.last().text());
            String descText = parseDescriptionText(link);
            post = new Post(title, date, link, descText);
        }
        return post;
    }

    public List<Post> list(String link) {
        Document doc = loadPages(link);
        final ArrayList<Post> listPosts = new ArrayList<>();
        Elements select = doc.select("tr");
        for (Element ele : select) {
            Post post = detail(ele);
            if (post != null) {
                listPosts.add(post);
            }
        }
        return listPosts;
    }

    private String parseDescriptionText(String link) {
        Document descPage = loadPage(link);
        return descPage.select(".msgBody").get(1).text();
    }

    private Document loadPages(String link) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 1; i++) {
            Document doc = loadPage(link + i);
            String html = doc.html();
            builder.append(html).append(System.lineSeparator());
        }
        return Jsoup.parse(builder.toString());
    }

    private Document loadPage(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(doc);
    }

    private LocalDateTime parseLocalDateTime(String dateTime) {
        LocalDateTime ldt = null;
        String[] split = dateTime.split("[\\p{Punct}\\s]+");
        if (split.length == 5) {
            ldt = parseLocalDateTime5(split);
        } else if (split.length == 3) {
            ldt = parseLocalDateTime3(split);
        }
        return Objects.requireNonNull(ldt);
    }

    private LocalDateTime parseLocalDateTime5(String[] dateTime) {
        int day = Integer.parseInt(dateTime[0]);
        Month month = parseMonth(dateTime[1]);
        int year = Integer.parseInt("20" + dateTime[2]);
        int hour = Integer.parseInt(dateTime[3]);
        int min = Integer.parseInt(dateTime[4]);
        return LocalDateTime.of(year, month, day, hour, min);
    }

    private LocalDateTime parseLocalDateTime3(String[] dateTime) {
        LocalDateTime ldt = null;
        String date = dateTime[0];
        int hour = Integer.parseInt(dateTime[1]);
        int min = Integer.parseInt(dateTime[2]);
        if (date.equals("сегодня")) {
            ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, min));
        } else if (date.equals("вчера")) {
            ldt = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(hour, min));
        }
        return ldt;
    }

    private Month parseMonth(String month) {
        Month returnMonth = null;
        for (Month months : Month.values()) {
            String monthShortRu = months.getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("ru"));
            if (monthShortRu.contains(month)) {
                returnMonth = months;
            }
        }
        return Objects.requireNonNull(returnMonth);
    }
}