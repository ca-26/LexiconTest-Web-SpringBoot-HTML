package com.ca.lextest.db.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ca.lextest.LexiconTestApplication;
 
/**
 * Spring configuration of the custom "mysql" database.
 * 
 * @author Cihan AKKOYUN
 *
 *
 * Important : 
 * 		1) Use the following code to access the Entity Manager from other classes.
 *			@PersistenceContext(unitName="mysqlPU")
 *			private EntityManager mysqlEntityManager;
 *		2) Use annotation @Transactionnal for methodes or classes accessing DB
 *		   because "mysqlPU" custom Persistance Unit defined in this file is transactionnal
 *		   and annotation @PersistenceContext is transactionnal by default.
 *		   Otherwise you will encounter thefollowing error : *
 *				Error : "No EntityManager with actual transaction available for current thread
 *						 - cannot reliably process 'persist' call"
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "mysqlEntityManagerFactory", 
		transactionManagerRef = "mysqlTransactionManager",
		basePackages = "${spring.mysql.datasource.repository.package}"
)
public class MySqlConfig {

	private static final Logger LOG = LoggerFactory.getLogger(LexiconTestApplication.class);
	
	@Autowired
	private Environment env;
 
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.mysql.datasource")
	//tell spring to pick up the data source properties that are prefixed with   spring.mysql.datasource from the application.properties file and build a data source using DataSourceBuilder.
	public DataSource mysqlDataSource() {
		LOG.info("==================[mysqlDataSource building]==================");
		return DataSourceBuilder
					.create()
					.build();
	}
 
	@Primary
	@Bean(name = "mysqlEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		LOG.info("==================[mysqlEntityManagerFactory building]==================");
		return builder
					.dataSource(mysqlDataSource())
						//method to load some hibernate properties from the file hibernate.properties I put in classpath.
					.properties(hibernateProperties())
						//packages where your entities are located
					.packages(env.getProperty("spring.mysql.datasource.entities.package"))
					.persistenceUnit(env.getProperty("spring.mysql.datasource.persistance.unit.name"))
					.build();
	}
 
	@Bean(name = "mysqlTransactionManager")
	public PlatformTransactionManager mysqlTransactionManager(@Qualifier("mysqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		LOG.info("==================[mysqlTransactionManager building]==================");
		return new JpaTransactionManager(entityManagerFactory);
	}
	 
	private Map<String, Object> hibernateProperties() {
		LOG.info("==================[hibernateProperties loading (file: hibernate.properties)]==================");
		Resource resource = new ClassPathResource("hibernate.properties");
		try {
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			return properties.entrySet().stream()
											.collect(Collectors.toMap(
														e -> e.getKey().toString(),
														e -> e.getValue())
													);
		} catch (IOException e) {
			return new HashMap<String, Object>();
		}
	}
}

/* 
 //To foredisabling :
 // spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
 //https://stackoverflow.com/questions/40509395/cant-set-jpa-naming-strategy-after-configuring-multiple-data-sources-spring-1
protected Map<String, Object> jpaProperties() {
    Map<String, Object> props = new HashMap<>();
    props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
    props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
    return props;
}

@Primary
@Bean(name = "defaultEntityManager")
public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
    EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(auntDataSource())
        .packages(Aunt.class)
        .persistenceUnit("aunt")
        .properties(jpaProperties())
        .build();
}
*/
