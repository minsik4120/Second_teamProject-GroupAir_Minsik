package org.spring.groupAir.board.repository;

import org.spring.groupAir.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Long> {



//  @Query(value = "SELECT b FROM BoardEntity b WHERE b.boardSeparateEntity.id = :boardSeparateId")
//  Page<BoardEntity> findByBoardSeparateId(Pageable pageable, @Param("boardSeparateId") Long boardSeparateId);

  Page<BoardEntity> findByContentContaining(Pageable pageable, String search);

  Page<BoardEntity> findByWriterContaining(Pageable pageable, String search);

  Page<BoardEntity> findByTitleContaining(Pageable pageable, String search);


  // 조회수 1식 증가
  @Modifying
  @Query(value = "update BoardEntity b set b.hit=b.hit+1 where b.id=:id")
  void updateHitById(@Param("id") Long id);
}
