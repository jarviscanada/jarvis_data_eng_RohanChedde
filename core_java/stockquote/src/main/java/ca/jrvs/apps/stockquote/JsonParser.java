package ca.jrvs.apps.stockquote;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonParser {

    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
        throws JsonProcessingException {
      ObjectMapper m = new ObjectMapper();
      if(!includeNullValues){
        m.setSerializationInclusion(Include.NON_NULL);
      }
      if(prettyJson){
        m.enable(SerializationFeature.INDENT_OUTPUT);
      }
      return m.writeValueAsString(object);
    }

    public static <T> T toObjectFromJson(String json, Class<T> clazz)
        throws IOException {
        ObjectMapper m = new ObjectMapper();
        return (T) m.readValue(json, clazz);
  
    }

    public static void main(String[] args) throws IOException {
      String json = "{ \"01. symbol\": \"AAPL\", \"05. price\": 127.79 }";
      StockQuote quote = toObjectFromJson(json, StockQuote.class);
      System.out.println(toJson(quote, true, false));
    }

}
