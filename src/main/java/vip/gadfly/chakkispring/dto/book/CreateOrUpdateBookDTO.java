package vip.gadfly.chakkispring.dto.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class CreateOrUpdateBookDTO {

    @NotEmpty(message = "{book.title.not-empty}")
    @Size(max = 50, message = "{book.title.size}")
    private String title;

    @NotEmpty(message = "{book.author.not-empty}")
    @Size(max = 50, message = "{book.author.size}")
    private String author;

    @NotEmpty(message = "{book.summary.not-empty}")
    @Size(max = 1000, message = "{book.summary.size}")
    private String summary;

    @Size(max = 100, message = "{book.image.size}")
    private String image;
}
