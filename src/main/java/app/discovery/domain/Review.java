package app.discovery.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "workshop_id")
  private Workshop workshop;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id")
  private User author;

  @Column(nullable = false)
  private int rating;

  @Column(columnDefinition = "TEXT")
  private String text;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  public Long getId() { return id; }
  public Workshop getWorkshop() { return workshop; }
  public void setWorkshop(Workshop workshop) { this.workshop = workshop; }
  public User getAuthor() { return author; }
  public void setAuthor(User author) { this.author = author; }
  public int getRating() { return rating; }
  public void setRating(int rating) { this.rating = rating; }
  public String getText() { return text; }
  public void setText(String text) { this.text = text; }
  public Instant getCreatedAt() { return createdAt; }
}
