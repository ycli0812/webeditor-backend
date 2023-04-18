package sg.edu.nus.server.service.verification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.edu.nus.server.dao.VerificationTaskRepository;
import sg.edu.nus.server.model.VerificationTask;
import sg.edu.nus.server.threads.VerificationThreadPoolExecutor;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.pass.*;
import sg.edu.nus.verification.verifier.Verifier;

import java.util.*;

/**
 * Services for verification functionalities.
 *
 * @author Lyc
 * @version 2023.03.23
 */
@Service
public class VerificationService {
    /**
     * JPA Repository used to access database.
     */
    @Autowired
    private VerificationTaskRepository repository;

    /**
     * Thread pool executor especially for verification tasks.
     * Note that a service can also be asynchronous by adding notation @Async(name="pool name")
     */
    @Autowired
    private VerificationThreadPoolExecutor executor;

    public String launchVerificationTask(String targetCircuit, String sampleCircuit) {
        Date now = new Date();
        String token = UUID.randomUUID().toString();
        VerificationTask task = new VerificationTask(token, "PENDING", null, now);
        repository.save(task);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Construct Verifier and perform verification algorithm
                Verifier verifier = new Verifier();
                verifier.updateTarget(targetCircuit);
                verifier.updateExample(sampleCircuit);
                verifier.addPass(new UselessElementsPass());
                verifier.addPass(new ImpossibleConnectionPass());
                verifier.addPass(new ConnectivityAnalysisPass());
                verifier.addPass(new ShortElementsPass());
                verifier.addPass(new ElementMatchingPass());
                verifier.addPass(new ConnectionMatchingPass());
                verifier.executeAllPasses();
                // Transform output of algorithm and update database
                List<Info> output = verifier.getOutput();
                try {
                    task.setResult(new ObjectMapper().writeValueAsString(output));
                } catch (JsonProcessingException e) {
                    task.setResult("[]");
                }
                task.setStatus("DONE");
                repository.save(task);
            }
        });
        // TODO: handle exception that thread not be created successfully and return an object containing explanation
        return token;
    }

    public Map<String, Object> queryVerificationTaskResult(String token) {
        Optional<VerificationTask> dbResult = repository.findById(token);
        HashMap<String, Object> result = new HashMap<>();
        if(dbResult.isEmpty()) {
            result.put("status", "FAILED");
            result.put("msg", "An invalid token was provided.");
        } else {
            result.put("status", dbResult.get().getStatus());
            result.put("result", dbResult.get().getResult());
        }
        return result;
    }
}
