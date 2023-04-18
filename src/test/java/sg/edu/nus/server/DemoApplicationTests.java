package sg.edu.nus.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.server.dao.VerificationTaskRepository;
import sg.edu.nus.server.model.VerificationTask;
import sg.edu.nus.verification.pass.*;
import sg.edu.nus.verification.verifier.Verifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	private VerificationTaskRepository repo;

	@Test
	void contextLoads() {

	}

	@Test
	void testVerifier() {
		Verifier verifier = new Verifier();
		String sample, target;
		try {
			sample = Files.readString(Paths.get("src/main/resources/store/sample.json"));
			target = Files.readString(Paths.get("src/main/resources/store/design1.json"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		verifier.updateExample(sample);
		verifier.updateTarget(target);
		verifier.addPass(new UselessElementsPass());
		verifier.addPass(new ImpossibleConnectionPass());
		verifier.addPass(new ConnectivityAnalysisPass());
		verifier.addPass(new ShortElementsPass());
		verifier.addPass(new ElementMatchingPass());
		verifier.addPass(new ConnectionMatchingPass());
		verifier.executeAllPasses();
		verifier.summaryInfo();
	}

}
