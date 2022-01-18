package com.pavell.rickAndMortyApi.specification;

import com.pavell.rickAndMortyApi.entity.Episode;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class EpisodeSpecification implements Specification<Episode> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Episode> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
    }

}
