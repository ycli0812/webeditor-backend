package sg.edu.nus.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.server.dao.VerificationTaskRepository;
import sg.edu.nus.server.model.VerificationTask;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	private VerificationTaskRepository repo;

	@Test
	void contextLoads() {
//		VerificationTask task = new VerificationTask("task_id_01", "RESULT", null, new Date());
//		repo.save(task);
//		Optional<VerificationTask> res = repo.findAll();
//		if(res.isPresent()) {
//			System.out.println("Time is " + res.get().getTime());
//		}
	}

}
