package ru.yandex.prakticum.CourierT;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.CourierGenerator;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private CourierClient courierClient;
    private int courierId;
    @Before
    public void setUp(){
        courierClient = new CourierClient();
        Courier courier = new Courier(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN,CourierGenerator.FIRST_NAME_GEN);
        courierClient.create(courier);

    }

    @Test
    @DisplayName("Курьер может авторизоваться.Ответ 200")
    @Description("Post запрос на ручку /api/v1/courier/login")
    @Step("Основной шаг - тест логин курьера")
    public void courierCanAuthorizations(){
        CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN);
        courierClient.login(courierLogin)
                .then().
                assertThat().statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("Курьер может авторизоваться. Запрос возвращает id")
    @Description("Post запрос на ручку /api/v1/courier/login")
    @Step("Основной шаг - тест логин курьера")
    public void courierCanAuthorizationsReturnId(){
        CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN);
        courierClient.login(courierLogin)
                .then().
                assertThat().body("id",notNullValue());
    }

    @Test
    @DisplayName("Курьер не сможет авторизоваться, без обязательного поля - нет логина. Error 400")
    @Description("Post запрос на ручку /api/v1/courier/login")
    @Step("Основной шаг - тест логин курьера")
    public void courierCanNotAuthorizationsWithoutLogin(){
        CourierLogin courierLogin = new CourierLogin(null,CourierGenerator.PASSWORD_GEN);
        courierClient.login(courierLogin)
                .then().
                assertThat().statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Курьер не сможет авторизоваться, без обязательного поля - нет пароля. Error 400")
    @Description("Post запрос на ручку /api/v1/courier/login")
    @Step("Основной шаг - тест логин курьера")
    public void courierCanNotAuthorizationsWithoutPassword(){
        CourierLogin courierLoginForDelete = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN);
            CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN,null);
            courierClient.login(courierLogin)
                    .then().
                    assertThat().statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Курьер не сможет авторизоваться, неправильный логин. Error 404")
    @Description("Post запрос на ручку /api/v1/courier/login")
    @Step("Основной шаг - тест логин курьера")
    public void courierCanNotAuthorizationsIncorrectLogin(){
        CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_SECOND_GEN,CourierGenerator.PASSWORD_GEN);
        courierClient.login(courierLogin)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().extract().path("message","Учетная запись не найдена");
    }

    @Test
    @DisplayName("Курьер не сможет авторизоваться, неправильный пароль. Error 404")
    @Description("Post запрос на ручку /api/v1/courier/login")
    @Step("Основной шаг - тест логин курьера")
    public void courierCanNotAuthorizationsIncorrectPassword(){
        CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_SECOND_GEN);
        courierClient.login(courierLogin)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().extract().path("message","Учетная запись не найдена");
    }


    @After
    @Step("Постусловие.Удаление курьера")
    public void clearData() {
        CourierLogin courierLoginForDelete = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN);
        Response response = courierClient.login(courierLoginForDelete);
        courierId = response.path("id");
        courierClient.delete(courierId);
        System.out.println("удален - " + courierId);
    }
}
