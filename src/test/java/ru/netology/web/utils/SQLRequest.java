package ru.netology.web.utils;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;

public class SQLRequest {
    private static QueryRunner runner = new QueryRunner();

    @SneakyThrows
    public static Connection connection() {
        var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "shade1471", "shade1471");
        return conn;
    }

    @SneakyThrows
    public static String getLoginExistUser(int number) {
        var usersSQL = "SELECT * FROM users;";

        var all = runner.query(connection(), usersSQL, new BeanListHandler<>(User.class));
        return all.get(number - 1).getLogin();
    }

    @SneakyThrows
    public static String getEncryptedPasswordExistUser(int number) {
        var usersSQL = "SELECT * FROM users;";

        var all = runner.query(connection(), usersSQL, new BeanListHandler<>(User.class));
        return all.get(number - 1).getPassword();
    }

    @SneakyThrows
    public static void addNewUser(DataHelper.AuthInfo info) {
        var dataSQL = "INSERT INTO users(id, login, password) VALUES(?, ?, ?);";

        String id = UUID.randomUUID().toString();
        runner.update(connection(), dataSQL, id, info.getLogin(), getKnownEncryptedPassword());
    }

    //Получить зашифрованный пароль для qwerty123
    @SneakyThrows
    public static String getKnownEncryptedPassword() {
        var passwordSQL = "SELECT password" +
                " FROM users" +
                " WHERE login='vasya'";

        var password = runner.query(connection(), passwordSQL, new ScalarHandler<>()).toString();
        return password;
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCodeFor(String login) {

        var codeSQL = "SELECT code" +
                " FROM auth_codes ac JOIN users u ON ac.user_id = u.id" +
                " WHERE login='" + login + "'" +
                " ORDER BY created DESC" +
                " LIMIT 1;";

        var code = runner.query(connection(), codeSQL, new ScalarHandler<>()).toString();
        return new DataHelper.VerificationCode(code);
    }

    @SneakyThrows
    public static void clearAll() {
        var cardsClearSQL = "DELETE FROM cards";
        var auth_codesClearSQL = "DELETE FROM auth_codes";
        var usersClearSQL = "DELETE FROM users";

        runner.update(connection(), cardsClearSQL);
        runner.update(connection(), auth_codesClearSQL);
        runner.update(connection(), usersClearSQL);
    }
}
