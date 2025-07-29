package com.groupware.repository;

import com.groupware.entity.work.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, Integer> {
}
