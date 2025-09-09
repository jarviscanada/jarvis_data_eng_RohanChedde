package com.rohan.lil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.rohan.lil.data.dao.ServiceDao;
import com.rohan.lil.data.entity.Service;

public class App {
    public static void main(String[] args) {
        ServiceDao serviceDao = new ServiceDao();
        List<Service> services = serviceDao.getAll();
        System.out.println("**** Services ****");
        System.out.println("\n*** Get_ALL ***");
        services.forEach(System.out::println);
        Optional<Service> service = serviceDao.getOne(services.get(0).getServiceId());
        System.out.println("\n*** Get_ONE ***" + service.get());
        Service newService = new Service();
        newService.setName("FooBarBaz" + System.currentTimeMillis());
        newService.setPrice(new BigDecimal(4.35));
        newService = serviceDao.create(newService);
        System.out.println("\n*** Create ***" + newService);
        newService.setPrice(new BigDecimal(13.45));
        newService = serviceDao.update(newService);
        System.out.println("\n*** UPDATE ***\n" + newService);
        serviceDao.delete(newService.getServiceId());
        System.out.println("\n*** DELETE ***\n");
    }
}
