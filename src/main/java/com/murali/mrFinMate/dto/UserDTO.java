package com.murali.mrFinMate.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String userName;
    private String password;
    private String phoneNo;
    private String emailId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
