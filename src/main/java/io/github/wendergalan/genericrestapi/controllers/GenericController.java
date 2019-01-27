package io.github.wendergalan.genericrestapi.controllers;

import io.github.wendergalan.genericrestapi.builders.SpecificationBuilder;
import io.github.wendergalan.genericrestapi.exceptions.GenericControllerException;
import io.github.wendergalan.genericrestapi.models.beans.ResponseBean;
import io.github.wendergalan.genericrestapi.utils.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: GenericController.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * This class must be inherited from the controller
 * so that it inherits the CRUD methods. Within the
 * same one can make the overwriting of the necessary
 * methods as needed.
 * *********************************************
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
public abstract class GenericController<T, ID extends Serializable> {

    private Logger logger = LoggerFactory.getLogger(GenericController.class);

    /**
     * The Validator.
     */
    @Autowired
    protected Validator validator;

    /**
     * You can use this variable to retrieve headers.
     *
     * The Http request.
     */
    @Autowired
    protected HttpServletRequest httpRequest;

    /**
     * The Repository.
     */
    @Autowired
    protected JpaRepository<T, ID> repository;

    /**
     * The Spec builder.
     */
    @Autowired(required = false)
    protected SpecificationBuilder<T> specBuilder;

    /**
     * This method searches all records based on the
     * query mounted and sent by the search parameter,
     * you can create your SpecificationBuilder to override
     * the pattern and create more complex searches,
     * watch out for the search operators added so far.
     *
     * @param search    - query with params
     * @param page      - number page
     * @param order     - search order
     * @param size      - size of page
     * @param direction - search direction
     * @return ResponseEntity response entity
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiOperation("Searches all records for the entity.")
    @GetMapping
    public ResponseEntity searchAll(@RequestParam(value = "search", required = false) String search,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(value = "order", required = false) String order,
                                    @RequestParam(value = "size", required = false, defaultValue = "30") Integer size,
                                    @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {
        Specification<T> spec = null;

        if (specBuilder != null && search != null && !search.isEmpty()) {
            specBuilder.cleanParams();

            String[] params = search.split("==|!=|>|<|,");
            if (params != null && params.length > 0) {
                String[] ops = new String[params.length / 2];
                Pattern pattern = Pattern.compile("(==|!=|>|<)");
                Matcher matcher = pattern.matcher(search);
                int count = 0;

                while (matcher.find()) {
                    ops[count] = matcher.group(0);
                    count++;
                }

                count = 0;

                for (int i = 0; i < params.length - 1; i += 2) {
                    specBuilder.with(params[i], count < ops.length ? ops[count] : "==", params[i + 1]);
                    count++;
                }

                spec = specBuilder.build();
            }
        }

        PageRequest pageRequest;
        if (order != null && !order.trim().isEmpty()) {
            pageRequest = PageRequest.of((page != null ? page : 0),
                    (size != null ? (size == -1 ? 1000000 : size) : 30),
                    (direction != null && (direction.toUpperCase().equals(Sort.Direction.ASC.toString()) ||
                            direction.toUpperCase().equals(Sort.Direction.DESC.toString())) ?
                            Sort.Direction.fromString(direction.toString()) : Sort.Direction.ASC),
                    order);
        } else {
            pageRequest = PageRequest.of((page != null ? page : 0),
                    (size != null ? (size == -1 ? 1000000 : size) : 30));
        }

        Page<T> allOfPages;
        if (repository instanceof JpaSpecificationExecutor) {
            allOfPages = ((JpaSpecificationExecutor) repository).findAll(spec, pageRequest);
        } else {
            allOfPages = ((PagingAndSortingRepository) repository).findAll(pageRequest);
        }
        return ResponseEntity.ok(new ResponseBean(page, allOfPages.getTotalPages(), allOfPages.getContent()));
    }

    /**
     * This method searches the entity for the ID.
     * You can load the dependencies of the entity by
     * overwriting the method {@link #loadDependencies(Object)}
     *
     * @param id - Entity ID
     * @return ResponseEntity response entity
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiOperation("Searches only an entity item for ID.")
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") ID id) {
        Optional<T> entity = repository.findById(id);

        if (entity.isPresent()) {
            loadDependencies(entity.get());
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(entity.get());
    }

    /**
     * This method saves the entity.
     * You can prepare the entity to save by
     * calling the method {@link #prepareRecordBeforeSave(Object)},
     * and you can also call the method {@link #afterRecordSave(Object)}
     * to perform operations after saving the entity
     *
     * @param entity - Entity Specified
     * @param result - Binding Result
     * @return ResponseEntity response entity
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiOperation("Save the record.")
    @PostMapping
    public ResponseEntity save(@Valid @RequestBody T entity, BindingResult result) {
        return saveEntity(entity, result);
    }

    /**
     * This method change the entity.
     * You can prepare the entity to save by
     * calling the method {@link #prepareRecordBeforeSave(Object)},
     * and you can also call the method {@link #afterRecordSave(Object)}
     * to perform operations after saving the entity
     *
     * @param entity - Entity Specified
     * @param result - Binding Result
     * @return ResponseEntity response entity
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiOperation("Changes the record.")
    @PutMapping
    public ResponseEntity change(@Valid @RequestBody T entity, BindingResult result) {
        return saveEntity(entity, result);
    }

    /**
     * Deletes the entity for ID.
     *
     * @param id - Entity ID
     * @return ResponseEntity response entity
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiOperation("Deletes the record by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") ID id) {
        try {
            Optional<T> entity = repository.findById(id);

            if (entity.isPresent()) {
                repository.delete(entity.get());
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new GenericControllerException(ex.getMessage(), ex);
        }
    }

    /**
     * This method saves the specified entity.
     * {@link #prepareRecordBeforeSave(Object)}
     * {@link #afterRecordSave(Object)}
     * @param entity - Entity specified
     * @param result - Binding Result
     * @return ResponseEntity
     */
    private ResponseEntity saveEntity(T entity, BindingResult result) {
        try {
            if (setDefaultValue(entity)) {
                DataBinder binder = new DataBinder(entity);
                BindingResult bindingResult = binder.getBindingResult();
                validator.validate(entity, bindingResult);
                result = bindingResult;
            }

            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Util.createListOfValidationErrors(result.getAllErrors()));
            }

            prepareRecordBeforeSave(entity);

            repository.save(entity);

            afterRecordSave(entity);

            return ResponseEntity.ok(entity);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new GenericControllerException(ex.getMessage(), ex);
        }
    }

    /**
     * This method is called within the ID lookup after
     * fetching the entity by ID, for example if you need to
     * load a list of items that will be inserted based on
     * another query, this method is the place for the dependency
     * injection that does not come from the entity.
     *
     * @param entity - Entity specified
     */
    protected void loadDependencies(T entity) {
    }

    /**
     * This method is called when making the validation of
     * the parameters of the entity if the entity requires
     * some type of treatment to be able to set certain value
     * and it is necessary to save and validate.
     *
     * @param entity - Entity specified
     * @return true if it`s ok to continue or false.
     */
    protected boolean setDefaultValue(T entity) {
        return true;
    }

    /**
     * This method is called before saving the record,
     * to load the records to save that entity for example.
     *
     * @param entity - Entity specified
     */
    protected void prepareRecordBeforeSave(T entity) {
    }

    /**
     * This method is called after saving in case you need
     * to make some kind of change after saving the record
     *
     * @param entity - Entity specified
     */
    protected void afterRecordSave(T entity) {
    }
}
