package io.mountblue.blogapplication.specification;

import io.mountblue.blogapplication.entity.Post;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta  .persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class PostSpecification {
    public static Specification<Post> postHasAuthor(List<String> authors) {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return (authors == null || authors.isEmpty()) ? cb.disjunction() : root.get("author").in(authors);
            }
        };
    }

    public static Specification<Post> postHasTag(List<String> tags) {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                var tagsJoin = root.join("tags");

                Predicate returnvalue = (tags == null || tags.isEmpty()) ? cb.disjunction() : tagsJoin.get("name").in(tags);
                return returnvalue;
            }
        };
    }

    public static Specification<Post> postHasDate(LocalDate date) {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);

                return cb.between(root.get("createdAt"), startOfDay, endOfDay);
            }
        };
    }
}
