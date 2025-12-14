package tetris.demo.controller;

import tetris.demo.model.TetrisRecord;
import tetris.demo.repository.RecordRepository;
import tetris.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired private RecordRepository recordRepository;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/leaderboard")
    public List<TetrisRecord> getLeaderboard() {
        return recordRepository.findTop10ByOrderByScoreDesc();
    }

    @GetMapping("/user-stats")
    public Object getUserStats(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // 去掉 "Bearer "
            String username = jwtUtil.extractUsername(token);
            
            if (username != null) {
                List<TetrisRecord> userRecords = recordRepository.findByUsernameOrderByScoreDesc(username);
                
                int totalGames = userRecords.size();
                int highestScore = userRecords.isEmpty() ? 0 : userRecords.get(0).getScore();
                double avgScore = userRecords.stream()
                    .mapToInt(TetrisRecord::getScore)
                    .average()
                    .orElse(0.0);
                
                return java.util.Map.of(
                    "username", username,
                    "totalGames", totalGames,
                    "highestScore", highestScore,
                    "averageScore", avgScore,
                    "recentGames", userRecords.stream().limit(5).toList()
                );
            }
        } catch (Exception e) {
            return java.util.Map.of("error", "Invalid Token");
        }
        return java.util.Map.of("error", "Unauthorized");
    }

    @PostMapping("/submit")
    public String submitScore(@RequestHeader("Authorization") String authHeader, 
                              @RequestBody TetrisRecord record) {
        try {
            String token = authHeader.substring(7); // 去掉 "Bearer "
            String username = jwtUtil.extractUsername(token);
            
            if (username != null) {
                record.setUsername(username);
                recordRepository.save(record);
                return "Score saved!";
            }
        } catch (Exception e) {
            return "Invalid Token";
        }
        return "Error";
    }
}