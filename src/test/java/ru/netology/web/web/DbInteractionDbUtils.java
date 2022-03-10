package ru.netology.web.web;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.User;

import java.sql.DriverManager;

public class DbInteractionDbUtils {

    @Test
    @SneakyThrows
    void stubTest() {
        var countSQL = "SELECT COUNT(*) FROM users;";
        var usersSQL = "SELECT * FROM users;";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "shade1471", "shade1471"
                );
        ) {
            var first = runner.query(conn, usersSQL, new BeanHandler<>(User.class));
            var login = first.getLogin();
            var password = first.getPassword();

            var all = runner.query(conn, usersSQL, new BeanListHandler<>(User.class));
            all.get(0).getLogin();

            System.out.println(all);
        }
    }
}

