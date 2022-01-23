package com.pavell.rickAndMortyApi.specification;

import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class LocationSpecification {
    public static Specification<Location> findByCriteria(final SearchCriteriaLocation searchCriteria) {
        return new Specification<Location>() {
            @Override
            public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (searchCriteria.getDimension() != null && !searchCriteria.getDimension().isEmpty()) {
                    predicates.add(cb.equal(root.get("dimension"), searchCriteria.getDimension()));
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
