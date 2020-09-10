package ru.job4j.html;

import ru.job4j.grabber.Store;
import ru.job4j.model.Post;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    public static void main(String[] args) {
        Properties cfg = getProperties();
        PsqlStore store = new PsqlStore(cfg);
        SqlRuParse parse = new SqlRuParse();
        List<Post> list = parse.list(SqlRuParse.URL);
        list.forEach(store::save);
        String sep = System.lineSeparator();
//        System.out.println("Post with id 1 : " + sep + store.findById("1"));
//        System.out.println("Post with id 5 : " + sep + store.findById("5"));
        System.out.println("All Posts : " + sep + store.getAll());
        System.out.println();
    }

    private static Properties getProperties() {
        URL resource = PsqlStore.class.getClassLoader().getResource("rabbit.properties");
        Objects.requireNonNull(resource);
        Properties cfg = new Properties();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(resource.openStream())) {
            cfg.load(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfg;
    }

    private final Connection cn;
    private static final String SAVE = "insert into grabber (title, text, link,create_date) values (?, ?, ?, ?)";
    private static final String SELECT_ALL = "select * from grabber";
    private static final String FIND_BY_ID = "select * from grabber where id = ?";

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = cn.prepareStatement(SAVE)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreateDate()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        ArrayList<Post> list = new ArrayList<>();
        try (Statement st = cn.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT_ALL);
            while (rs.next()) {
                list.add(new Post(
                                rs.getString("title"),
                                rs.getTimestamp("create_date").toLocalDateTime(),
                                rs.getString("link"),
                                rs.getString("text")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(String id) {
        Post post = null;
        try (PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                post = new Post(
                        rs.getString("title"),
                        rs.getTimestamp("create_date").toLocalDateTime(),
                        rs.getString("link"),
                        rs.getString("text")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(post);
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }
}
