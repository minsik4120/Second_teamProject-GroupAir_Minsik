package org.spring.groupAir.member.repository;

import org.spring.groupAir.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<Object> findByUserEmail(String userEmail);
}
