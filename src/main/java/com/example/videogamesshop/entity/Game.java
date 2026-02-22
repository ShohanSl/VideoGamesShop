package com.example.videogamesshop.entity;

import java.time.LocalDate;

public class Game {
    private Long id;
    private String title;
    private String genre;
    private Double price;
    private LocalDate releaseDate;
    private String description;

    public Game() {
    }

    public Game(Long id, String title, String genre, Double price,
                LocalDate releaseDate, String description) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.releaseDate = releaseDate;
        this.description = description;
    }

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
