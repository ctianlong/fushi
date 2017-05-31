package springboot.service.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import springboot.domain.CompInteScoItem;
import springboot.service.dto.CompInteScoItemDTO;

/**
 * Mapper for the entity CompInteScoItem and its DTO CompInteScoItemDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, StudentMapper.class, })
public interface CompInteScoItemMapper {

    @Mapping(target = "teacherId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "teacherName", ignore = true)
    @Mapping(target = "studentName", ignore = true)
    CompInteScoItemDTO compInteScoItemToCompInteScoItemDTO(CompInteScoItem compInteScoItem);

    List<CompInteScoItemDTO> compInteScoItemsToCompInteScoItemDTOs(List<CompInteScoItem> compInteScoItems);

    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "student", ignore = true)
    CompInteScoItem compInteScoItemDTOToCompInteScoItem(CompInteScoItemDTO compInteScoItemDTO);

    List<CompInteScoItem> compInteScoItemDTOsToCompInteScoItems(List<CompInteScoItemDTO> compInteScoItemDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default CompInteScoItem compInteScoItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        CompInteScoItem compInteScoItem = new CompInteScoItem();
        compInteScoItem.setId(id);
        return compInteScoItem;
    }
    

}
