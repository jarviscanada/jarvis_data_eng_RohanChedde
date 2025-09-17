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
public class PositionDao implements CrudDao<Position, String> {

    private Connection c;
    private static final Logger LOGGER = Logger.getLogger(PositionDao.class.getName());
    private static final String INSERT = "insert into Position (ticker, numOfShares, valuePaid) values (?, ?, ?);";
    private static final String SELECT = "select * from Position where ticker=?;";
    private static final String SELECT_ALL = "select * from Position;";
    private static final String DELETE = "delete from Position where ticker=?;";
    private static final String DELETE_ALL = "delete from Position;";


    @Override
    public Position save(Position entity) throws IllegalArgumentException {
    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(INSERT);
        statement.setString(1, entity.getTicker());
        statement.setInt(2, entity.getNumOfShares());
        statement.setDouble(3, entity.getValuePaid());

      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("PositionDao.create.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("PositionDao.create", e, LOGGER);
    }
    Optional<Position> position = this.findById(entity.getTicker());
    if (!position.isPresent()) {
      return null;
    }
    return position.get();
    }

  /**
   * Retrieves a Quote entity by its symbol
   * @param id - must not be null
   * @return Quote with the given symbol or empty optional if none found
   * @throws IllegalArgumentException - if id is null
   */
  @Override
  public Optional<Position> findById(String id) throws IllegalArgumentException {
    try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(SELECT)){
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        List<Position> positions = this.processResultSet(rs);
        if (positions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(positions.get(0));
    } catch (SQLException e) {
        DatabaseUtils.handleSqlException("PositionDao.findById", e, LOGGER);
    }
    return Optional.empty();
  }

    @Override
    public Iterable<Position> findAll() {
        List<Position> positions = new ArrayList<>();
        Connection connection = DatabaseUtils.getConnection();
        try (Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(SELECT_ALL);
            positions = this.processResultSet(rs);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("PositionDao.findAll", e, LOGGER);
        }
        return positions;
    }

    @Override
    public void deleteById(String id){
        Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(DELETE);
        statement.setString(1, id);
      statement.executeUpdate();
        connection.commit();
        statement.close();
    }catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("PositionDao.deleteById.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("PositionDao.deleteById", e, LOGGER);
    }
    }

  @Override
  public void deleteAll() {
    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(DELETE_ALL);
      statement.executeUpdate();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("PositionDao.deleteAll.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("PositionDao.deleteAll", e, LOGGER);
    }
  }

    private List<Position> processResultSet(ResultSet rs) throws SQLException {
    List<Position> positions = new ArrayList<>();
        while (rs.next()) {
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(0);
        position.setValuePaid(0);

        positions.add(position);
        }
        return positions;
    }

}