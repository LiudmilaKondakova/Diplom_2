package org.example.client;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static org.example.generator.MyConstant.BASE_URI;

public class BasePage {

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static void installSpec(RequestSpecification request){
        RestAssured.requestSpecification = request;
    }
}
