package jp.co.jc21ps.activity_management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.jc21ps.activity_management.dto.ParticipantListDto;
import jp.co.jc21ps.activity_management.entity.ParticipantListEntity;
import jp.co.jc21ps.activity_management.repository.ParticipantListRepository;
import jp.co.jc21ps.activity_management.dto.ParticipantDto;

@Service
public class ParticipantListService {
    private final ParticipantListRepository participantListRepository;

    public ParticipantListService(ParticipantListRepository participantListRepository) {
        this.participantListRepository = participantListRepository;
    }

    // 初期画面表示
    public List<ParticipantDto> getParticipantListData(ParticipantListDto paramDto) {

        // entityに値をセット
        ParticipantListEntity participantListEntity = new ParticipantListEntity();
        participantListEntity.setActivityId(paramDto.getActivityId());
        participantListEntity.setUserId(paramDto.getUserId());
        participantListEntity.setUserName(paramDto.getUserName());

        // 参加者一覧データ取得
        List<ParticipantListEntity> participantList =
                participantListRepository.getParticipantListData(participantListEntity);

        // Controller が必要としている List<ParticipantDto>
        List<ParticipantDto> resultList = new ArrayList<>();

        for (ParticipantListEntity entity : participantList) {

            ParticipantDto dto = new ParticipantDto();
            dto.setParticipantUserId(entity.getUserId());
            dto.setParticipantUserName(entity.getUserName());
            dto.setParticipantClubName(entity.getClubName()); // エンティティにある前提

            resultList.add(dto);
        }

        return resultList;
    }
}
