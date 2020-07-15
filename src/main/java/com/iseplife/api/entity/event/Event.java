package com.iseplife.api.entity.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iseplife.api.entity.feed.Feed;
import com.iseplife.api.entity.club.Club;
import com.iseplife.api.constants.EventType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Guillaume on 27/07/2017.
 * back
 */
@Entity
public class Event {

  @Id
  @GeneratedValue
  private Long id;

  private String title;
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  private EventType type;

  private Date start;
  private Date end;
  private String location;
  private String ticketUrl = null;
  private Float price = null;

  @JsonIgnore
  private Boolean published = false;
  private Boolean closed = false;

  @Column(columnDefinition = "TEXT")
  private String description;

  //An event can have event child in specific cases (e.g BDE Campaign)
  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL)
  private List<Event> events;

  @ManyToOne
  private Club club;

  @OneToOne
  private Feed feed;

  @JsonIgnore
  @ManyToMany
  private List<Feed> targets;

  @JsonIgnore
  @OneToOne
  private Event previousEdition;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date startsAt) {
    this.start = startsAt;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Club getClub() {
    return club;
  }

  public void setClub(Club club) {
    this.club = club;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date endsAt) {
    this.end = endsAt;
  }

  public Event getPreviousEdition() {
    return previousEdition;
  }

  public void setPreviousEdition(Event previousEdition) {
    this.previousEdition = previousEdition;
  }

  public List<Event> getEvents() {
    return events;
  }

  public void addEvent(Event event) {
    this.events.add(event);
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }

  public EventType getType() {
    return type;
  }

  public void setType(EventType type) {
    this.type = type;
  }

  public String getTicketUrl() {
    return ticketUrl;
  }

  public void setTicketUrl(String ticketUrl) {
    this.ticketUrl = ticketUrl;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Feed getFeed() {
    return feed;
  }

  public void setFeed(Feed feed) {
    this.feed = feed;
  }

  public Boolean getPublished() {
    return published;
  }

  public void setPublished(Boolean published) {
    this.published = published;
  }

  public Boolean getClosed() {
    return closed;
  }

  public void setClosed(Boolean closed) {
    this.closed = closed;
  }

  public List<Feed> getTargets() {
    return targets;
  }

  public void setTargets(List<Feed> targets) {
    this.targets = targets;
  }
}
