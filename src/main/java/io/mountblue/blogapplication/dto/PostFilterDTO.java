package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostFilterDTO {
    private String order = "latest";

    private String searchQuery;

    private List<String> tags;

    private List<Long> authors;

    private LocalDate date;

    private int pageNumber = 0;

    private int pageSize = 3;

    private LocalDateTime startOfDay = LocalDateTime.now();

    private LocalDateTime endOfDay = LocalDateTime.now();

    public void setDate(LocalDate dateTime) {
        this.date = dateTime;
        if (dateTime != null) {
            this.startOfDay = this.date.atStartOfDay();
            this.endOfDay = this.date.atTime(23, 59, 59, 999999999);
        }
    }
}
