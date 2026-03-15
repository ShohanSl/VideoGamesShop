package com.example.videogamesshop.cache;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class GameQueryKey {
    private final List<Long> categoryIds;
    private final int page;
    private final int size;
    private final String sort;

    public GameQueryKey(List<Long> categoryIds, Pageable pageable) {
        this.categoryIds = categoryIds != null
                ? categoryIds.stream().sorted().toList()
                : null;
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
                && Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryIds, page, size, sort);
    }
}