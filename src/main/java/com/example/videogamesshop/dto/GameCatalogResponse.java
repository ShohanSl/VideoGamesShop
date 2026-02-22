package com.example.videogamesshop.dto;

public class GameCatalogResponse {
    private Long id;
    private String title;
    private String genre;
    private Double price;

    public GameCatalogResponse() {}

    public GameCatalogResponse(Long id, String title, String genre, Double price) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.price = price;
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
}
