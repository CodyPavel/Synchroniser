package com.pavell.rickAndMortyApi.specification;

import com.pavell.rickAndMortyApi.entity.Episode;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class EpisodeSpecification {
    public static Specification<Episode> findByCriteria(final SearchCriteriaEpisode searchCriteria) {
        return new Specification<Episode>() {
            @Override
            public Predicate toPredicate(Root<Episode> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (searchCriteria.getEpisode() != null && !searchCriteria.getEpisode().isEmpty()) {
                    predicates.add(cb.equal(root.get("episode"), searchCriteria.getEpisode()));
                }
                if (searchCriteria.getName() != null && !searchCriteria.getName().isEmpty()) {
                    predicates.add(cb.equal(root.get("name"), searchCriteria.getName()));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }
}
