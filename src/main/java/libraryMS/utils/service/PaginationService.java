package libraryMS.utils.service;

import libraryMS.utils.model.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PaginationService {

    public Pageable getPagination(Pagination pagination) {
        try {
            if (pagination == null) {
                return Pageable.unpaged();
            }

            Sort.Direction sortType = pagination.getOrderType() != null && pagination.getOrderType().equalsIgnoreCase("ASC")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            String[] orderBy = pagination.getOrderBy() != null ? pagination.getOrderBy().split(",") : new String[]{"id"};

            int start = pagination.getStart() != null ? pagination.getStart() : 0; // Default to first page if start is null
            int size = pagination.getSize() != null ? pagination.getSize() : Integer.MAX_VALUE; // Fetch all data if size is null

            if (size == Integer.MAX_VALUE) {
                return Pageable.unpaged();
            }

            return PageRequest.of(start, size, Sort.by(sortType, orderBy));

        } catch (Exception e) {
            e.printStackTrace();
            return Pageable.unpaged();
        }
    }
}
