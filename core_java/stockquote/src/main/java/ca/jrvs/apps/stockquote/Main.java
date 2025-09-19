package ca.jrvs.apps.stockquote;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class Main {

	public static void main(String[] args) {		
		Map<String, String> properties = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/resources/properties.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(":");
				properties.put(tokens[0], tokens[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Class.forName(properties.get("db-class"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		OkHttpClient client = new OkHttpClient();
		String url = "jdbc:postgresql://"+properties.get("server")+":"+properties.get("port")+"/"+properties.get("database");
		try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
			QuoteDao qRepo = new QuoteDao(c);
			PositionDao pRepo = new PositionDao(c);
			QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"), client, c);
			QuoteService sQuote = new QuoteService(qRepo, rcon);
			PositionService sPos = new PositionService(pRepo, qRepo);
			StockQuoteController con = new StockQuoteController(sQuote, sPos);
			con.initClient();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
