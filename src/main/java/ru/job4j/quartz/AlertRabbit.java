package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    public static void main(String[] args) {
        Properties properties = getProperties();
        int interval = Integer.parseInt(properties.getProperty("rabbit.interval"));
        try (Connection connection = getConnection(properties)) {

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException | SQLException se) {
            se.printStackTrace();
        }
    }

    private static Connection getConnection(Properties ps) {
        Connection cn = null;
        try {
            Class.forName(ps.getProperty("jdbc.driver"));
            cn = DriverManager.getConnection(
                    ps.getProperty("jdbc.url"),
                    ps.getProperty("jdbc.username"),
                    ps.getProperty("jdbc.password"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cn;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static class Rabbit implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            writeDateToBD(connection);
        }

        private void writeDateToBD(Connection connection) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            try (PreparedStatement ps = connection.prepareStatement("insert into rabbit (created_date) values (?)")) {
                ps.setTimestamp(1, timestamp);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
