package com.timoleon.gamedirectory.web.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timoleon.gamedirectory.domain.search.SearchCriteria;
import com.timoleon.gamedirectory.domain.search.SearchFilter;
import com.timoleon.gamedirectory.domain.search.SearchSortItem;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;

/**
 * Created by Teo on 31/10/2018.
 */
public abstract class AbstractApiResource {

    private final Logger log = LoggerFactory.getLogger(AbstractApiResource.class);

    protected PageRequest extractPageRequestFromRequest(HttpServletRequest request) {
        if (
            request.getParameter("skip") != null &&
            isInteger(request.getParameter("skip")) &&
            request.getParameter("take") != null &&
            isInteger(request.getParameter("take"))
        ) {
            Integer skip = Integer.parseInt(request.getParameter("skip"));
            Integer take = Integer.parseInt(request.getParameter("take"));
            return PageRequest.of((skip), take);
        } else {
            return null;
        }
    }

    protected SearchCriteria extractSearchCriteriaFromRequest(HttpServletRequest request) {
        SearchCriteria criteria = new SearchCriteria();

        if (
            request.getParameter("skip") != null &&
            isInteger(request.getParameter("skip")) &&
            request.getParameter("take") != null &&
            isInteger(request.getParameter("take"))
        ) {
            Integer skip = Integer.parseInt(request.getParameter("skip"));
            Integer take = Integer.parseInt(request.getParameter("take"));
            criteria.setPage((skip));
            criteria.setPageSize(take);
        } else {
            // Set default values
            criteria.setPage(0);
            criteria.setPageSize(20);
        }

        if (request.getParameter("filter") != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                SearchFilter searchFilter = mapper.readValue(request.getParameter("filter"), SearchFilter.class);
                criteria.setFilter(searchFilter);
            } catch (Exception ex) {
                log.error("failed to map request params to SearchFilter!", ex);
            }
        }

        if (request.getParameter("sort") != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<SearchSortItem> searchSorts = mapper.readValue(
                    request.getParameter("sort"),
                    new TypeReference<List<SearchSortItem>>() {}
                );
                criteria.setSort(searchSorts);
            } catch (Exception ex) {
                log.error("failed to map request params to SearchFilter!", ex);
            }
        }

        return criteria;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
}
