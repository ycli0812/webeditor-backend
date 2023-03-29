package sg.edu.nus.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import sg.edu.nus.server.model.VerificationTask;

import java.util.Date;
import java.util.List;

public interface VerificationTaskRepository extends JpaRepository<VerificationTask, String>,
        JpaSpecificationExecutor<VerificationTask> {}
