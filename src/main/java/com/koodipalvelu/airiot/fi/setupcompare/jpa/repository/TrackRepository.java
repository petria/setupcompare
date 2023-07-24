package com.koodipalvelu.airiot.fi.setupcompare.jpa.repository;


import com.koodipalvelu.airiot.fi.setupcompare.jpa.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Track findOneByName(String name);

    List<Track> findAllByName(String name);

}
