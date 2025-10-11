package com.jobmatching.jobservice.integration.search;

import com.jobmatching.jobservice.integration.search.dto.SearchJobDoc;
import com.jobmatching.jobservice.integration.search.events.JobDeleteEvent;
import com.jobmatching.jobservice.integration.search.events.JobUpsertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobSearchSyncListener {

    private final SearchSyncClient client;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUpsert(JobUpsertEvent ev) {
        SearchJobDoc doc = SearchMapper.toDoc(ev.job());
        client.upsert(doc);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDelete(JobDeleteEvent ev) {
        client.delete(ev.jobId());
    }
}
