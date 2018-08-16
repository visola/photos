package org.visola.lifebooster.config;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.visola.lifebooster.dao.JournalEntryDao;

@Configuration
public class JDBIConfiguration {

  @Bean
  public Jdbi jdbi(DataSource dataSource) {
    Jdbi jdbi = Jdbi.create(dataSource);
    jdbi.installPlugin(new SqlObjectPlugin());
    return jdbi;
  }

  @Bean
  public JournalEntryDao journalEntryDao(Jdbi jdbi) {
    return jdbi.onDemand(JournalEntryDao.class);
  }

}
