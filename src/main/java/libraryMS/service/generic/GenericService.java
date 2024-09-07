package libraryMS.service.generic;

import libraryMS.dao.generic.GenericDao;
import libraryMS.domain.generic.GenericDomain;
import libraryMS.exception.exceptions.NotFoundException;
import libraryMS.utils.model.Pagination;
import libraryMS.utils.service.PaginationService;
import libraryMS.utils.service.RefService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.*;

public class GenericService<Dao extends GenericDao, Domain extends GenericDomain, IdClass extends Serializable> {

    @Autowired
    protected Dao dao;

    @Autowired
    private RefService refService;


    @Autowired
    private PaginationService paginationService;


    @Transactional
    public Iterable<Domain> insert(List<Domain> domain) {
        refService.linkChildrenWithParents(domain, new ArrayList<>());
        return ((JpaRepository<Domain, IdClass>) dao).saveAll(domain);
    }

    @Transactional
    public Domain merge(Domain domain) throws Exception {
        refService.linkChildrenWithParents(domain, new ArrayList<>());
        return ((JpaRepository<Domain, IdClass>) dao).save(domain);
    }

    @Cacheable(cacheNames = "Entity",key = "#id")
    public Domain getById(IdClass id) {
        Optional<Domain> result = ((JpaRepository<Domain, IdClass>) dao).findById(id);
        if (!result.isPresent()) {
            throw new NotFoundException();
        }
        return result.get();
    }

    @Cacheable(cacheNames = "ALL_Entities",key = "getAll")
    public Page getAll(Pagination pagination, int... recordStatus) {

        return recordStatus != null ?
                dao.findAllByStatusWithPagination(paginationService.getPagination(pagination), recordStatus) :
                dao.findAll(paginationService.getPagination(pagination));
    }


    @Transactional
    public Boolean delete(IdClass id) throws InterruptedException {
        Domain domain = this.getById(id);
        if (domain == null) {
            throw new NotFoundException();
        }

        //dao.delete(domain);
        dao.softDelete(id);

        return true;
    }


}
