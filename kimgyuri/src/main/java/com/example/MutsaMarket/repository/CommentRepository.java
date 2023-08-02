package com.example.MutsaMarket.repository;

import com.example.MutsaMarket.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
