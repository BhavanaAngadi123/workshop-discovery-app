package app.discovery.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "post_likes", uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
public class PostLike {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_id")
  private ExperiencePost post;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  public Long getId() { return id; }
  public ExperiencePost getPost() { return post; }
  public void setPost(ExperiencePost post) { this.post = post; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
}
