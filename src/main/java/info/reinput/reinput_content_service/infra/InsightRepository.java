package info.reinput.reinput_content_service.infra;

import info.reinput.reinput_content_service.insight.domain.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Long>, InsightRepositoryCustom {

}
