package com.groupware.repository;

import com.groupware.entity.work.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptionRepository extends JpaRepository<Description, Integer> {
}
