package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.util.Locale;

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
        return new AuthInfo(getLoginExistUser(number), getPasswordExistUser(getLoginExistUser(number)));
    }

    @SneakyThrows
    public static String getLoginExistUser(int number) {


        var usersSQL = "SELECT * FROM users;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "shade1471", "shade1471"
                );
        ) {
            var all = runner.query(conn, usersSQL, new BeanListHandler<>(User.class));
            return all.get(all.size() - number).getLogin();
        }
    }

    public static String getPasswordExistUser(String user) {
        if (user.equals("petya")) {
            return "123qwerty";
        } else {
            return "qwerty123";
        }
    }

    public static AuthInfo getAuthInfoInvalid() {
        return new AuthInfo(getLoginExistUser(1), "qwerty");
    }

    @SneakyThrows
    public static void addUser(AuthInfo info) {
        QueryRunner runner = new QueryRunner();
        var dataSQL = "INSERT INTO users(id, login, password) VALUES(?, ?, ?);";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "shade1471", "shade1471"
                );
        ) {
            runner.update(conn, dataSQL,"407d235e-3456-432c-4fg1-85077e1df297", info.getLogin(), info.getPassword());
        }
    }

    public static AuthInfo getNewAuthInfo(){
        return new AuthInfo(DataHelper.generateLogin("en"), "$2a$10$ku1fTclpxp0umxAiqrztLuii64OOaNvausRy/V4OW2a85LQ0Ww2uu");
    }

    public static String generateLogin(String locale) {
        String login = new Faker(new Locale(locale)).name().username();
        return login;
    }



//    public static AuthInfo getInvalidAuthInfo(AuthInfo original) {
//        return new AuthInfo(DataHelper.generateLogin("en"), DataHelper.generatePassword("en"));
//    }

    @Value
    public static class VerificationCode {
        private String code;
    }
    public static  VerificationCode getWrongCode(){
        return new VerificationCode("11111");
    }

    @SneakyThrows
    public static VerificationCode getVerificationCodeFor(String login) {

        var codeSQL = "SELECT code" +
                " FROM auth_codes ac JOIN users u ON ac.user_id = u.id" +
                " WHERE login='" + login + "'" +
                " ORDER BY created DESC" +
                " LIMIT 1;";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "shade1471", "shade1471"
                );
        ) {
            var code = runner.query(conn, codeSQL, new ScalarHandler<>()).toString();
            return new VerificationCode(code);
        }
    }
}
