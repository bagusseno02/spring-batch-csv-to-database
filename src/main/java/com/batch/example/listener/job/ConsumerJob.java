package com.batch.example.listener.job;

import com.batch.example.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

@Slf4j
@RequiredArgsConstructor
public class ConsumerJob extends JobExecutionListenerSupport {

  private final ConsumerRepository consumerRepository;

  @Override
  public void beforeJob(JobExecution jobExecution){
    log.info("Consumer Job | Before Job | Executing job id : " + jobExecution.getJobId());
    super.beforeJob(jobExecution);
  }

  @Override
  public void afterJob(JobExecution jobExecution){

    log.info("Consumer Job | After Job | Executing job id : " + jobExecution.getJobId());

    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("Job Completed");
    }

    this.consumerRepository.findAll()
        .forEach(person -> log.info("Found (" + person + ">) in the database.") );
  }
}
