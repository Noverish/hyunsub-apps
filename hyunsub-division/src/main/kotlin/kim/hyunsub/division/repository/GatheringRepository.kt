package kim.hyunsub.division.repository

import kim.hyunsub.division.repository.entity.Gathering
import org.springframework.data.jpa.repository.JpaRepository

interface GatheringRepository: JpaRepository<Gathering, String>
