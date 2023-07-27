package com.timoleon.gamedirectory.repository.specifications;

import com.google.common.collect.Lists;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.domain.search.SearchFilterItem;
import com.timoleon.gamedirectory.util.CustomUtil;
import com.timoleon.gamedirectory.util.DateUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

public abstract class AbstractApiSpecification<T extends Object> {

    private String lastModifiedDate;

    private static final Map<Character, Character> LATIN_TO_GREEK = new HashMap<>();

    static {
        LATIN_TO_GREEK.put('a', 'α');
        LATIN_TO_GREEK.put('b', 'β');
        LATIN_TO_GREEK.put('g', 'γ');
        LATIN_TO_GREEK.put('d', 'δ');
        LATIN_TO_GREEK.put('e', 'ε');
        LATIN_TO_GREEK.put('z', 'ζ');
        LATIN_TO_GREEK.put('h', 'η');
        LATIN_TO_GREEK.put('u', 'θ');
        LATIN_TO_GREEK.put('i', 'ι');
        LATIN_TO_GREEK.put('k', 'κ');
        LATIN_TO_GREEK.put('l', 'λ');
        LATIN_TO_GREEK.put('m', 'μ');
        LATIN_TO_GREEK.put('n', 'ν');
        LATIN_TO_GREEK.put('j', 'ξ');
        LATIN_TO_GREEK.put('o', 'ο');
        LATIN_TO_GREEK.put('p', 'π');
        LATIN_TO_GREEK.put('r', 'ρ');
        LATIN_TO_GREEK.put('s', 'σ');
        LATIN_TO_GREEK.put('t', 'τ');
        LATIN_TO_GREEK.put('y', 'υ');
        LATIN_TO_GREEK.put('f', 'φ');
        LATIN_TO_GREEK.put('x', 'χ');
        LATIN_TO_GREEK.put('c', 'ψ');
        LATIN_TO_GREEK.put('v', 'ω');
    }

