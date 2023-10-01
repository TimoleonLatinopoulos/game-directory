package com.timoleon.gamedirectory.service.dto.steam;

import com.timoleon.gamedirectory.domain.Category;
import java.util.List;

public class SteamGameDetailsDTO {

    private String type;
    private String name;
    private Long steam_appid;
    private Integer required_age;
    private Boolean is_free;
    private String controller_support;
    private String detailed_description;
    private String about_the_game;
    private String short_description;
    private String supported_languages;
    private String reviews;
    private String header_image;
    private String capsule_image;
    private String capsule_imagev5;
    private String website;
    private List<String> developers;
    private List<String> publishers;
    private List<Category> categories;
    private List<Category> genres;
    private ReleaseDateDTO release_date;
    private MetacriticScoreDTO metacritic;
    private PriceDTO price_overview;
    private ContentDescriptorsDTO content_descriptors;

    public SteamGameDetailsDTO() {}

    public SteamGameDetailsDTO(
        String type,
        String name,
        Long steam_appid,
        Integer required_age,
        Boolean is_free,
        String controller_support,
        String detailed_description,
        String about_the_game,
        String short_description,
        String supported_languages,
        String reviews,
        String header_image,
        String capsule_image,
        String capsule_imagev5,
        String website,
        List<String> developers,
        List<String> publishers,
        List<Category> categories,
        List<Category> genres,
        ReleaseDateDTO release_date,
        MetacriticScoreDTO metacritic,
        PriceDTO price_overview,
        ContentDescriptorsDTO content_descriptors
    ) {
        this.type = type;
        this.name = name;
        this.steam_appid = steam_appid;
        this.required_age = required_age;
        this.is_free = is_free;
        this.controller_support = controller_support;
        this.detailed_description = detailed_description;
        this.about_the_game = about_the_game;
        this.short_description = short_description;
        this.supported_languages = supported_languages;
        this.reviews = reviews;
        this.header_image = header_image;
        this.capsule_image = capsule_image;
        this.capsule_imagev5 = capsule_imagev5;
        this.website = website;
        this.developers = developers;
        this.publishers = publishers;
        this.categories = categories;
        this.genres = genres;
        this.release_date = release_date;
        this.metacritic = metacritic;
        this.price_overview = price_overview;
        this.content_descriptors = content_descriptors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSteam_appid() {
        return steam_appid;
    }

    public void setSteam_appid(Long steam_appid) {
        this.steam_appid = steam_appid;
    }

    public Integer getRequired_age() {
        return required_age;
    }

    public void setRequired_age(Integer required_age) {
        this.required_age = required_age;
    }

    public Boolean getIs_free() {
        return is_free;
    }

    public void setIs_free(Boolean is_free) {
        this.is_free = is_free;
    }

    public String getController_support() {
        return controller_support;
    }

    public void setController_support(String controller_support) {
        this.controller_support = controller_support;
    }

    public String getDetailed_description() {
        return detailed_description;
    }

    public void setDetailed_description(String detailed_description) {
        this.detailed_description = detailed_description;
    }

    public String getAbout_the_game() {
        return about_the_game;
    }

    public void setAbout_the_game(String about_the_game) {
        this.about_the_game = about_the_game;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getSupported_languages() {
        return supported_languages;
    }

    public void setSupported_languages(String supported_languages) {
        this.supported_languages = supported_languages;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

    public String getCapsule_image() {
        return capsule_image;
    }

    public void setCapsule_image(String capsule_image) {
        this.capsule_image = capsule_image;
    }

    public String getCapsule_imagev5() {
        return capsule_imagev5;
    }

    public void setCapsule_imagev5(String capsule_imagev5) {
        this.capsule_imagev5 = capsule_imagev5;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<String> developers) {
        this.developers = developers;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getGenres() {
        return genres;
    }

    public void setGenres(List<Category> genres) {
        this.genres = genres;
    }

    public ReleaseDateDTO getRelease_date() {
        return release_date;
    }

    public void setRelease_date(ReleaseDateDTO release_date) {
        this.release_date = release_date;
    }

    public MetacriticScoreDTO getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(MetacriticScoreDTO metacritic) {
        this.metacritic = metacritic;
    }

    public PriceDTO getPrice_overview() {
        return price_overview;
    }

    public void setPrice_overview(PriceDTO price_overview) {
        this.price_overview = price_overview;
    }

    public ContentDescriptorsDTO getContent_descriptors() {
        return content_descriptors;
    }

    public void setContent_descriptors(ContentDescriptorsDTO content_descriptors) {
        this.content_descriptors = content_descriptors;
    }
}
