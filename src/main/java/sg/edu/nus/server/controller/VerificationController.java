package sg.edu.nus.server.controller;

import sg.edu.nus.server.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for verification-involved interfaces.
 * Interface list:
 * <ul>
 *     <li>{@code /verify} start a verification task.</li>
 *     <li>{@code /verifyresult} get the result of a verification task.</li>
 * </ul>
 *
 * @author Lyc
 * @version 2023.03.22
 */
@RestController
public class VerificationController {
    @Autowired
    private VerificationService service;

    @PostMapping("/verify")
    public String verify(String targetCircuit, String sampleCircuit) {
        return service.launchVerificationTask(targetCircuit, sampleCircuit);
    }

    @PostMapping("/verifyresult")
    public String getResult(String token) {
        return service.queryVerificationTaskResult(token);
    }
}
