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
            Pageable pageable = null;
            if (pagination != null) {

                Sort.Direction sortType = pagination.getOrderType() != null ?
                        pagination.getOrderType().equalsIgnoreCase("DESC") ?
                                Sort.Direction.DESC :
                                Sort.Direction.ASC : Sort.Direction.DESC;
                pageable = PageRequest.of(pagination.getStart(),
                        pagination.getSize(), Sort.by(sortType,
                                pagination.getOrderBy() != null ?
                                        pagination.getOrderBy().split(",") :
                                        new String[]{"id"}));
            }
            return pageable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
