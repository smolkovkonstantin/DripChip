package com.example.dripchip.repositorie;

import com.example.dripchip.entites.Account;
import com.example.dripchip.entites.Animal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface AnimalDAO extends JpaRepository<Animal, Long> {
    @Query(value =
            "select * from Animal JOIN Account on Animal.account=Account.id_account " +
                    "WHERE (id_account = ?1 or ?1 is null) and (chipping_location_id = ?2 or ?2 is null)" +
                    "and (life_status = ?3 or ?3 is null) and (gender = ?4 or ?4 is null)" +
                    "and (chipping_date_time > CAST(?5 AS timestamp) or CAST(?5 AS timestamp) is null)" +
                    "and (chipping_date_time < CAST(?6 AS timestamp) or CAST(?6 AS timestamp) is null)" +
                    " order by id", nativeQuery = true)
    List<Animal> searchByParameters(Integer chipperId, Long chippingLocationId,
                                    String lifeStatus, String gender,
                                    Date startDateTime, Date endDateTime,
                                    Pageable pageable);

    @Transactional
    @Modifying
    @Query("""
            update Animal a set a.weight = ?1, a.length = ?2, a.height = ?3, a.gender = ?4, a.lifeStatus = ?5,
             a.deathDateTime=?6, a.account = ?7, a.chippingLocationId = ?8
            where a.id = ?9""")
    void updateAnimal(Float weight, Float length, Float height, String gender, String lifeStatus, Date deathDateTime,
                      Account account, Long chippingLocationId, Long id);
}
