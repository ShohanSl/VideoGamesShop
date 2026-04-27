package com.example.videogamesshop.cache;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class GameQueryKey {
    private final List<Long> categoryIds;
    private final List<Long> excludedGameIds;
    private final Long publisherId;
    private final String title;
    private final int page;
    private final int size;
    private final String sort;

    public GameQueryKey(List<Long> categoryIds,
                        List<Long> excludedGameIds,
                        Long publisherId,
                        String title,
                        Pageable pageable) {
        this.categoryIds = categoryIds != null
                ? categoryIds.stream().sorted().toList()
                : null;
        this.excludedGameIds = excludedGameIds != null
                ? excludedGameIds.stream().sorted().toList()
                : null;
        this.publisherId = publisherId;
        this.title = title != null && !title.isBlank() ? title.trim().toLowerCase() : null;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.sort = pageable.getSort().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameQueryKey that = (GameQueryKey) o;
        return page == that.page
                && size == that.size
                && Objects.equals(categoryIds, that.categoryIds)
                && Objects.equals(excludedGameIds, that.excludedGameIds)
                && Objects.equals(publisherId, that.publisherId)
                && Objects.equals(title, that.title)
                && Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryIds, excludedGameIds, publisherId, title, page, size, sort);
    }
}
