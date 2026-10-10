package app.discovery.service;

import app.discovery.domain.Review;
import app.discovery.domain.User;
import app.discovery.domain.Workshop;
import app.discovery.repo.ReviewRepository;
import app.discovery.repo.UserRepository;
import app.discovery.repo.WorkshopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
  private final ReviewRepository reviews;
  private final WorkshopRepository workshops;
  private final UserRepository users;
  private final AuditService audit;

  public ReviewService(ReviewRepository reviews, WorkshopRepository workshops,
                      UserRepository users, AuditService audit) {
    this.reviews = reviews;
    this.workshops = workshops;
    this.users = users;
    this.audit = audit;
  }

  @Transactional
  public Review add(Long authorId, Long workshopId, int rating, String text) {
    if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be 1-5");
    Workshop w = workshops.findById(workshopId).orElseThrow(() -> new IllegalArgumentException("Workshop not found"));
    User author = users.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    Review r = new Review();
    r.setWorkshop(w);
    r.setAuthor(author);
    r.setRating(rating);
    r.setText(text == null ? "" : text.trim());
    reviews.save(r);
    // recompute aggregate
    var all = reviews.findByWorkshopIdOrderByCreatedAtDesc(workshopId, Pageable.unpaged());
    double avg = all.getContent().stream().mapToInt(Review::getRating).average().orElse(0);
    w.setRatingAvg(Math.round(avg * 10.0) / 10.0);
    w.setRatingCount((int) all.getTotalElements());
    audit.log(authorId, "REVIEW_ADD", "Review", r.getId());
    return r;
  }

  public Page<Review> forWorkshop(Long workshopId, Pageable pageable) {
    return reviews.findByWorkshopIdOrderByCreatedAtDesc(workshopId, pageable);
  }
}
