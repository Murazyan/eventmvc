package com.example.eventmvc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "event")
public   class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private User createrUser;
    @Column(name = "creater_visibility")
    private boolean createrVisibility;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "other_info")
    private String otherInfo;
    @Column(name = "event_access_type")
    private boolean eventAccessType;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "update_date")
    private String updateDate;
    @Column(name = "pic_url")
    private String picUrl;
    @Column(name = "occur_date")
    private String occurDate;
    @Column(name = "occur_address")
    private String occurAddress;
    @Column(name = "max_count_person")
    private int maxCountPerson;
    @Column(name = "current_count_person")
    private int currentCountPerson;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private EventStatusC eventStatus;
    @Column(name = "preferences_themes")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_theme",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id"))
    private List<PreferencesThemes> preferencesThemes;
    @Override
    public String toString() {
        return "Event{" +
                "createrUser=" + createrUser +
                ", createrVisibility=" + createrVisibility +
                ", eventName='" + eventName + '\'' +
                ", otherInfo='" + otherInfo + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", eventStatus=" + eventStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id &&
                Objects.equals(picUrl, event.picUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, picUrl);
    }
}
