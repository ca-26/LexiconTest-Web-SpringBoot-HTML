//package com.ca.lextest.db.config;
//
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.stream.Collectors;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PropertiesLoaderUtils;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
// 
///**
// * Spring configuration of the "mysql" database.
// * 
// * @author Radouane ROUFID.
// *
// */
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//		entityManagerFactoryRef = "mysqlEntityManagerFactory", 
//		transactionManagerRef = "mysqlTransactionManager",
//		basePackages = "${spring.mysql.datasource.repository.package}"
//)
//public class MySqlConfigWithoutEntityManagerAccess4JUnit {
//
//	@Autowired
//	private Environment env;
// 
//	/**
//	 * MySQL datasource definition.
//	 * 
//	 * @return datasource.
//	 */
//	@Bean
//	@Primary
//	@ConfigurationProperties(prefix = "spring.mysql.datasource")
//////tell spring to pick up the data source properties that are prefixed with   spring.mysql.datasource from the application.properties file and build a data source using DataSourceBuilder.
//	public DataSource mysqlDataSource() {
//		return DataSourceBuilder
//					.create()
//					.build();
//	}
// 
//	/**
//	 * Entity manager definition. 
//	 *  
//	 * @param builder an EntityManagerFactoryBuilder.
//	 * @return LocalContainerEntityManagerFactoryBean.
//	 */
//	@Bean(name = "mysqlEntityManager")
//	public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
//		return builder
//					.dataSource(mysqlDataSource())
//						//method to load some hibernate properties from the file hibernate.properties I put in classpath.
//					.properties(hibernateProperties())
//					.packages(env.getProperty("spring.mysql.datasource.entities.package"))
//						//packages where your entities are located
//					.persistenceUnit("mysqlPU")
//					.build();
//	}
// 
//	/**
//	 * @param entityManagerFactory
//	 * @return
//	 */
//	@Bean(name = "mysqlTransactionManager")
//	public PlatformTransactionManager mysqlTransactionManager(@Qualifier("mysqlEntityManager") EntityManagerFactory entityManagerFactory) {
//		return new JpaTransactionManager(entityManagerFactory);
//	}
//	
//	 
//	private Map<String, Object> hibernateProperties() {
// 
//		Resource resource = new ClassPathResource("hibernate.properties");
//		try {
//			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
//			return properties.entrySet().stream()
//											.collect(Collectors.toMap(
//														e -> e.getKey().toString(),
//														e -> e.getValue())
//													);
//		} catch (IOException e) {
//			return new HashMap<String, Object>();
//		}
//	}
//}
//
///* 
// //To foredisabling :
// // spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
// //https://stackoverflow.com/questions/40509395/cant-set-jpa-naming-strategy-after-configuring-multiple-data-sources-spring-1
//protected Map<String, Object> jpaProperties() {
//    Map<String, Object> props = new HashMap<>();
//    props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
//    props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
//    return props;
//}
//
//@Primary
//@Bean(name = "defaultEntityManager")
//public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
//    EntityManagerFactoryBuilder builder) {
//    return builder
//        .dataSource(auntDataSource())
//        .packages(Aunt.class)
//        .persistenceUnit("aunt")
//        .properties(jpaProperties())
//        .build();
//}
//*/
