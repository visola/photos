package org.visola.lifebooster.config;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.visola.lifebooster.dao.JournalEntryDao;
import org.visola.lifebooster.dao.ProjectDao;
import org.visola.lifebooster.dao.UserDao;

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

  @Bean
  public ProjectDao projectDao(Jdbi jdbi) {
    return jdbi.onDemand(ProjectDao.class);
  }

  @Bean
  public UserDao userDao(Jdbi jdbi) {
    return jdbi.onDemand(UserDao.class);
  }

}
