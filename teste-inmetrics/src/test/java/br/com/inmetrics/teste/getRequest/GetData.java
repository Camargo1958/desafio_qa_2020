package br.com.inmetrics.teste.getRequest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetData {

    Response resp;
    String baseUri = "http://inm-api-test.herokuapp.com/";
    RequestSpecification httpRequest;

    public Response getResponse1(String param){
        resp = RestAssured.get(baseUri+param);
        return resp;
    }

    public String getResponse2(String param){
        resp = RestAssured.get(baseUri+param);
        String data = resp.asString();
        return data;
    }

    public String getResponse3(String param){

        RestAssured.baseURI = this.baseUri;

        RequestSpecification httpRequest = RestAssured.given();

        // Usando chave de autorização de acesso
        resp = httpRequest.given().header("Content-Type", "application/json").given()
                .header("Authorization", "Basic aW5tZXRyaWNzOmF1dG9tYWNhbw==").
                        request(Method.GET,param);

        String responseBody = resp.getBody().asString();
        return responseBody;

    }

    public int getResponseCode(){

        int resp_code = resp.getStatusCode();

        return resp_code;
    }

    public long getRespDelay(){
        long delay = resp.getTime();
        System.out.println("Response time "+delay);
        return delay;
    }

    public Response sendEmpData(String newData, String param ){

        RestAssured.baseURI = "http://inm-api-test.herokuapp.com/";

        this.httpRequest = RestAssured.given();

        // Registra dados a serem enviados no log para debug
        System.out.println("Dados a enviar : "+newData);

        try {
            resp = httpRequest
                    .given().header("Content-Type", "application/json")
                    .given().header("Authorization", "Basic aW5tZXRyaWNzOmF1dG9tYWNhbw==")
                    .given().body(newData)
                    .request(Method.POST,param);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseBody = resp.getBody().asString();
        int resp_code = resp.getStatusCode();
        // Registra dados recebidos e status no log para debug
        System.out.println("retCode ="+resp_code+"\n");
        System.out.println("Response : "+responseBody+"\n");

        return resp;

    }

}