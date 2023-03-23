package sg.edu.nus.server.service.verification;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sg.edu.nus.verification.verifier.Verifier;

@Service
public class VerificationService {
    public String launchVerificationTask(String targetCircuit, String sampleCircuit) {
        Verifier verifier = new Verifier();
        verifier.updateTarget(targetCircuit);
        verifier.updateExample(sampleCircuit);
        String token = "random_token";
        return token;
    }

    public String queryVerificationTaskResult(String token) {
        String status = "running";
        return status;
    }
}
