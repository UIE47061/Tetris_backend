package tetris.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tetris_records")
public class TetrisRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; 
    private Integer score;
    private Integer level;
    private Integer duration;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @PostLoad
    protected void onLoad() {
        // 確保舊資料也有 createdAt 值
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}