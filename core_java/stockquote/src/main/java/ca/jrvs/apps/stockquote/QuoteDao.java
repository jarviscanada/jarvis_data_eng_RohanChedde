package ca.jrvs.apps.stockquote;

import java.security.Provider.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.sql.ResultSet;

import ca.jrvs.apps.stockquote.util.DatabaseUtils;
public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;
    private static final Logger LOGGER = Logger.getLogger(QuoteDao.class.getName());
    private static final String UPSERT = "insert into Quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (symbol) DO UPDATE SET open=excluded.open, high=excluded.high, low=excluded.low, price=excluded.price, volume=excluded.volume, latest_trading_day=excluded.latest_trading_day, previous_close=excluded.previous_close, change=excluded.change, change_percent=excluded.change_percent, timestamp=excluded.timestamp;";
    private static final String SELECT = "select * from Quote where symbol=?;";
    private static final String SELECT_ALL = "select * from Quote;";
    private static final String DELETE = "delete from Quote where symbol=?;";
    private static final String DELETE_ALL = "delete from Quote;";


    public QuoteDao(Connection c) {
      this.c=c;
    }

    @Override
  public Quote save(Quote entity) throws IllegalArgumentException {
    try {
      c.setAutoCommit(false);
      PreparedStatement statement = c.prepareStatement(UPSERT);
      statement.setString(1, entity.getSymbol());
      statement.setDouble(2, entity.getOpen());
      statement.setDouble(3, entity.getHigh());
      statement.setDouble(4, entity.getLow());
      statement.setDouble(5, entity.getPrice());
      statement.setInt(6, entity.getVolume());
      statement.setDate(7, entity.getLatestTradingDay());
      statement.setDouble(8, entity.getPreviousClose());
      statement.setDouble(9, entity.getChange());
      statement.setString(10, entity.getChangePercent());
      statement.setTimestamp(11, entity.getTimestamp());
      statement.execute();
      c.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        c.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("QuoteDao.save.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("QuoteDao.save", e, LOGGER);
    }
    Optional<Quote> quote = this.findById(entity.getSymbol());
    if (!quote.isPresent()) {
      return null;
    }
    return quote.get();
  }

  /**
   * Retrieves a Quote entity by its symbol
   * @param id - must not be null
   * @return Quote with the given symbol or empty optional if none found
   * @throws IllegalArgumentException - if id is null
   */
  @Override
  public Optional<Quote> findById(String id) throws IllegalArgumentException {
    try (PreparedStatement statement = c.prepareStatement(SELECT)){
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        List<Quote> quotes = this.processResultSet(rs);
        if (quotes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(quotes.get(0));
    } catch (SQLException e) {
        DatabaseUtils.handleSqlException("QuoteDao.findById", e, LOGGER);
    }
    return Optional.empty();
  }

    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        try (Statement statement = c.createStatement()){
            ResultSet rs = statement.executeQuery(SELECT_ALL);
            quotes = this.processResultSet(rs);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("QuoteDao.findAll", e, LOGGER);
        }
        return quotes;
    }

    @Override
    public void deleteById(String id){
    try {
      c.setAutoCommit(false);
      PreparedStatement statement = c.prepareStatement(DELETE);
        statement.setString(1, id);
      statement.executeUpdate();
        c.commit();
        statement.close();
    }catch (SQLException e) {
      try {
        c.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("QuoteDao.deleteById.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("QuoteDao.deleteById", e, LOGGER);
    }
    }

  @Override
  public void deleteAll() {
    try {
      c.setAutoCommit(false);
      PreparedStatement statement = c.prepareStatement(DELETE_ALL);
      statement.executeUpdate();
      c.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        c.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("QuoteDao.deleteAll.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("QuoteDao.deleteAll", e, LOGGER);
    }
  }

    private List<Quote> processResultSet(ResultSet rs) throws SQLException {
    List<Quote> quotes = new ArrayList<>();
        while (rs.next()) {
        Quote quote = new Quote();
        quote.setSymbol(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));
        quotes.add(quote);
        }
        return quotes;
    }

}