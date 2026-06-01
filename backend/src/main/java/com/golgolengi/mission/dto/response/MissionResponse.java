package com.golgolengi.mission.dto.response;

import com.golgolengi.mission.domain.Mission;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MissionResponse {
    private String missionId;
    private String familyId;
    private String title;
    private String description;
    private String category;
    private String status;
    private int targetCount;
    private int currentValue;
    private long completedCount;
    private int totalFamilyCount;
    private String unit;
    private LocalDate endDate;

    public static MissionResponse from(Mission mission) {
        return MissionResponse.builder()
                .missionId(mission.getId().toHexString())
                .familyId(mission.getFamilyId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .category(mission.getCategory())
                .status(mission.getStatus())
                .targetCount(mission.getTargetCount())
                .currentValue(0)
                .completedCount(0)
                .totalFamilyCount(0)
                .unit(mission.getUnit())
                .endDate(mission.getEndDate())
                .build();
    }

    public static MissionResponse from(
            Mission mission,
            int currentValue,
            long completedCount,
            int totalFamilyCount
    ) {
        String status = currentValue >= mission.getTargetCount() ? "COMPLETED" : mission.getStatus();
        return MissionResponse.builder()
                .missionId(mission.getId().toHexString())
                .familyId(mission.getFamilyId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .category(mission.getCategory())
                .status(status)
                .targetCount(mission.getTargetCount())
                .currentValue(currentValue)
                .completedCount(completedCount)
                .totalFamilyCount(totalFamilyCount)
                .unit(mission.getUnit())
                .endDate(mission.getEndDate())
                .build();
    }
}
