package io.github.wendergalan.genericrestapi.builders;

import io.github.wendergalan.genericrestapi.models.beans.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: SpecificationBuilder.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * *********************************************
 *
 * @param <T> the type parameter
 */
public class SpecificationBuilder<T> {
    /**
     * The Key search.
     */
    protected final String KEY_SEARCH = "search";

    private final List<SearchCriteria> params;

    /**
     * Instantiates a new Specification builder.
     */
    public SpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    /**
     * With specification builder.
     *
     * @param key       the key
     * @param operation the operation
     * @param value     the value
     * @return the specification builder
     */
    public SpecificationBuilder with(String key, String operation, Object value) {
        if (key != null) {
            value = value.toString();
        }
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    /**
     * Build specification.
     *
     * @return the specification
     */
    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>();
        params.forEach(p -> specs.add(createSpecification(p)));

        Specification<T> result = specs.get(0);
        for (Specification<T> spec : specs) {
            result = Specification.where(result).and(spec);
        }
        return result;
    }

    /**
     * Clean params.
     */
    public void cleanParams() {
        params.clear();
    }

    /**
     * Create specification specification.
     *
     * @param searchCriteria the search criteria
     * @return the specification
     */
    protected Specification<T> createSpecification(SearchCriteria searchCriteria) {
        return new BasicSpecification(searchCriteria);
    }

    /**
     * The type Basic specification.
     */
    public class BasicSpecification implements Specification<T> {
        private SearchCriteria criteria;

        /**
         * Instantiates a new Basic specification.
         *
         * @param searchCriteria the search criteria
         */
        public BasicSpecification(SearchCriteria searchCriteria) {
            this.criteria = searchCriteria;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
            switch (criteria.getOperation()) {
                case "==":
                    if (root.get(criteria.getKey()).getJavaType() == String.class) {
                        return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                    } else {
                        return builder.equal(root.get(criteria.getKey()), criteria.getValue().toString());
                    }
                case "!=":
                    return builder.notEqual(root.get(criteria.getKey()), criteria.getValue().toString());
               case ">":
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                case "<":
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                case "IS_NULL":
                    return builder.isNull(root.get(criteria.getKey()));
                case "IS_NOT_NULL":
                    return builder.isNotNull(root.get(criteria.getKey()));
            }
            return null;
        }

        /**
         * Gets criteria.
         *
         * @return the criteria
         */
        public SearchCriteria getCriteria() {
            return criteria;
        }
    }
}
