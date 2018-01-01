//package com.ca.lextest.db.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
///**
// * @author CIHAN
// * JUNIT Tests :
// * Testing DB access (H2 in memory DB, mode MySQL) with config file "persistence-test.properties".
// */
//
//@Configuration
//@EnableJpaRepositories(basePackages = "com.ca.lextest.db.repository")
//@PropertySource("classpath:/persistence-test.properties")
//@EnableTransactionManagement
//public class H2TestProfileJPAConfigInFile {
// 
//    @Autowired
//    private Environment env;
//     
//    @Bean
//    // DataSource Available only for test 
//    @Profile("test")	
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
//        dataSource.setUrl(env.getProperty("jdbc.url"));
//        dataSource.setUsername(env.getProperty("jdbc.username"));
//        dataSource.setPassword(env.getProperty("jdbc.password"));
// 
//        return dataSource;
//    }
//     
//    // configure entityManagerFactory
//     
//    // configure transactionManager
// 
//    // configure additional Hibernate Properties
//}