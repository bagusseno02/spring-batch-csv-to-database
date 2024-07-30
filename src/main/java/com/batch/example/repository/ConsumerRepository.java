package com.batch.example.repository;

import com.batch.example.model.Consumer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, UUID> {

}
