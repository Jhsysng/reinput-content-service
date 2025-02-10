package info.reinput.reinput_content_service.application;

import info.reinput.reinput_content_service.application.dto.InsightCountCollection;
import info.reinput.reinput_content_service.application.dto.InsightDto;
import info.reinput.reinput_content_service.application.dto.InsightSummaryCollection;

import java.util.List;
import java.util.Map;

public interface InsightService {
    Long countInsight(final Long folderId, final Long memberId);
    InsightCountCollection countInsight(final List<Long> folderIds, final Long memberId);
    InsightSummaryCollection getInsightSummaries(final Long folderId, final Long memberId);
    InsightDto editInsight(final InsightDto insightDto, final Long memberId);
    InsightDto saveInsight(final InsightDto insightDto, final Long memberId);
    InsightSummaryCollection getSharedInsightSummaries(final String shareId, final Long memberId);
}
