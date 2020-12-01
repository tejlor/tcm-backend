package pl.olawa.telech.tcm.utils;

import javax.persistence.EntityManager;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

@SpringBootTest 
@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@Import(TcmTestConfiguration.class)
public class BaseTest {

	@Autowired
	protected EntityManager entityManager;
	
	protected <T extends AbstractEntity> void save(T entity) {
		entityManager.persist(entity);
	}
	
	protected <T extends AbstractEntity> void flush() {
		entityManager.flush();
	}
}
