package org.spring.groupAir.airplane.service;

import lombok.RequiredArgsConstructor;
import org.spring.groupAir.airplane.dto.AirplaneDto;
import org.spring.groupAir.airplane.entity.AirPlaneEntity;
import org.spring.groupAir.airplane.repository.AirplaneRepository;
import org.spring.groupAir.airplane.service.serviceInterface.AirPlaneServiceInterface;
import org.spring.groupAir.member.dto.MemberDto;
import org.spring.groupAir.member.entity.MemberEntity;
import org.spring.groupAir.salary.dto.SalaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AirplaneService implements AirPlaneServiceInterface {

    private final AirplaneRepository airplaneRepository;

    @Override
    public Page<AirplaneDto> allAirplane(Pageable pageable, String subject, String search) {

        Page<AirPlaneEntity> airPlaneEntityPage;

        if (subject == null || search == null) {
            airPlaneEntityPage = airplaneRepository.findAll(pageable);
        } else if (subject.equals("fromArea")) {
            airPlaneEntityPage = airplaneRepository.findByFromAreaContains(pageable, search);
        } else if (subject.equals("toArea")) {
            airPlaneEntityPage = airplaneRepository.findByToAreaContains(pageable, search);
        } else if (subject.equals("airplane")) {
            airPlaneEntityPage = airplaneRepository.findByAirplaneContains(pageable, search);
        } else {
            airPlaneEntityPage = airplaneRepository.findAll(pageable);
        }

        Page<AirplaneDto> airplaneDtoPage = airPlaneEntityPage.map(airPlaneEntity ->
            AirplaneDto.builder()
                .id(airPlaneEntity.getId())
                .fromTime(airPlaneEntity.getFromTime())
                .fromArea(airPlaneEntity.getFromArea())
                .toArea(airPlaneEntity.getToArea())
                .toTime(airPlaneEntity.getToTime())
                .timeTaken(airPlaneEntity.getTimeTaken())
                .airplane(airPlaneEntity.getAirplane())
                .memberEntity(airPlaneEntity.getMemberEntity())
                .build()
        );

        return airplaneDtoPage;
    }

    @Override
    public void addAirplane(AirplaneDto airplaneDto) {

        LocalDateTime toTime = airplaneDto.getToTime();
        LocalDateTime fromTime = airplaneDto.getFromTime();

        airplaneDto.setMemberEntity(MemberEntity.builder().id(airplaneDto.getMemberId()).build());

        int timeTaken = (int) Duration.between(toTime, fromTime).toHours();

        AirPlaneEntity airPlaneEntity = AirPlaneEntity
            .builder()
            .fromTime(airplaneDto.getFromTime())
            .fromArea(airplaneDto.getFromArea())
            .toArea(airplaneDto.getToArea())
            .toTime(airplaneDto.getToTime())
            .timeTaken(timeTaken)
            .airplane(airplaneDto.getAirplane())
            .memberEntity(airplaneDto.getMemberEntity())
            .build();

        airplaneRepository.save(airPlaneEntity);
    }

    @Override
    public AirplaneDto airplaneDetail(Long id) {

        AirPlaneEntity airPlaneEntity = airplaneRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        AirplaneDto airplaneDto = AirplaneDto.builder()
            .id(airPlaneEntity.getId())
            .toArea(airPlaneEntity.getToArea())
            .fromArea(airPlaneEntity.getFromArea())
            .toTime(airPlaneEntity.getToTime())
            .fromTime(airPlaneEntity.getFromTime())
            .airplane(airPlaneEntity.getAirplane())
            .timeTaken(airPlaneEntity.getTimeTaken())
            .memberEntity(airPlaneEntity.getMemberEntity())
            .build();

        return airplaneDto;
    }

    @Override
    public void airplaneDelete(Long id) {

        airplaneRepository.deleteById(id);

    }

    @Override
    public Page<AirplaneDto> myAirplane(Pageable pageable, Long id) {

        Page<AirPlaneEntity> airPlaneEntityPage
            = airplaneRepository.findByMemberEntityId(pageable, id);

        Page<AirplaneDto> airplaneDtoPage = airPlaneEntityPage.map(airplaneEntity->
            AirplaneDto.builder()
                .id(airplaneEntity.getId())
                .toArea(airplaneEntity.getToArea())
                .fromArea(airplaneEntity.getFromArea())
                .toTime(airplaneEntity.getToTime())
                .fromTime(airplaneEntity.getFromTime())
                .airplane(airplaneEntity.getAirplane())
                .timeTaken(airplaneEntity.getTimeTaken())
                .memberEntity(airplaneEntity.getMemberEntity())
                .build()
            );

        return airplaneDtoPage;
    }

    @Override
    public Page<AirplaneDto> todayMyAirplane(Pageable pageable, Long id) {

        Date today = Date.valueOf(LocalDate.now());

        Page<AirPlaneEntity> airPlaneEntityPage
            = airplaneRepository.findTodayAirplane(pageable, id, today);

        Page<AirplaneDto> airplaneDtoPage = airPlaneEntityPage.map(airplaneEntity->
            AirplaneDto.builder()
                .id(airplaneEntity.getId())
                .toArea(airplaneEntity.getToArea())
                .fromArea(airplaneEntity.getFromArea())
                .toTime(airplaneEntity.getToTime())
                .fromTime(airplaneEntity.getFromTime())
                .airplane(airplaneEntity.getAirplane())
                .timeTaken(airplaneEntity.getTimeTaken())
                .memberEntity(airplaneEntity.getMemberEntity())
                .build()
        );

        return airplaneDtoPage;
    }

    @Override
    public void deleteOverTimeAirplane() {
        LocalDateTime now = LocalDateTime.now();
        airplaneRepository.deleteByFromTimeBefore(now);
    }
}
