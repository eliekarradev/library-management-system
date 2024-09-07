package libraryMS.controller.generic;

import jakarta.validation.Valid;
import libraryMS.dao.generic.GenericDao;
import libraryMS.domain.generic.GenericDomain;
import libraryMS.exception.exceptions.NotFoundException;
import libraryMS.service.generic.GenericService;
import libraryMS.utils.annotation.ParameterName;
import libraryMS.utils.model.Pagination;
import libraryMS.utils.model.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class GenericController<Service extends GenericService<Dao, Domain, Id>,
        Dao extends GenericDao<Domain, Id> & Serializable,
        Domain extends GenericDomain & Serializable,
        Id extends Serializable> {

    @Autowired
    protected Service service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll(@ModelAttribute Pagination pagination,
                                 @RequestParam(required = false) int... status) {
        Page<Domain> page = service.getAll(pagination, status);
        Map<String, Object> result = new HashMap<>();
        if (page != null) {
            result.put("count", page.getTotalElements());
            result.put("data", page.getContent());

            return ResponseObject.SUCCESS_RESPONSE("Data Retrieved Successfully", result);
        }


        return ResponseObject.FAILED_RESPONSE("Failed to get Data", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Id id) {
        Domain model = service.getById(id);

        if (model == null) {
            return ResponseObject.FAILED_RESPONSE("Entity Not Found", HttpStatus.NOT_FOUND);
        }
        return ResponseObject.SUCCESS_RESPONSE("Fetched Successfully", model);
    }


    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> save(@Valid @RequestBody Domain domain) throws Exception {
        Domain savedDomain = service.merge(domain);
        return ResponseObject.SUCCESS_RESPONSE("Added Successfully", savedDomain);
    }

    @RequestMapping(value = "/add-list", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> saveList(@ParameterName("List") List<Domain> list) {
        Iterable<Domain> savedDomains = service.insert(list);
        return ResponseObject.SUCCESS_RESPONSE("Added Successfully", savedDomains);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> update(@Valid @RequestBody Domain domain,@PathVariable Id id) throws Exception {
        Domain searchedDomain = service.getById(id);
        if(searchedDomain == null){
            throw new NotFoundException();
        }
        domain.setId((int)id);
        Domain updatedDomain = service.merge(domain);
        return ResponseObject.SUCCESS_RESPONSE("Updated Successfully", updatedDomain);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable("id") Id id) throws Exception {
        Domain domain = service.getById(id);
        if (domain == null) {
            return ResponseObject.FAILED_RESPONSE("Entity Not Found", HttpStatus.NOT_FOUND);
        }

        Boolean success = service.delete(id);

        return success ?
                ResponseObject.SUCCESS_RESPONSE("Deleted Successfully", null) :
                ResponseObject.FAILED_RESPONSE("Error in delete the entity", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
