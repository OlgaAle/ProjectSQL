import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestLogin {

    public static final String LOGIN = "vasya";

    private User user;

    @BeforeEach
    void setUpAll() {
        user = DBDataQuery.getUser(LOGIN);
    }

    @Test
    void ifCodeIsCorrectShouldReturnSuccess() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=login] input").setValue(LOGIN);
        form.$("[data-test-id=password] input").setValue("qwerty123");
        form.$("[data-test-id=action-login]").click();
        $(withText("Необходимо подтверждение")).waitUntil(visible, 5000);
        $("[data-test-id=code] input").setValue(DBDataQuery.getAuthCode(user.getId()));
        $("[data-test-id=action-verify]").click();
        $(withText("Личный кабинет")).waitUntil(visible, 5000);
    }
}
