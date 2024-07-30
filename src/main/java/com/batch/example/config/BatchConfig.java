package com.batch.example.config;

import com.batch.example.dto.ConsumerRequestDTO;
import com.batch.example.listener.job.ConsumerJob;
import com.batch.example.listener.step.ConsumerStep;
import com.batch.example.model.Consumer;
import com.batch.example.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

  private final PlatformTransactionManager transactionManager;

  private final ConsumerRepository consumerRepository;
  private final JobRepository jobRepository;

  @Bean
  public FlatFileItemReader<ConsumerRequestDTO> reader() {
    FlatFileItemReader<ConsumerRequestDTO> itemReader = new FlatFileItemReader<>();
    itemReader.setResource(new FileSystemResource("src/main/resources/consumer.csv"));
    itemReader.setName("csvReader");
    itemReader.setLinesToSkip(1);
    itemReader.setLineMapper(lineMapper());
    return itemReader;
  }

  private LineMapper<ConsumerRequestDTO> lineMapper() {
    DefaultLineMapper<ConsumerRequestDTO> lineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames(
        "personId","firstName","lastName","email","gender","birthday","country"
    );

    BeanWrapperFieldSetMapper<ConsumerRequestDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(ConsumerRequestDTO.class);

    lineMapper.setLineTokenizer(lineTokenizer);
    lineMapper.setFieldSetMapper(fieldSetMapper);
    return lineMapper;
  }

  @Bean
  public ConsumerProcessor processor() {
    return new ConsumerProcessor();
  }

  @Bean
  public RepositoryItemWriter<Consumer> writer() {
    RepositoryItemWriter<Consumer> writer = new RepositoryItemWriter<>();
    writer.setRepository(consumerRepository);
    writer.setMethodName("save");
    return writer;
  }

  @Bean
  public Step step1() {
    return new StepBuilder("csv-step", jobRepository)
        .<ConsumerRequestDTO, Consumer>chunk(10, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .listener(stepExecutionListener())
        .taskExecutor(taskExecutor())
        .build();
  }

  @Bean
  public Job runJob() {
    return new JobBuilder("import-consumer", jobRepository)
        .listener(jobExecutionListener())
        .flow(step1())
        .end()
        .build();
  }

  @Bean
  public TaskExecutor taskExecutor() {
    SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
    asyncTaskExecutor.setConcurrencyLimit(10);
    return asyncTaskExecutor;
  }

  @Bean
  public ConsumerJob stepExecutionListener() {
    return new ConsumerJob(consumerRepository);
  }


  @Bean
  public ConsumerStep jobExecutionListener() {
    return new ConsumerStep();
  }
}
