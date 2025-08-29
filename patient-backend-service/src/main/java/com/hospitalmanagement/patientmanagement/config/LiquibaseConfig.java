package com.hospitalmanagement.patientmanagement.config;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiquibaseConfig {

	@Value("${spring.liquibase.change-log:classpath:/db/changelog/db.changelog-master.yaml}")
	private String changelogPath;

	@Value("${spring.liquibase.database-change-log-table:patient_management_db_databasechangelog}")
	private String changeLogTableName;

	@Value("${spring.liquibase.database-change-log-lock-table:patient_management_db_databasechangeloglock}")
	private String changeLogLockTableName;

	@Bean
	@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", havingValue = "true")
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(changelogPath);
		liquibase.setDatabaseChangeLogTable(changeLogTableName);
		liquibase.setDatabaseChangeLogLockTable(changeLogLockTableName);
		liquibase.setShouldRun(true);
		return liquibase;
	}
} 