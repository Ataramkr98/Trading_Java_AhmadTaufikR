package com.ahmadtrportofolio.pulse.academy;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "academy_courses")
public class Course {
    @Id
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "lessons_count", nullable = false)
    private int lessonsCount;

    @Column(name = "duration", nullable = false)
    private String duration;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public int getLessonsCount() { return lessonsCount; }
    public void setLessonsCount(int lessonsCount) { this.lessonsCount = lessonsCount; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
