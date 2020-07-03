import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBDataQuery {

    public static final String USERS_SQL = "select * from users where login = ?;";
    public static final String AUTH_CODE_SQL = "select * from auth_codes where user_id = ? order by created desc;";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "9mREavXDs9Gk89Ef";

    public static User getUser(String login) {
        QueryRunner runner = new QueryRunner();
        User user = null;
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            user = runner.query(conn, USERS_SQL, login, new BeanHandler<>(User.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String getAuthCode(String id) {
        AuthCode authCode = new AuthCode();
        if(id == null)
            return null;
        QueryRunner runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            authCode = runner.query(conn, AUTH_CODE_SQL, id, new BeanHandler<>(AuthCode.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authCode.getCode();
    }
}
