package ru.yandex.praktikum.client;

import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.model.*;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient{
    private static final String COURIER_URI = BASE_URI + "courier/";

   public ValidatableResponse create(Courier courier) {
       return given()
               .spec(getBaseReqSpec())
               .body(courier)
               .when()
               .post(COURIER_URI)
               .then();
   }

    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI + id)
                .then();
    }


    public Response login(CourierLogin courierLogin) {
        return given()
                .config(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig() // немного подкрутил запрос для удобства
                .setParam("http.socket.timeout",7000)))
                .spec(getBaseReqSpec())
                .body(courierLogin)
                .post(COURIER_URI + "login/");
    }

    public ValidatableResponse getListOfOrder (){
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(BASE_URI+ "orders/")
                .then();
    }

    public ValidatableResponse createOrder (ListForCreateOrder listForCreateOrder){
        return given()
                .spec(getBaseReqSpec())
                .body(listForCreateOrder)
                .when()
                .post(BASE_URI+ "orders/")
                .then();
    }





}
