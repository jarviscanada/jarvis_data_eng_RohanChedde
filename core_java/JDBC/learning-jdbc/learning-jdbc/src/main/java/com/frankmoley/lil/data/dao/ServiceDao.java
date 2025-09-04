package com.frankmoley.lil.data.dao;
import com.frankmoley.lil.data.entity.Service;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ResultSet;

public class ServiceDao implements Dao<Service, UUID>{
    private static final Logger LOGGER = Logger.getLogger(ServiceDao.class.getName());

    private static final String GET_ALL = "select service_id, name, price from wisdom.services;";
    @Override
    public Service create(Service entity){
        return null;
    }

    @Override
    public void delete(UUID id){

    }

    @Override
    public List<Service> getAll(){
        List<Service> services = new ArrayList<>();
        Connection connection = DatabaseUtils.getConnection();
        try(Statement statement = connection.createStatement()){
            Result rs = statement.executeQuery(GET_ALL);
            services = this.processResultSet(rs);
        }catch(SQLException e){
            DatabaseUtils.handleSqlException("ServiceDao.getAll", e, LOGGER);
        }
        return services;
    }

    @Override
    public Optional<Service> getOne(UUID id){
        return Optional.empty();
    }

    @Override
    public Service update(Service entity){
        return null;
    }

    private List<Service> processResultSet(ResultSet rs) throws SQLException{
        List<Service> services = new ArrayList<>();
        while(rs.next()){
            Service service = new Service();
            service.setService((UUID)rs.getObject("service_id"));
            service.setName(rs.getString("name"));
            service.setPrice(rs.getBigDecimal("price"));
            services.add(service);
        }
        return services;
    }

}