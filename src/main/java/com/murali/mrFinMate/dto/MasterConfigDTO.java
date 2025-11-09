package com.murali.mrFinMate.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterConfigDTO {
    private Long id;
    private Long profileId;
    private String profileName;
    private String month;
    private String year;
    private String configName;
    private String configValue;
    private String description;
    private Boolean active;
    private String createdUser;
    private String updatedUser;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
