package com.rohan.lil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class API {

  public static void main(String[] args) {
    String symbol = "GOOG";
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(
            "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json"))
        .header("X-RapidAPI-Key", "ebe9112052msh012e03c973d45f5p18bce4jsn5dd9f3d22fe4")
        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
    try {
      HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
