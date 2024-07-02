package com.kingmartinien.iutnotifyapi.repository;

import com.kingmartinien.iutnotifyapi.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findAllByChannelId(Long channelId);

}
