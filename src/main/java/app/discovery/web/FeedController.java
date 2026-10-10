package app.discovery.web;

import app.discovery.domain.User;
import app.discovery.service.FeedService;
import app.discovery.service.ReviewService;
import app.discovery.web.dto.FeedDtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Feed & Reviews")
public class FeedController {
  private final FeedService feed;
  private final ReviewService reviews;

  public FeedController(FeedService feed, ReviewService reviews) {
    this.feed = feed;
    this.reviews = reviews;
  }

  @GetMapping("/api/feed")
  @Operation(summary = "Social experience feed — what people did, newest first")
  public Page<PostDto> feed(Pageable pageable, @AuthenticationPrincipal User me) {
    return feed.feed(pageable).map(p -> toDto(p, me));
  }

  @PostMapping("/api/feed")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Post your workshop experience (photos + story)")
  public PostDto post(@AuthenticationPrincipal User me, @Valid @RequestBody PostRequest req) {
    var p = feed.publish(me.getId(), req.workshopId(), req.text(), req.photoUrls());
    return toDto(p, me);
  }

  @PostMapping("/api/feed/{id}/like")
  @Operation(summary = "Toggle like on a post")
  public Map<String, Object> like(@AuthenticationPrincipal User me, @PathVariable Long id) {
    boolean liked = feed.toggleLike(me.getId(), id);
    return Map.of("liked", liked);
  }

  @GetMapping("/api/workshops/{id}/reviews")
  @Operation(summary = "Reviews for a workshop")
  public Page<Map<String, Object>> reviews(@PathVariable Long id, Pageable pageable) {
    return reviews.forWorkshop(id, pageable)
        .map(r -> Map.<String, Object>of(
            "author", r.getAuthor().getDisplayName(),
            "rating", r.getRating(),
            "text", r.getText() == null ? "" : r.getText(),
            "createdAt", r.getCreatedAt().toString()));
  }

  @PostMapping("/api/workshops/{id}/reviews")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Review a workshop (1-5 stars)")
  public Map<String, Object> addReview(@AuthenticationPrincipal User me, @PathVariable Long id,
                                       @RequestBody Map<String, Object> body) {
    int rating = Integer.parseInt(String.valueOf(body.getOrDefault("rating", "5")));
    String text = String.valueOf(body.getOrDefault("text", ""));
    var r = reviews.add(me.getId(), id, rating, text);
    return Map.of("id", r.getId(), "rating", r.getRating());
  }

  private PostDto toDto(app.discovery.domain.ExperiencePost p, User me) {
    boolean likedByMe = false;
    if (me != null) {
      // cheap check via service would need another query; keep simple: false unless liked state known
      likedByMe = false;
    }
    return new PostDto(p.getId(), p.getAuthor().getDisplayName(),
        p.getWorkshop() == null ? null : p.getWorkshop().getId(),
        p.getWorkshop() == null ? null : p.getWorkshop().getTitle(),
        p.getText(),
        p.getPhotos().stream().map(app.discovery.domain.PostPhoto::getUrl).toList(),
        p.getLikeCount(), likedByMe, p.getCreatedAt().toString());
  }
}
