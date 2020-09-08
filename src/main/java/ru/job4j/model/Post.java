package ru.job4j.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private String headLine;
    private LocalDateTime dateTime;
    private String link;

    public Post() {
    }

    public Post(String headLine, LocalDateTime dateTime, String link) {
        this.headLine = headLine;
        this.dateTime = dateTime;
        this.link = link;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }

        Post post = (Post) o;

        if (!Objects.equals(headLine, post.headLine)) {
            return false;
        }
        if (!Objects.equals(dateTime, post.dateTime)) {
            return false;
        }
        return Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + (headLine != null ? headLine.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Post{"
                + "headLine='" + headLine + '\''
                + ", dateTime=" + dateTime
                + ", link='" + link + '\''
                + '}';
    }
}
