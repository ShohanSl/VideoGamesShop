package com.example.videogamesshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private Double price;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Developer developer;

    @ManyToMany
    @JoinTable(
            name = "game_category",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Category> categories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Publisher publisher;

    @ManyToMany(mappedBy = "games")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> libraries = new HashSet<>();
}