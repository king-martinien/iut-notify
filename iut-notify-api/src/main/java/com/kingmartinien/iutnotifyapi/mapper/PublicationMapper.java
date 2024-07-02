package com.kingmartinien.iutnotifyapi.mapper;

import com.kingmartinien.iutnotifyapi.dto.PublicationDto;
import com.kingmartinien.iutnotifyapi.entity.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PublicationMapper {

    PublicationDto toDto(Publication publication);

    List<PublicationDto> toDto(List<Publication> publications);

    Publication toEntity(PublicationDto publicationDto);

}
