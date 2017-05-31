package springboot.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.domain.CompInteScoItem;
import springboot.repository.CompInteScoItemRepository;
import springboot.rest.util.HeaderUtil;
import springboot.rest.util.ResponseUtil;
import springboot.service.dto.CompInteScoItemDTO;
import springboot.service.mapper.CompInteScoItemMapper;

/**
 * REST controller for managing CompInteScoItem.
 */
//@RestController
//@RequestMapping("/api")
public class CompInteScoItemResource {

    private final Logger log = LoggerFactory.getLogger(CompInteScoItemResource.class);

    private static final String ENTITY_NAME = "compInteScoItem";
        
    private final CompInteScoItemRepository compInteScoItemRepository;

    private final CompInteScoItemMapper compInteScoItemMapper;

    public CompInteScoItemResource(CompInteScoItemRepository compInteScoItemRepository, CompInteScoItemMapper compInteScoItemMapper) {
        this.compInteScoItemRepository = compInteScoItemRepository;
        this.compInteScoItemMapper = compInteScoItemMapper;
    }

    /**
     * POST  /comp-inte-sco-items : Create a new compInteScoItem.
     *
     * @param compInteScoItemDTO the compInteScoItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compInteScoItemDTO, or with status 400 (Bad Request) if the compInteScoItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comp-inte-sco-items")
    public ResponseEntity<CompInteScoItemDTO> createCompInteScoItem(@Valid @RequestBody CompInteScoItemDTO compInteScoItemDTO) throws URISyntaxException {
        log.debug("REST request to save CompInteScoItem : {}", compInteScoItemDTO);
        if (compInteScoItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new compInteScoItem cannot already have an ID")).body(null);
        }
        CompInteScoItem compInteScoItem = compInteScoItemMapper.compInteScoItemDTOToCompInteScoItem(compInteScoItemDTO);
        compInteScoItem = compInteScoItemRepository.save(compInteScoItem);
        CompInteScoItemDTO result = compInteScoItemMapper.compInteScoItemToCompInteScoItemDTO(compInteScoItem);
        return ResponseEntity.created(new URI("/api/comp-inte-sco-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comp-inte-sco-items : Updates an existing compInteScoItem.
     *
     * @param compInteScoItemDTO the compInteScoItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compInteScoItemDTO,
     * or with status 400 (Bad Request) if the compInteScoItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the compInteScoItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comp-inte-sco-items")
    public ResponseEntity<CompInteScoItemDTO> updateCompInteScoItem(@Valid @RequestBody CompInteScoItemDTO compInteScoItemDTO) throws URISyntaxException {
        log.debug("REST request to update CompInteScoItem : {}", compInteScoItemDTO);
        if (compInteScoItemDTO.getId() == null) {
            return createCompInteScoItem(compInteScoItemDTO);
        }
        CompInteScoItem compInteScoItem = compInteScoItemMapper.compInteScoItemDTOToCompInteScoItem(compInteScoItemDTO);
        compInteScoItem = compInteScoItemRepository.save(compInteScoItem);
        CompInteScoItemDTO result = compInteScoItemMapper.compInteScoItemToCompInteScoItemDTO(compInteScoItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, compInteScoItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comp-inte-sco-items : get all the compInteScoItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of compInteScoItems in body
     */
    @GetMapping("/comp-inte-sco-items")
    public List<CompInteScoItemDTO> getAllCompInteScoItems() {
        log.debug("REST request to get all CompInteScoItems");
        List<CompInteScoItem> compInteScoItems = compInteScoItemRepository.findAll();
        return compInteScoItemMapper.compInteScoItemsToCompInteScoItemDTOs(compInteScoItems);
    }

    /**
     * GET  /comp-inte-sco-items/:id : get the "id" compInteScoItem.
     *
     * @param id the id of the compInteScoItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compInteScoItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comp-inte-sco-items/{id}")
    public ResponseEntity<CompInteScoItemDTO> getCompInteScoItem(@PathVariable Long id) {
        log.debug("REST request to get CompInteScoItem : {}", id);
        CompInteScoItem compInteScoItem = compInteScoItemRepository.findOne(id);
        CompInteScoItemDTO compInteScoItemDTO = compInteScoItemMapper.compInteScoItemToCompInteScoItemDTO(compInteScoItem);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(compInteScoItemDTO));
    }

    /**
     * DELETE  /comp-inte-sco-items/:id : delete the "id" compInteScoItem.
     *
     * @param id the id of the compInteScoItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comp-inte-sco-items/{id}")
    public ResponseEntity<Void> deleteCompInteScoItem(@PathVariable Long id) {
        log.debug("REST request to delete CompInteScoItem : {}", id);
        compInteScoItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
