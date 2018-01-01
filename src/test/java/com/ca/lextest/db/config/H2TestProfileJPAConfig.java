//package com.ca.lextest.db.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
///**
// * @author CIHAN
// * JUNIT Tests :
// * Testing DB access (H2 in memory DBS, mode MySQL) without config file.
// */
//
//@Configuration
//@EnableJpaRepositories(basePackages = {"com.ca.lextest.db.repository"})
//@EnableTransactionManagement
//public class H2TestProfileJPAConfig {
// 
//    @Bean
//    // DataSource Available only for test 
//    @Profile("test")
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:lexicontest;MODE=MySQLDATABASE_TO_UPPER=FALSE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
//        dataSource.setUsername("test");
//        dataSource.setPassword("");
// 
//        return dataSource;
//    }
//     
//    // configure entityManagerFactory
//    // configure transactionManager
//    // configure additional Hibernate properties
//}