package tetris.demo.repository;

import tetris.demo.model.TetrisRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecordRepository extends JpaRepository<TetrisRecord, Long> {
    List<TetrisRecord> findTop10ByOrderByScoreDesc();
    List<TetrisRecord> findByUsernameOrderByScoreDesc(String username);
}