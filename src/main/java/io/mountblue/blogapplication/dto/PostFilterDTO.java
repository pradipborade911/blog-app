package io.mountblue.blogapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostFilterDTO {
    private int pageNumber = 0;
    private int pageSize = 3;
    private List<String> tags;
    private List<String> authors;
    private String order;
    private LocalDate date;

    private LocalDateTime startOfDay = LocalDateTime.of(2020, 1, 1, 1,1);

    private LocalDateTime endOfDay = LocalDateTime.now();

    public void setDateTime(LocalDate dateTime) {
        this.date = dateTime;
        if(dateTime!=null){
            this.startOfDay = this.date.atStartOfDay();
            this.endOfDay = this.date.atTime(23, 59, 59, 999999999);
        }
    }

    public LocalDateTime getDateTime() {
        return date.atStartOfDay();
    }

    @Override
    public String toString() {
        return "PostFilterDTO{" +
                "authors=" + authors +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", tags=" + tags +
                ", order='" + order + '\'' +
                ", dateTime=" + date +
                ", startOfDay=" + startOfDay +
                ", endOfDay=" + endOfDay +
                '}';
    }
}
