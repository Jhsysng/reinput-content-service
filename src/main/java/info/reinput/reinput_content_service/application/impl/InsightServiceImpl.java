package info.reinput.reinput_content_service.application.impl;

import info.reinput.reinput_content_service.application.InsightService;
import info.reinput.reinput_content_service.application.dto.InsightCountCollection;
import info.reinput.reinput_content_service.application.dto.InsightSummaryCollection;
import info.reinput.reinput_content_service.infra.InsightRepository;
import info.reinput.reinput_content_service.infra.client.WorkspaceClientAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightServiceImpl implements InsightService {

    private final InsightRepository insightRepository;
    private final WorkspaceClientAdapter workspaceClientAdapter;

    @Override
    public Long countInsight(final Long folderId, final Long memberId) {
        log.info("[InsightService.countInsight] folderId : {}, memberId : {}", folderId, memberId);

        return insightRepository.countByFolderId(folderId);
    }

    @Override
    public InsightCountCollection countInsight(final List<Long> folderIds, final Long memberId) {
        log.info("[InsightService.countInsights] folderIds : {}, memberId : {}", folderIds, memberId);

        return InsightCountCollection.builder()
                .insightCountMap(insightRepository.countByFolderIds(folderIds))
                .build();
    }

    @Override
    public InsightSummaryCollection getSharedInsightSummaries(final String shareId, final Long memberId) {
        log.info("[InsightService.getSharedInsightSummaries] shareId : {}, memberId : {}", shareId, memberId);


        return InsightSummaryCollection.builder()
                .insightSummaries(insightRepository.getInsightSummaries(getSharedFolderId(shareId, memberId)))
                .build();

    }

    private Long getSharedFolderId(final String shareId, final Long memberId) {
        return workspaceClientAdapter.getSharedFolderId(shareId, memberId);
    }
}
