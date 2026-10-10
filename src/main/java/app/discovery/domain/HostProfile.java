package app.discovery.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "host_profiles")
public class HostProfile {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @Column(length = 160)
  private String tagline;

  @Column(columnDefinition = "TEXT")
  private String bio;

  @Column(name = "avatar_url", length = 500)
  private String avatarUrl;

  @Column(nullable = false)
  private boolean verified = false;

  @Column(name = "rating_avg")
  private double ratingAvg = 0.0;

  @Column(name = "rating_count")
  private int ratingCount = 0;

  public Long getId() { return id; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public String getTagline() { return tagline; }
  public void setTagline(String tagline) { this.tagline = tagline; }
  public String getBio() { return bio; }
  public void setBio(String bio) { this.bio = bio; }
  public String getAvatarUrl() { return avatarUrl; }
  public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
  public boolean isVerified() { return verified; }
  public void setVerified(boolean verified) { this.verified = verified; }
  public double getRatingAvg() { return ratingAvg; }
  public void setRatingAvg(double ratingAvg) { this.ratingAvg = ratingAvg; }
  public int getRatingCount() { return ratingCount; }
  public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
}
