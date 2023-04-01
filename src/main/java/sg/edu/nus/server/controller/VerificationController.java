package sg.edu.nus.server.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import sg.edu.nus.server.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @CrossOrigin
    @PostMapping("/verify")
    public HashMap<String, String> verify(@RequestBody VerifyParams bodyParam) {
        String taskToken = service.launchVerificationTask(bodyParam.getTarget(), bodyParam.getSample());
        HashMap<String, String> responseJson = new HashMap<>();
        responseJson.put("token", taskToken);
        responseJson.put("status", "created");
        return responseJson;
    }

    @CrossOrigin
    @PostMapping("/verifyresult")
    public Map<String, Object> getResult(@RequestBody Map<String, String> body) {
        return service.queryVerificationTaskResult(body.get("token"));
    }
}

class VerifyParams {
    private String target;
    private String sample;

    public VerifyParams(String target, String sample) {
        this.target = target;
        this.sample = sample;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getSample() {
        return sample;
    }
}