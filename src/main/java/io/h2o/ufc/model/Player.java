package io.h2o.ufc.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "player")

public class Player {

    @Id
    private Integer player_id;
    private String player_name;
    private String player_nickname;
    private String image_path;
    private String match_played;
    private String score;
    private String total_wins;
    private String win_percent;

}
