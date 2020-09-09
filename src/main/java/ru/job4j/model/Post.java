package ru.job4j.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private String title;
    private LocalDateTime createDate;
    private String link;
    private String text;

    public Post() {
    }

    public Post(String headLine, LocalDateTime dateTime, String link, String text) {
        this.title = headLine;
        this.createDate = dateTime;
        this.link = link;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

        if (!Objects.equals(title, post.title)) {
            return false;
        }
        if (!Objects.equals(createDate, post.createDate)) {
            return false;
        }
        if (!Objects.equals(text, post.text)) {
            return false;
        }
        return Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + (title != null ? title.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Post{"
                + System.lineSeparator()
                + "headLine='" + title + '\''
                + System.lineSeparator()
                + "dateTime=" + createDate
                + System.lineSeparator()
                + "link='" + link + '\''
                + System.lineSeparator()
                + "text='" + text + '\''
                + '}'
                + System.lineSeparator();
    }
}
