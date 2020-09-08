package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                String date = alts.get(1).text();
                System.out.printf("%s%n%s%n%s%n%n", headline, href, date);

            }
        }
    }
}