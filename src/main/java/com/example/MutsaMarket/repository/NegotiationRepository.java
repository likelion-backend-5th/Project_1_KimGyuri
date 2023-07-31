package com.example.MutsaMarket.repository;

import com.example.MutsaMarket.entity.NegotiationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity, Long> {
    List<NegotiationEntity> findBySalesItem_Id(Long itemId);
    Page<NegotiationEntity> findAllBySalesItem_IdAndWriterAndPassword(Long itemId, String writer, String password, Pageable pageable);
    Page<NegotiationEntity> findAllBySalesItem_Id(Long itemId, Pageable pageable);
    List<NegotiationEntity> findAllBySalesItem_Id(Long itemId);
    Page<NegotiationEntity> findAllBySalesItem_IdAndUser_Id(Long itemId, Long userId, Pageable pageable);
}
