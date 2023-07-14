package com.timoleon.gamedirectory.domain.search;

import java.io.Serializable;

public class SearchFilterItem implements Serializable {

    public static final String OPERATOR_STARTS_WITH = "startswith";
    public static final String OPERATOR_EQUAL_DATE_ONLY = "eqdateonly";
    public static final String OPERATOR_EQUAL_DATE_ONLY_LIKE = "eqdateonlylike";
    public static final String OPERATOR_EQUAL_BOOL = "eqbool";
    public static final String OPERATOR_NOT_EQUAL_BOOL = "noteqbool";
    public static final String OPERATOR_EQUALS = "eq";
    public static final String OPERATOR_EQUALS_CASE_INSENSITIVE = "eqCaseInsensitive";
    public static final String OPERATOR_NOT_EQUALS = "neq";
    public static final String OPERATOR_CONTAINS = "contains";
    public static final String OPERATOR_LIKE = "like";
    public static final String OPERATOR_EQUAL_DATE = "eqdate";
    public static final String OPERATOR_EQUAL_DATE_TIME = "eqdatetime";
    public static final String OPERATOR_EQUAL_CALENDAR = "eqcalendar";
    public static final String OPERATOR_FROM_DATE = "fromdate";
    public static final String OPERATOR_TO_DATE = "todate";
    public static final String OPERATOR_FROM_DATETIME = "fromdatetime";
    public static final String OPERATOR_TO_DATETIME = "todatetime";
    public static final String OPERATOR_IN = "in";
    public static final String OPERATOR_IN_STR = "inStr";
    //
    //    public static final String OPERATOR_LESS_THAN_OR_EQUALS = "ltoreq";
    //    public static final String OPERATOR_GREATER_THAN_OR_EQUALS = "gtoreq";

    public static final String OPERATOR_STARTS_WITH_NUMBER = "startswithnum";
    public static final String OPERATOR_IS_NULL = "isnull";
    public static final String OPERATOR_IS_NOT_NULL = "isnotnull";
    public static final String OPERATOR_GREATER_THAN = "greaterThan";
    public static final String OPERATOR_GREATER_THAN_OR_EQUAL_TO = "greaterThanOrEqualTo";
    public static final String OPERATOR_LESS_THAN_OR_EQUAL_TO = "lessThanOrEqualTo";
    public static final String OPERATOR_MASTER_FILTER = "masterFilter";
    public static final String OPERATOR_FROM = "from";
    public static final String OPERATOR_TO = "to";
    public static final String OPERATOR_FROM_INSTANT = "fromInstant";
    public static final String OPERATOR_TO_INSTANT = "toInstant";
    public static final String OPERATOR_ADDRESS_SEARCH = "addressSearch";
    public static final String OPERATOR_DATE_BETWEEN_FROM = "dateBetweenFrom";
    public static final String OPERATOR_DATE_BETWEEN_TO = "dateBetweenTo";
    public static final String OPERATOR_DATE_IS_OPEN = "dateIsOpen";
    public static final String OPERATOR_SUBQ_PATIENT = "subqPatient";
    public static final String OPERATOR_PATIENT_IS_CONFIRMED = "patientIsConfirmed";
    public static final String OPERATOR_PATIENT_IS_NOT_CONFIRMED = "patientIsNotConfirmed";

    private String field;

    private String operator;

    private String value;

    public SearchFilterItem() {}

    public SearchFilterItem(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
