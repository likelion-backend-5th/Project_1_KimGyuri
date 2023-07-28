package com.example.MutsaMarket.service;

import com.example.MutsaMarket.dto.negotiation.ProposalDto;
import com.example.MutsaMarket.dto.negotiation.ProposalListDto;
import com.example.MutsaMarket.dto.negotiation.UpdateProposalDto;
import com.example.MutsaMarket.entity.NegotiationEntity;
import com.example.MutsaMarket.entity.SalesItemEntity;
import com.example.MutsaMarket.exceptions.*;
import com.example.MutsaMarket.repository.NegotiationRepository;
import com.example.MutsaMarket.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final SalesItemRepository salesItemRepository;
    private final NegotiationRepository negotiationRepository;

    //구매 제안 등록
    public ProposalDto createProposal(Long itemId, ProposalDto dto) {
        //itemId 없을 때 구매 제안 등록 방지
        Optional<SalesItemEntity> optionalSalesItem = salesItemRepository.findById(itemId);
        if(optionalSalesItem.isEmpty()) {
            throw new ItemNotFoundException();
        }
        SalesItemEntity item = optionalSalesItem.get();
        if(item.getStatus().equals("판매 완료"))
            throw new SoldOutException();

        NegotiationEntity newProposal = new NegotiationEntity();
        newProposal.setWriter(dto.getWriter());
        newProposal.setPassword(dto.getPassword());
        newProposal.setSuggestedPrice(dto.getSuggestedPrice());
        newProposal.setSalesItem(optionalSalesItem.get());
        newProposal.setStatus("제안");
        newProposal = negotiationRepository.save(newProposal);
        return ProposalDto.fromEntity(newProposal);
    }

    //구매 제안 조회
    public Page<ProposalListDto> readProposalAll(Long itemId, String writer, String password, Integer page) {
        Optional<SalesItemEntity> optionalSalesItem = salesItemRepository.findById(itemId);
        List<NegotiationEntity> optionalNegotiation = negotiationRepository.findBySalesItem_Id(itemId);;

        //대상 물품이 없을 때
        if (optionalSalesItem.isEmpty())
            throw new ItemNotFoundException();

        //대상 제안이 없을 때
        if (optionalNegotiation.isEmpty())
            throw new NegotiationNotFoundException();

        //물품 등록자의 경우
        SalesItemEntity item = optionalSalesItem.get();
        if(item.getWriter().equals(writer) && item.getPassword().equals(password)) {
            Pageable pageable = PageRequest.of(page, 25, Sort.by("id"));
            Page<NegotiationEntity> proposalEntityPage = negotiationRepository.findAllBySalesItem_Id(itemId, pageable);
            return proposalEntityPage.map(ProposalListDto::fromEntity);
        }

        //구매 제안자의 경우
        else if(!optionalNegotiation.isEmpty()) {
            Pageable pageable = PageRequest.of(page, 25, Sort.by("id"));
            Page<NegotiationEntity> proposalEntityPage = negotiationRepository.findAllBySalesItem_IdAndWriterAndPassword(itemId, writer, password, pageable);
            return proposalEntityPage.map(ProposalListDto::fromEntity);
        }
        else {
            throw new AuthorizationException();
        }
    }

    //구매 제안 수정 / 수락/거절 / 확정
    public String updateProposal(Long itemId, Long proposalId, UpdateProposalDto dto) {
        Optional<NegotiationEntity> optionalProposal = negotiationRepository.findById(proposalId);
        if (optionalProposal.isEmpty())
            throw new NegotiationNotFoundException();

        Optional<SalesItemEntity> optionalSalesItem = salesItemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty())
            throw new ItemNotFoundException();

        NegotiationEntity proposal = optionalProposal.get();
        SalesItemEntity item = optionalSalesItem.get();

        //구매 제안자 (제안 수정 / 제안 확정)
        if(proposal.getSalesItem().getId().equals(itemId) && proposal.getWriter().equals(dto.getWriter()) && proposal.getPassword().equals(dto.getPassword())) {

            if(proposal.getStatus().equals("수락")) {
                proposal.setStatus(dto.getStatus());
                negotiationRepository.save(proposal); //제안 확정

                item.setStatus("판매 완료");
                salesItemRepository.save(item); //해당 물품 판매 완료

                List<NegotiationEntity> proposals = negotiationRepository.findAllBySalesItem_Id(itemId);
                for (NegotiationEntity negotiation : proposals) {
                    if (!negotiation.getStatus().equals("확정")) {
                        negotiation.setStatus("거절");
                        negotiationRepository.save(negotiation);
                    }
                }
                return "confirm";
            } else if (proposal.getStatus().equals("제안") && dto.getStatus().isEmpty()){
                proposal.setSuggestedPrice(dto.getSuggestedPrice());
                negotiationRepository.save(proposal);
                return "edit";
            } else {
                throw new CheckStatusException();
            }
        }

        //물품 등록자 (제안 수락/거절)
        else if(proposal.getSalesItem().getId().equals(itemId) && item.getWriter().equals(dto.getWriter()) && item.getPassword().equals(dto.getPassword())) {
            if(proposal.getStatus().equals("제안")) {
                proposal.setStatus(dto.getStatus());
                negotiationRepository.save(proposal);
                return "modify";
            } else {
                throw new CheckStatusException();
            }
        }

        else {
            throw new AuthorizationException();
        }
    }

    //구매 제안 삭제
    public void deleteProposal(Long itemId, Long proposalId, UpdateProposalDto dto) {
        Optional<NegotiationEntity> optionalProposal = negotiationRepository.findById(proposalId);
        if (optionalProposal.isEmpty())
            throw new NegotiationNotFoundException();

        NegotiationEntity proposal = optionalProposal.get();
        if(proposal.getSalesItem().getId().equals(itemId) || proposal.getId().equals(proposalId))
            throw new ItemNotFoundException();

        if(proposal.getWriter().equals(dto.getWriter()) && proposal.getPassword().equals(dto.getPassword()))
            negotiationRepository.deleteById(proposalId);
        else
            throw new AuthorizationException();
    }
}
