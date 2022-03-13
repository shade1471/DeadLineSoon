package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;
import java.util.UUID;

public class DataHelper {
    private static QueryRunner runner = new QueryRunner();

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfoExist(int number) {
        return new AuthInfo(getLoginExistUser(number), getPasswordUser(getLoginExistUser(number)));
    }

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
        return all.get(all.size() - number).getLogin();
    }

    public static String getPasswordUser(String user) {
        if (user.equals("petya")) {
            return "123qwerty";
        } else {
            return "qwerty123";
        }
    }

    public static AuthInfo getAuthInfoInvalid() {
        return new AuthInfo("vasya", "qwerty");
    }

    @SneakyThrows
    public static void addUser(AuthInfo info) {
        var dataSQL = "INSERT INTO users(id, login, password) VALUES(?, ?, ?);";

        String id = UUID.randomUUID().toString();
        runner.update(connection(), dataSQL, id, info.getLogin(), getExistPassword());
    }

    public static AuthInfo getNewAuthInfo() {
        return new AuthInfo(DataHelper.generateLogin("en"), "qwerty123");
    }

    public static String generateLogin(String locale) {
        String login = new Faker(new Locale(locale)).name().username();
        return login;
    }

    //Получить зашифрованный пароль для qwerty123
    @SneakyThrows
    public static String getExistPassword() {
        var passwordSQL = "SELECT password" +
                " FROM users" +
                " WHERE login='vasya'";

        var password = runner.query(connection(), passwordSQL, new ScalarHandler<>()).toString();
        return password;
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getWrongCode() {
        return new VerificationCode("11111");
    }

    @SneakyThrows
    public static VerificationCode getVerificationCodeFor(String login) {

        var codeSQL = "SELECT code" +
                " FROM auth_codes ac JOIN users u ON ac.user_id = u.id" +
                " WHERE login='" + login + "'" +
                " ORDER BY created DESC" +
                " LIMIT 1;";

        var code = runner.query(connection(), codeSQL, new ScalarHandler<>()).toString();
        return new VerificationCode(code);
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
