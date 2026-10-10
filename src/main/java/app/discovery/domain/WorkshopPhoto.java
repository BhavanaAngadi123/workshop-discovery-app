package app.discovery.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "workshop_photos")
public class WorkshopPhoto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "workshop_id")
  private Workshop workshop;

  @Column(nullable = false, length = 500)
  private String url;

  @Column(name = "sort_order")
  private int sortOrder = 0;

  public Long getId() { return id; }
  public Workshop getWorkshop() { return workshop; }
  public void setWorkshop(Workshop workshop) { this.workshop = workshop; }
  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }
  public int getSortOrder() { return sortOrder; }
  public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
