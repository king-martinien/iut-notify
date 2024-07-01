package com.kingmartinien.iutnotifyapi.repository;

import com.kingmartinien.iutnotifyapi.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelReposiroty extends JpaRepository<Channel, Long> {

    Optional<Channel> findByName(String name);

}
