package com.pavell.rickAndMortyApi.specification;

import com.pavell.rickAndMortyApi.entity.Character;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CharacterSpecification {
    public static Specification<Character> findByCriteria(final SearchCriteriaCharacter searchCriteria) {
        return new Specification<Character>() {
            @Override
            public Predicate toPredicate(Root<Character> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (searchCriteria.getGender() != null) {
                    predicates.add(cb.equal(root.get("gender"), searchCriteria.getGender()));
                }
                if (searchCriteria.getStatus() != null  ) {
                    predicates.add(cb.equal(root.get("status"), searchCriteria.getStatus()));
                }
                if (searchCriteria.getSpecies() != null && !searchCriteria.getSpecies().isEmpty()) {
                    predicates.add(cb.equal(root.get("species"), searchCriteria.getSpecies()));
                }
                if (searchCriteria.getName() != null && !searchCriteria.getName().isEmpty()) {
                    predicates.add(cb.equal(root.get("name"), searchCriteria.getName()));
                }
                if (searchCriteria.getType() != null && !searchCriteria.getType().isEmpty()) {
                    predicates.add(cb.equal(root.get("type"), searchCriteria.getType()));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };


    }


}
