package app.discovery.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "experience_posts")
public class ExperiencePost {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "workshop_id")
  private Workshop workshop;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text;

  @Column(name = "like_count", nullable = false)
  private int likeCount = 0;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("sortOrder ASC")
  private List<PostPhoto> photos = new ArrayList<>();

  public Long getId() { return id; }
  public User getAuthor() { return author; }
  public void setAuthor(User author) { this.author = author; }
  public Workshop getWorkshop() { return workshop; }
  public void setWorkshop(Workshop workshop) { this.workshop = workshop; }
  public String getText() { return text; }
  public void setText(String text) { this.text = text; }
  public int getLikeCount() { return likeCount; }
  public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
  public Instant getCreatedAt() { return createdAt; }
  public List<PostPhoto> getPhotos() { return photos; }
}
