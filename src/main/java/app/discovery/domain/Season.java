package app.discovery.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "seasons")
public class Season {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 60)
  private String slug;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "start_month", nullable = false)
  private int startMonth;

  @Column(name = "end_month", nullable = false)
  private int endMonth;

  @Column(name = "banner_url", length = 500)
  private String bannerUrl;

  @Column(nullable = false)
  private boolean active = true;

  public Long getId() { return id; }
  public String getSlug() { return slug; }
  public void setSlug(String slug) { this.slug = slug; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public int getStartMonth() { return startMonth; }
  public void setStartMonth(int startMonth) { this.startMonth = startMonth; }
  public int getEndMonth() { return endMonth; }
  public void setEndMonth(int endMonth) { this.endMonth = endMonth; }
  public String getBannerUrl() { return bannerUrl; }
  public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
  public boolean isActive() { return active; }
  public void setActive(boolean active) { this.active = active; }
}
