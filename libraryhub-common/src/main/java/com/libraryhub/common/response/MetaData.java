package com.libraryhub.common.response;

import com.libraryhub.common.constants.Constants;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
    private String status = Constants.SUCCESS;
    private String message;
    private String code;
    private String time = LocalDateTime.now().toString();
}
