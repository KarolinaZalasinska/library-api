package dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserActivityDto {
    private Long id;
    private String bookTitle;
    private String actionType;
    private LocalDate actionDate;
    private Long userId;
    private Long copyId;
}