    private static String getGreekString(String filter) {
        char[] chars = filter.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Character charToReplace = LATIN_TO_GREEK.get(chars[i]);
            if (charToReplace != null) chars[i] = charToReplace;
        }
        return new String(chars);
    }

    public Page<T> search(
        SearchCriteria criteria,
        Pageable pageable,
        EntityManager entityManager,
        Class<T> clazz,
        EntityGraph entityGraph,
        List<String> fetchAssocs
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Query to get all the ids for this search based on the criteria provided
        CriteriaQuery<Long> queryForIds = builder.createQuery(Long.class);
        Root<T> rootForIds = queryForIds.from(clazz);

        List<Predicate> criteriaList = getPredicatesFromCriteria(rootForIds, builder, queryForIds, criteria);
        List<Order> orderList = getOrderFromCriteria(rootForIds, builder, criteria);
        queryForIds
            .where(builder.and(criteriaList.toArray(new Predicate[0])))
            .select(rootForIds.get("id"))
            .distinct(false)
            .orderBy(orderList);

        List<Long> ids = entityManager
            .createQuery(queryForIds)
            .setFirstResult((pageable.getPageNumber()) * pageable.getPageSize())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        if (ids == null || ids.isEmpty()) {
            return Page.empty();
        }

        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        if (fetchAssocs != null && !fetchAssocs.isEmpty()) {
            fetchAssocs.forEach(f -> root.fetch(f, JoinType.LEFT));
        }

        orderList = getOrderFromCriteria(root, builder, criteria);
        List<T> resultList = new ArrayList<>();
        TypedQuery tp;
        query.select(root).orderBy(orderList);
        if (ids.size() > 999) {
            List<List<Long>> idPartitionsList = Lists.partition(ids, 999);
            for (List<Long> idList : idPartitionsList) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("id"));
                idList.forEach(inClause::value);
                query.where(builder.or(inClause));
                if (entityGraph != null) {
                    tp = entityManager.createQuery(query).setHint("javax.persistence.fetchgraph", entityGraph);
                } else {
                    tp = entityManager.createQuery(query);
                }

                resultList.addAll(tp.getResultList());
            }
        } else {
            CriteriaBuilder.In<Long> inClause = builder.in(root.get("id"));
            ids.forEach(inClause::value);
            query.where(inClause);
            if (entityGraph != null) {
                tp = entityManager.createQuery(query).setHint("javax.persistence.fetchgraph", entityGraph);
            } else {
                tp = entityManager.createQuery(query);
            }
            resultList = tp.getResultList();
        }

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(clazz);

        List<Predicate> countPredicates = getPredicatesFromCriteria(countRoot, builder, countQuery, criteria);

        countQuery.where(countPredicates.toArray(new Predicate[countPredicates.size()])).select(builder.countDistinct(countRoot));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(resultList, pageable, count);
    }

    public List<T> getAll(EntityManager entityManager, Class<T> clazz, EntityGraph entityGraph, List<String> fetchAssocs) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        if (fetchAssocs != null && !fetchAssocs.isEmpty()) {
            fetchAssocs.forEach(f -> root.fetch(f, JoinType.LEFT));
        }

        query.select(root);

        TypedQuery tp;
        if (entityGraph != null) {
            tp = entityManager.createQuery(query).setHint("javax.persistence.fetchgraph", entityGraph);
        } else {
            tp = entityManager.createQuery(query);
        }

        List<T> resultList = tp.getResultList();

        return resultList;
    }

    public List<T> search(SearchCriteria criteria, EntityManager entityManager, Class<T> clazz, String selectField) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        List<Predicate> criteriaList = getPredicatesFromCriteria(root, builder, query, criteria);

        if (selectField.contains(".")) {
            List<String> selectFields = Arrays.asList(selectField.split("\\."));
            switch (selectFields.size()) {
                case 2:
                    query
                        .where(builder.and(criteriaList.toArray(new Predicate[0])))
                        .select(root.join(selectFields.get(0), JoinType.LEFT).join(selectFields.get(1), JoinType.LEFT))
                        .distinct(true);
                    break;
            }
        } else {
            query.where(builder.and(criteriaList.toArray(new Predicate[0]))).select(root.join(selectField)).distinct(true);
        }

        TypedQuery tp = entityManager.createQuery(query);
        return tp.getResultList();
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    protected List<Predicate> getPredicatesFromCriteria(
        Root<?> root,
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<?> query,
        SearchCriteria searchCriteria
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(lastModifiedDate)) {
            ZonedDateTime temp = ZonedDateTime.parse(lastModifiedDate);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("lastModifiedDate"), temp));
        }

        // Set the filters
        if (searchCriteria.getFilter() != null) {
            searchCriteria
                .getFilter()
                .getFilters()
                .forEach(filter -> {
                    filter.setValue(CustomUtil.stripAccents(filter.getValue()));
                    if (filter.getField().contains(".")) {
                        List<String> fields = Arrays.asList(filter.getField().split("\\."));
                        switch (fields.size()) {
                            case 2:
                                if (filter.getOperator().equals(SearchFilterItem.OPERATOR_EQUAL_DATE_ONLY)) {
                                    predicates.add(
                                        criteriaBuilder.equal(
                                            root.join(fields.get(0)).<LocalDate>get(fields.get(1)),
                                            LocalDate.parse(filter.getValue())
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_IS_NULL)) {
                                    predicates.add(criteriaBuilder.isNull(root.join(fields.get(0), JoinType.LEFT)));
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_EQUAL_CALENDAR)) {
                                    predicates.add(
                                        criteriaBuilder.equal(
                                            root.join(fields.get(0)).<Calendar>get(fields.get(1)),
                                            DateUtil.stringToCalendar(filter.getValue(), new SimpleDateFormat(DateUtil.DATE_PATTERN_1))
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_NOT_EQUALS)) {
                                    predicates.add(
                                        criteriaBuilder.notEqual(root.join(fields.get(0)).get(fields.get(1)), filter.getValue())
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_EQUAL_BOOL)) {
                                    predicates.add(
                                        criteriaBuilder.equal(
                                            root.join(fields.get(0)).<Boolean>get(fields.get(1)),
                                            Boolean.valueOf(filter.getValue())
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_STARTS_WITH)) {
                                    predicates.add(
                                        criteriaBuilder.or(
                                            criteriaBuilder.like(
                                                criteriaBuilder.upper(root.join(fields.get(0)).get(fields.get(1)).as(String.class)),
                                                filter.getValue().toUpperCase() + "%"
                                            ),
                                            criteriaBuilder.like(
                                                criteriaBuilder.upper(root.join(fields.get(0)).get(fields.get(1)).as(String.class)),
                                                getGreekString(filter.getValue().toUpperCase()) + "%"
                                            )
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_EQUALS)) {
                                    predicates.add(criteriaBuilder.equal(root.join(fields.get(0)).get(fields.get(1)), filter.getValue()));
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_EQUALS_CASE_INSENSITIVE)) {
                                    predicates.add(
                                        criteriaBuilder.equal(
                                            criteriaBuilder.upper(root.join(fields.get(0)).get(fields.get(1))),
                                            filter.getValue().toUpperCase()
                                        )
                                    );
                                } else if (
                                    filter.getOperator().equals(SearchFilterItem.OPERATOR_CONTAINS) ||
                                    filter.getOperator().equals(SearchFilterItem.OPERATOR_LIKE)
                                ) {
                                    predicates.add(
                                        criteriaBuilder.like(
                                            criteriaBuilder.upper(root.join(fields.get(0)).get(fields.get(1)).as(String.class)),
                                            "%" + CustomUtil.stripAccents(filter.getValue()).toUpperCase() + "%"
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_EQUAL_DATE)) {
                                    predicates.add(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                            root.join(fields.get(0)).<ZonedDateTime>get(fields.get(1)),
                                            ZonedDateTime.parse(filter.getValue()).withHour(0).withMinute(0).withSecond(0)
                                        )
                                    );
                                    predicates.add(
                                        criteriaBuilder.lessThanOrEqualTo(
                                            root.join(fields.get(0)).<ZonedDateTime>get(fields.get(1)),
                                            ZonedDateTime.parse(filter.getValue()).withHour(23).withMinute(59).withSecond(59)
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_EQUAL_DATE_TIME)) {
                                    predicates.add(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                            root.join(fields.get(0)).<LocalDateTime>get(fields.get(1)),
                                            LocalDateTime.parse(filter.getValue()).withHour(0).withMinute(0).withSecond(0)
                                        )
                                    );
                                    predicates.add(
                                        criteriaBuilder.lessThanOrEqualTo(
                                            root.join(fields.get(0)).<LocalDateTime>get(fields.get(1)),
                                            LocalDateTime.parse(filter.getValue()).withHour(23).withMinute(59).withSecond(59)
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_FROM_DATE)) {
                                    predicates.add(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                            root.join(fields.get(0)).<LocalDate>get(fields.get(1)),
                                            LocalDateTime
                                                .parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                                                .atOffset(ZoneOffset.UTC)
                                                .atZoneSameInstant(ZoneId.of("Europe/Athens"))
                                                .toLocalDate()
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_TO_DATE)) {
                                    predicates.add(
                                        criteriaBuilder.lessThanOrEqualTo(
                                            root.join(fields.get(0)).<LocalDate>get(fields.get(1)),
                                            LocalDateTime
                                                .parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                                                .atOffset(ZoneOffset.UTC)
                                                .atZoneSameInstant(ZoneId.of("Europe/Athens"))
                                                .toLocalDate()
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_IN) && !filter.getValue().equals("")) {
                                    Object[] ids = filter.getValue().split("[^-\\d]+");

                                    predicates.add(root.join(fields.get(0)).get(fields.get(1)).in(ids));
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_IN_STR) && !filter.getValue().equals("")) {
                                    Object[] ids = filter.getValue().split("[^-\\α-ω-Α-ωa-zA-z0-9]");

                                    predicates.add(root.join(fields.get(0)).get(fields.get(1)).in(ids));
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_STARTS_WITH_NUMBER)) {
                                    predicates.add(
                                        criteriaBuilder.like(
                                            criteriaBuilder.upper(root.join(fields.get(0)).get(fields.get(1)).as(String.class)),
                                            filter.getValue().toUpperCase() + "%"
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_IS_NOT_NULL)) {
                                    predicates.add(criteriaBuilder.isNotNull(root.join(fields.get(0), JoinType.LEFT).get(fields.get(1))));
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_IS_NULL)) {
                                    predicates.add(criteriaBuilder.isNull(root.join(fields.get(0), JoinType.LEFT).get(fields.get(1))));
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_FROM_INSTANT)) {
                                    predicates.add(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                            root.join(fields.get(0)).get(fields.get(1)),
                                            Instant
                                                .parse(filter.getValue())
                                                .atZone(ZoneId.of("Europe/Athens"))
                                                .withHour(0)
                                                .withMinute(0)
                                                .withSecond(0)
                                                .toInstant()
                                        )
                                    );
                                } else if (filter.getOperator().equals(SearchFilterItem.OPERATOR_TO_INSTANT)) {
                                    predicates.add(
                                        criteriaBuilder.lessThanOrEqualTo(
                                            root.join(fields.get(0)).get(fields.get(1)),
                                            Instant
                                                .parse(filter.getValue())
                                                .atZone(ZoneId.of("Europe/Athens"))
                                                .withHour(23)
                                                .withMinute(59)
                                                .withSecond(59)
                                                .toInstant()
                                        )
                                    );
                                }
                                break;
                            case 3:
                                switch (filter.getOperator()) {
                                    case SearchFilterItem.OPERATOR_STARTS_WITH:
                                        predicates.add(
                                            criteriaBuilder.or(
                                                criteriaBuilder.like(
                                                    criteriaBuilder.upper(
                                                        root.join(fields.get(0)).join(fields.get(1)).get(fields.get(2)).as(String.class)
                                                    ),
                                                    filter.getValue().toUpperCase() + "%"
                                                ),
                                                criteriaBuilder.like(
                                                    criteriaBuilder.upper(
                                                        root.join(fields.get(0)).join(fields.get(1)).get(fields.get(2)).as(String.class)
                                                    ),
                                                    getGreekString(filter.getValue().toUpperCase()) + "%"
                                                )
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_IN:
                                        Object[] ids = filter.getValue().split("[^-\\d]+");

                                        predicates.add(root.join(fields.get(0)).get(fields.get(1)).get(fields.get(2)).in(ids));
                                        break;
                                    case SearchFilterItem.OPERATOR_IN_STR:
                                        Object[] strs = filter.getValue().split("[^-\\α-ω-Α-ωa-zA-z0-9]");

                                        predicates.add(root.join(fields.get(0)).get(fields.get(1)).get(fields.get(2)).in(strs));
                                        break;
                                    case SearchFilterItem.OPERATOR_EQUALS:
                                        if (fields.get(1).equals("authorities")) {
                                            predicates.add(
                                                criteriaBuilder.equal(
                                                    root.join(fields.get(0)).join(fields.get(1)).get(fields.get(2)),
                                                    filter.getValue()
                                                )
                                            );
                                        } else {
                                            predicates.add(
                                                criteriaBuilder.equal(
                                                    root.join(fields.get(0)).join(fields.get(1)).get(fields.get(2)),
                                                    filter.getValue()
                                                )
                                            );
                                        }
                                        break;
                                    case SearchFilterItem.OPERATOR_NOT_EQUAL_BOOL:
                                        predicates.add(
                                            criteriaBuilder.notEqual(
                                                root.join(fields.get(0)).join(fields.get(1)).get(fields.get(2)),
                                                Boolean.valueOf(filter.getValue())
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_EQUAL_BOOL:
                                        predicates.add(
                                            criteriaBuilder.equal(
                                                root.join(fields.get(0)).join(fields.get(1)).<Boolean>get(fields.get(2)),
                                                Boolean.valueOf(filter.getValue())
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_CONTAINS:
                                    case SearchFilterItem.OPERATOR_LIKE:
                                        predicates.add(
                                            criteriaBuilder.like(
                                                criteriaBuilder.upper(
                                                    root.join(fields.get(0)).join(fields.get(1)).get(fields.get(2)).as(String.class)
                                                ),
                                                "%" + CustomUtil.stripAccents(filter.getValue()).toUpperCase() + "%"
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_EQUAL_DATE:
                                        predicates.add(
                                            criteriaBuilder.greaterThanOrEqualTo(
                                                root.join(fields.get(0)).join(fields.get(1)).<ZonedDateTime>get(fields.get(2)),
                                                ZonedDateTime.parse(filter.getValue()).withHour(0).withMinute(0).withSecond(0)
                                            )
                                        );
                                        predicates.add(
                                            criteriaBuilder.lessThanOrEqualTo(
                                                root.join(fields.get(0)).join(fields.get(1)).<ZonedDateTime>get(fields.get(2)),
                                                ZonedDateTime.parse(filter.getValue()).withHour(23).withMinute(59).withSecond(59)
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_IS_NULL:
                                        if (filter.getValue().equals("false")) {
                                            predicates.add(
                                                criteriaBuilder.isNotNull(
                                                    root
                                                        .join(fields.get(0), JoinType.LEFT)
                                                        .join(fields.get(1), JoinType.LEFT)
                                                        .join(fields.get(2), JoinType.LEFT)
                                                )
                                            );
                                        } else {
                                            predicates.add(
                                                criteriaBuilder.isNull(
                                                    root
                                                        .join(fields.get(0), JoinType.LEFT)
                                                        .join(fields.get(1), JoinType.LEFT)
                                                        .join(fields.get(2), JoinType.LEFT)
                                                )
                                            );
                                        }
                                        break;
                                }
                                break;
                            case 4:
                                switch (filter.getOperator()) {
                                    case SearchFilterItem.OPERATOR_EQUALS:
                                        predicates.add(
                                            criteriaBuilder.equal(
                                                root.join(fields.get(0)).join(fields.get(1)).join(fields.get(2)).get(fields.get(3)),
                                                filter.getValue()
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_STARTS_WITH:
                                        predicates.add(
                                            criteriaBuilder.or(
                                                criteriaBuilder.like(
                                                    criteriaBuilder.upper(
                                                        root
                                                            .join(fields.get(0))
                                                            .join(fields.get(1))
                                                            .join(fields.get(2))
                                                            .get(fields.get(3))
                                                            .as(String.class)
                                                    ),
                                                    filter.getValue().toUpperCase() + "%"
                                                ),
                                                criteriaBuilder.like(
                                                    criteriaBuilder.upper(
                                                        root
                                                            .join(fields.get(0))
                                                            .join(fields.get(1))
                                                            .join(fields.get(2))
                                                            .get(fields.get(3))
                                                            .as(String.class)
                                                    ),
                                                    getGreekString(filter.getValue().toUpperCase()) + "%"
                                                )
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_CONTAINS:
                                    case SearchFilterItem.OPERATOR_LIKE:
                                        predicates.add(
                                            criteriaBuilder.like(
                                                criteriaBuilder.upper(
                                                    root
                                                        .join(fields.get(0))
                                                        .join(fields.get(1))
                                                        .join(fields.get(2))
                                                        .get(fields.get(3))
                                                        .as(String.class)
                                                ),
                                                "%" + CustomUtil.stripAccents(filter.getValue()).toUpperCase() + "%"
                                            )
                                        );
                                        break;
                                    case SearchFilterItem.OPERATOR_EQUAL_DATE:
                                        predicates.add(
                                            criteriaBuilder.greaterThanOrEqualTo(
                                                root
                                                    .join(fields.get(0))
                                                    .join(fields.get(1))
                                                    .join(fields.get(2))
                                                    .<ZonedDateTime>get(fields.get(3)),
                                                ZonedDateTime.parse(filter.getValue()).withHour(0).withMinute(0).withSecond(0)
                                            )
                                        );
                                        predicates.add(
                                            criteriaBuilder.lessThanOrEqualTo(
                                                root
                                                    .join(fields.get(0))
                                                    .join(fields.get(1))
                                                    .join(fields.get(2))
                                                    .<ZonedDateTime>get(fields.get(3)),
                                                ZonedDateTime.parse(filter.getValue()).withHour(23).withMinute(59).withSecond(59)
                                            )
                                        );
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (filter.getOperator()) {
                            case SearchFilterItem.OPERATOR_STARTS_WITH:
                                predicates.add(
                                    criteriaBuilder.or(
                                        criteriaBuilder.like(
                                            criteriaBuilder.upper(root.get(filter.getField())),
                                            filter.getValue().toUpperCase() + "%"
                                        ),
                                        criteriaBuilder.like(
                                            criteriaBuilder.upper(root.get(filter.getField())),
                                            getGreekString(filter.getValue().toUpperCase()) + "%"
                                        )
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_STARTS_WITH_NUMBER:
                                predicates.add(
                                    criteriaBuilder.like(
                                        criteriaBuilder.upper(root.get(filter.getField()).as(String.class)),
                                        filter.getValue().toUpperCase() + "%"
                                    )
                                );

                                break;
                            case SearchFilterItem.OPERATOR_IN:
                                Object[] ids = filter.getValue().split("[^-\\d]+");

                                predicates.add(root.get(filter.getField()).in(ids));

                                break;
                            case SearchFilterItem.OPERATOR_IN_STR:
                                Object[] strs = filter.getValue().split("[^-\\α-ω-Α-ωa-zA-z0-9]");

                                predicates.add(root.get(filter.getField()).in(strs));

                                break;
                            case SearchFilterItem.OPERATOR_CONTAINS:
                            case SearchFilterItem.OPERATOR_LIKE:
                                predicates.add(
                                    criteriaBuilder.like(
                                        criteriaBuilder.upper(root.get(filter.getField())),
                                        "%" + CustomUtil.stripAccents(filter.getValue()).toUpperCase() + "%"
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_EQUALS:
                                predicates.add(criteriaBuilder.equal(root.get(filter.getField()), filter.getValue()));
                                break;
                            case SearchFilterItem.OPERATOR_NOT_EQUALS:
                                predicates.add(criteriaBuilder.notEqual(root.get(filter.getField()), filter.getValue()));
                                break;
                            case SearchFilterItem.OPERATOR_EQUAL_DATE:
                                predicates.add(criteriaBuilder.equal(root.get(filter.getField()), LocalDate.parse(filter.getValue())));
                                break;
                            case SearchFilterItem.OPERATOR_EQUAL_DATE_TIME:
                                predicates.add(
                                    criteriaBuilder.greaterThanOrEqualTo(
                                        root.get(filter.getField()),
                                        LocalDateTime
                                            .parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                                            .withHour(0)
                                            .withMinute(0)
                                            .withSecond(0)
                                    )
                                );
                                predicates.add(
                                    criteriaBuilder.lessThanOrEqualTo(
                                        root.get(filter.getField()),
                                        LocalDateTime
                                            .parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                                            .withHour(23)
                                            .withMinute(59)
                                            .withSecond(59)
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_EQUAL_CALENDAR:
                                predicates.add(
                                    criteriaBuilder.equal(
                                        root.get(filter.getField()),
                                        DateUtil.stringToCalendar(filter.getValue(), new SimpleDateFormat(DateUtil.DATE_PATTERN_1))
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_EQUAL_DATE_ONLY:
                                predicates.add(
                                    criteriaBuilder.equal(root.<LocalDate>get(filter.getField()), LocalDate.parse(filter.getValue()))
                                );
                                break;
                            case SearchFilterItem.OPERATOR_EQUAL_DATE_ONLY_LIKE:
                                Expression<String> exp = criteriaBuilder.function(
                                    "DATE_FORMAT",
                                    String.class,
                                    root.<LocalDate>get("birthDate"),
                                    criteriaBuilder.literal("%d%m%Y")
                                );
                                predicates.add(criteriaBuilder.like(exp, filter.getValue() + "%"));
                                break;
                            case SearchFilterItem.OPERATOR_FROM_DATE:
                                predicates.add(
                                    criteriaBuilder.greaterThanOrEqualTo(
                                        root.get(filter.getField()),
                                        LocalDateTime
                                            .parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                                            .atOffset(ZoneOffset.UTC)
                                            .atZoneSameInstant(ZoneId.of("Europe/Athens"))
                                            .toLocalDate()
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_TO_DATE:
                                predicates.add(
                                    criteriaBuilder.lessThanOrEqualTo(
                                        root.get(filter.getField()),
                                        LocalDateTime
                                            .parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                                            .atOffset(ZoneOffset.UTC)
                                            .atZoneSameInstant(ZoneId.of("Europe/Athens"))
                                            .toLocalDate()
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_FROM_DATETIME:
                                predicates.add(
                                    criteriaBuilder.greaterThanOrEqualTo(
                                        root.get(filter.getField()),
                                        ZonedDateTime
                                            .parse(filter.getValue())
                                            .withZoneSameInstant(ZoneId.of("Europe/Athens"))
                                            .toLocalDateTime()
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_TO_DATETIME:
                                predicates.add(
                                    criteriaBuilder.lessThanOrEqualTo(
                                        root.get(filter.getField()),
                                        ZonedDateTime
                                            .parse(filter.getValue())
                                            .withZoneSameInstant(ZoneId.of("Europe/Athens"))
                                            .toLocalDateTime()
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_FROM:
                                predicates.add(
                                    criteriaBuilder.greaterThanOrEqualTo(
                                        root.get(filter.getField()),
                                        LocalDate.parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_TO:
                                predicates.add(
                                    criteriaBuilder.lessThanOrEqualTo(
                                        root.get(filter.getField()),
                                        LocalDate.parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_FROM_INSTANT:
                                predicates.add(
                                    criteriaBuilder.greaterThanOrEqualTo(
                                        root.get(filter.getField()),
                                        Instant
                                            .parse(filter.getValue())
                                            .atZone(ZoneId.of("Europe/Athens"))
                                            .withHour(0)
                                            .withMinute(0)
                                            .withSecond(0)
                                            .toInstant()
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_TO_INSTANT:
                                predicates.add(
                                    criteriaBuilder.lessThanOrEqualTo(
                                        root.get(filter.getField()),
                                        Instant
                                            .parse(filter.getValue())
                                            .atZone(ZoneId.of("Europe/Athens"))
                                            .withHour(23)
                                            .withMinute(59)
                                            .withSecond(59)
                                            .toInstant()
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_EQUAL_BOOL:
                                predicates.add(
                                    criteriaBuilder.equal(root.<Boolean>get(filter.getField()), Boolean.valueOf(filter.getValue()))
                                );
                                break;
                            case SearchFilterItem.OPERATOR_GREATER_THAN:
                                predicates.add(
                                    criteriaBuilder.greaterThan(root.get(filter.getField()), root.<Boolean>get(filter.getValue()))
                                );
                                break;
                            case SearchFilterItem.OPERATOR_GREATER_THAN_OR_EQUAL_TO:
                                predicates.add(
                                    criteriaBuilder.greaterThanOrEqualTo(
                                        root.get(filter.getField()).as(Float.class),
                                        Float.parseFloat(filter.getValue())
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_LESS_THAN_OR_EQUAL_TO:
                                predicates.add(
                                    criteriaBuilder.lessThanOrEqualTo(
                                        root.get(filter.getField()).as(Float.class),
                                        Float.parseFloat(filter.getValue())
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_IS_NULL:
                                if (filter.getValue().equals("false")) {
                                    predicates.add(criteriaBuilder.isNotNull(root.get(filter.getField())));
                                } else {
                                    predicates.add(criteriaBuilder.isNull(root.get(filter.getField())));
                                }
                                break;
                            case SearchFilterItem.OPERATOR_NOT_EQUAL_BOOL:
                                predicates.add(criteriaBuilder.notEqual(root.get(filter.getField()), Boolean.valueOf(filter.getValue())));
                                break;
                            case SearchFilterItem.OPERATOR_DATE_IS_OPEN:
                                predicates.add(
                                    criteriaBuilder.or(
                                        criteriaBuilder.isNull(root.get(filter.getField())),
                                        criteriaBuilder.lessThanOrEqualTo(
                                            root.get(filter.getField()),
                                            LocalDate.parse(filter.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                        )
                                    )
                                );
                                break;
                            case SearchFilterItem.OPERATOR_IS_NOT_NULL:
                                predicates.add(criteriaBuilder.isNotNull(root.get(filter.getField())));
                                break;
                        }
                    }
                });
        }
        return predicates;
    }

    private Expression<String> concat(CriteriaBuilder builder, String delimiter, Expression<String>... expressions) {
        Expression<String> result = null;
        for (int i = 0; i < expressions.length; i++) {
            final boolean first = i == 0, last = i == (expressions.length - 1);
            final Expression<String> expression = expressions[i];
            if (first && last) {
                result = expression;
            } else if (first) {
                result = builder.concat(expression, delimiter);
            } else {
                result = builder.concat(result, expression);
                if (!last) {
                    result = builder.concat(result, delimiter);
                }
            }
        }
        return result;
    }

    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    protected List<Order> getOrderFromCriteria(Root<?> root, CriteriaBuilder criteriaBuilder, SearchCriteria searchCriteria) {
        // Set the ordering
        List<Order> order = new ArrayList<>();
        if (searchCriteria.getSort() != null) {
            searchCriteria
                .getSort()
                .forEach(sort -> {
                    if (!StringUtils.isEmpty(sort.getDir()) && !StringUtils.isEmpty(sort.getField())) {
                        if (sort.getField().contains(".")) {
                            List<String> fields = Arrays.asList(sort.getField().split("\\."));
                            switch (fields.size()) {
                                case 2:
                                    if (sort.getDir().equals("desc")) {
                                        order.add(criteriaBuilder.desc(root.join(fields.get(0), JoinType.LEFT).get(fields.get(1))));
                                    } else if (sort.getDir().equals("asc")) {
                                        order.add(criteriaBuilder.asc(root.join(fields.get(0), JoinType.LEFT).get(fields.get(1))));
                                    }
                                    break;
                                case 3:
                                    if (sort.getDir().equals("desc")) {
                                        order.add(
                                            criteriaBuilder.desc(
                                                root
                                                    .join(fields.get(0), JoinType.LEFT)
                                                    .join(fields.get(1), JoinType.LEFT)
                                                    .get(fields.get(2))
                                            )
                                        );
                                    } else if (sort.getDir().equals("asc")) {
                                        order.add(
                                            criteriaBuilder.asc(
                                                root
                                                    .join(fields.get(0), JoinType.LEFT)
                                                    .join(fields.get(1), JoinType.LEFT)
                                                    .get(fields.get(2))
                                            )
                                        );
                                    }
                                    break;
                                case 4:
                                    if (sort.getDir().equals("desc")) {
                                        order.add(
                                            criteriaBuilder.desc(
                                                root
                                                    .join(fields.get(0), JoinType.LEFT)
                                                    .join(fields.get(1), JoinType.LEFT)
                                                    .join(fields.get(2), JoinType.LEFT)
                                                    .get(fields.get(3))
                                            )
                                        );
                                    } else if (sort.getDir().equals("asc")) {
                                        order.add(
                                            criteriaBuilder.asc(
                                                root
                                                    .join(fields.get(0), JoinType.LEFT)
                                                    .join(fields.get(1), JoinType.LEFT)
                                                    .join(fields.get(2), JoinType.LEFT)
                                                    .get(fields.get(3))
                                            )
                                        );
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            if (sort.getField().equals("dateTime") && root.getModel().getName().equals("Visit")) {
                                // ignore time
                                if (sort.getDir().equals("desc")) {
                                    order.add(
                                        criteriaBuilder.desc(criteriaBuilder.function("DATE", String.class, root.get(sort.getField())))
                                    );
                                } else if (sort.getDir().equals("asc")) {
                                    order.add(
                                        criteriaBuilder.asc(criteriaBuilder.function("DATE", String.class, root.get(sort.getField())))
                                    );
                                }
                            } else {
                                if (sort.getDir().equals("desc")) {
                                    order.add(criteriaBuilder.desc(root.get(sort.getField())));
                                } else if (sort.getDir().equals("asc")) {
                                    order.add(criteriaBuilder.asc(root.get(sort.getField())));
                                }
                            }
                        }
                    }
                });
            order.add(criteriaBuilder.desc(root.get("id")));
        }

        return order;
    }

    protected List<Expression> getGroupByFromCriteria(Root<?> root, SearchCriteria searchCriteria) {
        // Set the ordering
        List<Expression> groupBy = new ArrayList<>();
        if (searchCriteria.getSort() != null) {
            searchCriteria
                .getSort()
                .forEach(sort -> {
                    if (!StringUtils.isEmpty(sort.getDir()) && !StringUtils.isEmpty(sort.getField())) {
                        if (sort.getField().contains(".")) {
                            List<String> fields = Arrays.asList(sort.getField().split("\\."));
                            switch (fields.size()) {
                                case 2:
                                    if (sort.getDir().equals("desc")) {
                                        groupBy.add(root.join(fields.get(0), JoinType.LEFT).get(fields.get(1)));
                                    } else if (sort.getDir().equals("asc")) {
                                        groupBy.add(root.join(fields.get(0), JoinType.LEFT).get(fields.get(1)));
                                    }
                                    break;
                                case 3:
                                    if (sort.getDir().equals("desc")) {
                                        groupBy.add(
                                            root.join(fields.get(0), JoinType.LEFT).join(fields.get(1), JoinType.LEFT).get(fields.get(2))
                                        );
                                    } else if (sort.getDir().equals("asc")) {
                                        groupBy.add(
                                            root.join(fields.get(0), JoinType.LEFT).join(fields.get(1), JoinType.LEFT).get(fields.get(2))
                                        );
                                    }
                                    break;
                                case 4:
                                    if (sort.getDir().equals("desc")) {
                                        groupBy.add(
                                            root
                                                .join(fields.get(0), JoinType.LEFT)
                                                .join(fields.get(1), JoinType.LEFT)
                                                .join(fields.get(2), JoinType.LEFT)
                                                .get(fields.get(3))
                                        );
                                    } else if (sort.getDir().equals("asc")) {
                                        groupBy.add(
                                            root
                                                .join(fields.get(0), JoinType.LEFT)
                                                .join(fields.get(1), JoinType.LEFT)
                                                .join(fields.get(2), JoinType.LEFT)
                                                .get(fields.get(3))
                                        );
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            if (sort.getDir().equals("desc")) {
                                groupBy.add(root.get(sort.getField()));
                            } else if (sort.getDir().equals("asc")) {
                                groupBy.add(root.get(sort.getField()));
                            }
                        }
                    }
                });
            groupBy.add(root.get("id"));
        }

        return groupBy;
    }

    protected Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
