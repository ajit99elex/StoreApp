package com.retail.storeapp.repository;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestSpecificationCriteria implements Specification {

    private SearchCriteria criteria;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public RequestSpecificationCriteria(SearchCriteria criteria){
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        switch (criteria.getOperation().split(":")[0]){
            case "DATE" : {
                try {
                    switch(criteria.getOperation().split(":")[1]){
                        case ">=" : return criteriaBuilder.greaterThanOrEqualTo(
                                root.get(criteria.getKey()).as(Date.class), sdf.parse(criteria.getValue().toString()));
                        case "<=" : return criteriaBuilder.lessThanOrEqualTo(
                                root.get(criteria.getKey()).as(Date.class), sdf.parse(criteria.getValue().toString()));
                        case "=" : return criteriaBuilder.equal(
                                root.get(criteria.getKey()).as(Date.class), sdf.parse(criteria.getValue().toString()));

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            case "STRING" : {
                switch(criteria.getOperation().split(":")[1]){
                    case ">=" : return criteriaBuilder.greaterThanOrEqualTo(
                            root.get(criteria.getKey()), criteria.getValue().toString());
                    case "<=" : return criteriaBuilder.lessThanOrEqualTo(
                            root.get(criteria.getKey()), criteria.getValue().toString());
                    case "=LIKE" : return criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(criteria.getKey())), "%" + criteria.getValue() + "%");
                    case "=" : return criteriaBuilder.equal(
                            root.get(criteria.getKey()), criteria.getValue());
                }
            }

            case "INTEGER" : {
                switch(criteria.getOperation().split(":")[1]){
                    case "=" : return criteriaBuilder.equal(
                            root.get(criteria.getKey()), Integer.parseInt(criteria.getValue().toString()));
                }
            }

            case "BOOLEAN" : {
                switch(criteria.getOperation().split(":")[1]){
                    case "=TRUE" : return criteriaBuilder.isTrue(root.<Boolean>get(criteria.getKey()));
                    case "=FALSE" : return criteriaBuilder.isFalse(root.<Boolean>get(criteria.getKey()));
                }
            }
        }
        return null;
    }
}
