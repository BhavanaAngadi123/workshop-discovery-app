package app.discovery.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workshops")
public class Workshop {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "host_id")
  private User host;

  @Column(nullable = false, length = 160)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private WorkshopCategory category = WorkshopCategory.OTHER;

  @Column(name = "price_cents", nullable = false)
  private long priceCents;

  @Column(nullable = false, length = 3)
  private String currency = "USD";

  @Column(name = "location_name", length = 200)
  private String locationName;

  @Column(length = 120)
  private String city;

  private Double latitude;
  private Double longitude;

  @Column(name = "cover_photo_url", length = 500)
  private String coverPhotoUrl;

  @Column(name = "poster_url", length = 500)
  private String posterUrl;

  /** What's included — mandatory, always visible. One item per line. */
  @Column(columnDefinition = "TEXT")
  private String includes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private WorkshopStatus status = WorkshopStatus.DRAFT;

  @Column(name = "rating_avg")
  private double ratingAvg = 0.0;

  @Column(name = "rating_count")
  private int ratingCount = 0;

  @Column(name = "season_slug", length = 60)
  private String seasonSlug;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("sortOrder ASC")
  private List<WorkshopPhoto> photos = new ArrayList<>();

  public Long getId() { return id; }
  public User getHost() { return host; }
  public void setHost(User host) { this.host = host; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public WorkshopCategory getCategory() { return category; }
  public void setCategory(WorkshopCategory category) { this.category = category; }
  public long getPriceCents() { return priceCents; }
  public void setPriceCents(long priceCents) { this.priceCents = priceCents; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
  public String getLocationName() { return locationName; }
  public void setLocationName(String locationName) { this.locationName = locationName; }
  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }
  public Double getLatitude() { return latitude; }
  public void setLatitude(Double latitude) { this.latitude = latitude; }
  public Double getLongitude() { return longitude; }
  public void setLongitude(Double longitude) { this.longitude = longitude; }
  public String getCoverPhotoUrl() { return coverPhotoUrl; }
  public void setCoverPhotoUrl(String coverPhotoUrl) { this.coverPhotoUrl = coverPhotoUrl; }
  public String getPosterUrl() { return posterUrl; }
  public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
  public String getIncludes() { return includes; }
  public void setIncludes(String includes) { this.includes = includes; }
  public WorkshopStatus getStatus() { return status; }
  public void setStatus(WorkshopStatus status) { this.status = status; }
  public double getRatingAvg() { return ratingAvg; }
  public void setRatingAvg(double ratingAvg) { this.ratingAvg = ratingAvg; }
  public int getRatingCount() { return ratingCount; }
  public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
  public String getSeasonSlug() { return seasonSlug; }
  public void setSeasonSlug(String seasonSlug) { this.seasonSlug = seasonSlug; }
  public Instant getCreatedAt() { return createdAt; }
  public List<WorkshopPhoto> getPhotos() { return photos; }
}
