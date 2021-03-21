package com.thomasdriscoll.pluto.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.thomasdriscoll.pluto.lib.dao")
public class BudgetConfig {
}
