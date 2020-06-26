import com.codeborne.selenide.SelenideElement;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestLogin {

    public static final String USER_NAME = "vasya";
    private static  final String USERS_SQL = "select * from users;";
    private static  final String AUTH_CODE_SQL = "select * from auth_codes where user_id = ? order by created desc;";

    @Test
    void ifCodeIsCorrectShouldReturnSuccess() throws SQLException {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=login] input").setValue("vasya");
        form.$("[data-test-id=password] input").setValue("qwerty123");
        form.$("[data-test-id=action-login]").click();
        $(withText("Необходимо подтверждение")).waitUntil(visible, 5000);
        $("[data-test-id=code] input").setValue(getCodeFromDB());
        $("[data-test-id=action-verify]").click();
        $(withText("Личный кабинет")).waitUntil(visible, 5000);
    }

    private String getCodeFromDB() throws SQLException {
        QueryRunner runner = new QueryRunner();
        AuthCode authCode;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "9mREavXDs9Gk89Ef")) {
            User user = runner.query(conn, USERS_SQL, new BeanHandler<>(User.class));
            if (null == user)
                return  null;
            authCode = runner.query(conn, AUTH_CODE_SQL, user.getId(), new BeanHandler<>(AuthCode.class));
        }
        return null != authCode ? authCode.getCode() : null;
    }
}
