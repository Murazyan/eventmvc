package com.example.eventmvc.repository;



import com.example.eventmvc.model.PreferencesThemes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferencesThemesRepository extends JpaRepository<PreferencesThemes, Integer> {
    PreferencesThemes findAllByPreferencesThemes(String theams);
}
