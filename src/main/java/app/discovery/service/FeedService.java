package app.discovery.service;

import app.discovery.domain.ExperiencePost;
import app.discovery.domain.PostLike;
import app.discovery.domain.PostPhoto;
import app.discovery.domain.User;
import app.discovery.domain.Workshop;
import app.discovery.repo.ExperiencePostRepository;
import app.discovery.repo.PostLikeRepository;
import app.discovery.repo.UserRepository;
import app.discovery.repo.WorkshopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** The social experience feed — "what people did last weekend", driving discovery. */
@Service
public class FeedService {
  private final ExperiencePostRepository posts;
  private final PostLikeRepository likes;
  private final UserRepository users;
  private final WorkshopRepository workshops;
  private final AuditService audit;

  public FeedService(ExperiencePostRepository posts, PostLikeRepository likes,
                    UserRepository users, WorkshopRepository workshops, AuditService audit) {
    this.posts = posts;
    this.likes = likes;
    this.users = users;
    this.workshops = workshops;
    this.audit = audit;
  }

  @Transactional
  public ExperiencePost publish(Long authorId, Long workshopId, String text, List<String> photoUrls) {
    if (text == null || text.isBlank()) throw new IllegalArgumentException("Post text required");
    User author = users.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    ExperiencePost p = new ExperiencePost();
    p.setAuthor(author);
    if (workshopId != null) {
      Workshop w = workshops.findById(workshopId).orElseThrow(() -> new IllegalArgumentException("Workshop not found"));
      p.setWorkshop(w);
    }
    p.setText(text.trim());
    if (photoUrls != null) {
      int i = 0;
      for (String url : photoUrls) {
        if (url == null || url.isBlank()) continue;
        PostPhoto ph = new PostPhoto();
        ph.setPost(p);
        ph.setUrl(url.trim());
        ph.setSortOrder(i++);
        p.getPhotos().add(ph);
      }
    }
    posts.save(p);
    audit.log(authorId, "POST_PUBLISH", "ExperiencePost", p.getId());
    return p;
  }

  public Page<ExperiencePost> feed(Pageable pageable) {
    return posts.findAllByOrderByCreatedAtDesc(pageable);
  }

  public Page<ExperiencePost> forWorkshop(Long workshopId, Pageable pageable) {
    return posts.findByWorkshopIdOrderByCreatedAtDesc(workshopId, pageable);
  }

  @Transactional
  public boolean toggleLike(Long userId, Long postId) {
    ExperiencePost p = posts.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    var existing = likes.findByPostIdAndUserId(postId, userId);
    if (existing.isPresent()) {
      likes.delete(existing.get());
      p.setLikeCount(Math.max(0, p.getLikeCount() - 1));
      return false;
    }
    User u = users.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    PostLike like = new PostLike();
    like.setPost(p);
    like.setUser(u);
    likes.save(like);
    p.setLikeCount(p.getLikeCount() + 1);
    return true;
  }
}
