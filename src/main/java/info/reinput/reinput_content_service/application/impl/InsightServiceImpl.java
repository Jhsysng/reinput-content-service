package info.reinput.reinput_content_service.application.impl;

import info.reinput.reinput_content_service.application.InsightService;
import info.reinput.reinput_content_service.application.dto.InsightCountCollection;
import info.reinput.reinput_content_service.application.dto.InsightDto;
import info.reinput.reinput_content_service.application.dto.InsightSummaryCollection;
import info.reinput.reinput_content_service.application.dto.ReminderDto;
import info.reinput.reinput_content_service.infra.InsightRepository;
import info.reinput.reinput_content_service.infra.client.NotificationClientAdapter;
import info.reinput.reinput_content_service.infra.client.WorkspaceClientAdapter;
import info.reinput.reinput_content_service.insight.domain.Insight;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightServiceImpl implements InsightService {

    private final InsightRepository insightRepository;
    private final WorkspaceClientAdapter workspaceClientAdapter;
    private final NotificationClientAdapter notificationClientAdapter;

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
    public InsightSummaryCollection getInsightSummaries(final Long folderId, final Long memberId) {
        log.info("[InsightService.getInsightSummaries] folderId : {}, memberId : {}", folderId, memberId);

        return InsightSummaryCollection.builder()
                .insightSummaries(insightRepository.getInsightSummaries(folderId))
                .build();
    }

    @Override
    public InsightSummaryCollection getSharedInsightSummaries(final String shareId, final Long memberId) {
        log.info("[InsightService.getSharedInsightSummaries] shareId : {}, memberId : {}", shareId, memberId);


        return InsightSummaryCollection.builder()
                .insightSummaries(insightRepository.getInsightSummaries(getSharedFolderId(shareId, memberId)))
                .build();

    }

    @Transactional
    @Override
    public InsightDto editInsight(final InsightDto insightDto, final Long memberId) {
        log.info("[InsightService.editInsight] insightDto : {}", insightDto);

        ReminderDto reminderDto = saveReminder(insightDto.reminder());

        Insight insight = insightRepository.findById(insightDto.id())
                .orElseThrow(() -> new IllegalArgumentException("Insight not found"));

        insight.update(
                Insight.createSummary(insightDto.title(), insightDto.AISummary(), insightDto.mainImagePath()),
                Insight.createDetail(insightDto.url(), insightDto.memo(), insightDto.source()),
                insightDto.images(),
                insightDto.hashTags()
        );

        return InsightDto.from(insight, reminderDto);
    }

    @Transactional
    @Override
    public InsightDto saveInsight(final InsightDto insightDto, final Long memberId) {
        log.info("[InsightService.saveInsight] insightDto : {}", insightDto);

        ReminderDto reminderDto = saveReminder(insightDto.reminder());

        return InsightDto.from(insightRepository.save(Insight.createInsight(
                Insight.createSummary(insightDto.title(), insightDto.AISummary(), insightDto.mainImagePath()),
                Insight.createDetail(insightDto.url(), insightDto.memo(), insightDto.source()),
                insightDto.images(),
                insightDto.hashTags(),
                insightDto.folderId(),
                memberId
        )), reminderDto);
    }

    private ReminderDto saveReminder(final ReminderDto reminderDto) {
        return notificationClientAdapter.saveReminder(reminderDto);
    }

    private Long getSharedFolderId(final String shareId, final Long memberId) {
        return workspaceClientAdapter.getSharedFolderId(shareId, memberId);
    }
}
