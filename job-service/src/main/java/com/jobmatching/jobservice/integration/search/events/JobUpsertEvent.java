package com.jobmatching.jobservice.integration.search.events;

import com.jobmatching.jobservice.model.Job;

public record JobUpsertEvent(Job job) { }
