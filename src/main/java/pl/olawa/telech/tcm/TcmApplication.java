package pl.olawa.telech.tcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import pl.olawa.telech.tcm.commons.dao.DAOImpl;


@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "pl.olawa.telech.tcm.dao", repositoryBaseClass = DAOImpl.class)
public class TcmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcmApplication.class, args);
	}

}