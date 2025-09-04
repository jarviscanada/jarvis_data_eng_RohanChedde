package com.frankmoley.lil;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ServiceDao serviceDao = new ServiceDao();
        List<Service> services = serviceDao.getAll();
        System.out.println("Services");
        System.out.println("\nGet_ALL");
        services.forEach(System.out::prinln);
    }
}
