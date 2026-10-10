package app.discovery.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "post_photos")
public class PostPhoto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_id")
  private ExperiencePost post;

  @Column(nullable = false, length = 500)
  private String url;

  @Column(name = "sort_order")
  private int sortOrder = 0;

  public Long getId() { return id; }
  public ExperiencePost getPost() { return post; }
  public void setPost(ExperiencePost post) { this.post = post; }
  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }
  public int getSortOrder() { return sortOrder; }
  public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
