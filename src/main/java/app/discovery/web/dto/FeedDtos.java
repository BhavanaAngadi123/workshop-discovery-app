package app.discovery.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class FeedDtos {
  public record PostRequest(@NotBlank String text, Long workshopId, List<String> photoUrls) {}

  public record PostDto(Long id, String authorName, Long workshopId, String workshopTitle,
                        String text, List<String> photoUrls, int likeCount,
                        boolean likedByMe, String createdAt) {}
}
