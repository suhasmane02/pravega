/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.segmentstore.storage.impl.bookkeeper;

import io.pravega.segmentstore.storage.QueueStats;
import io.pravega.shared.MetricsNames;
import io.pravega.shared.metrics.DynamicLogger;
import io.pravega.shared.metrics.MetricsProvider;
import io.pravega.shared.metrics.OpStatsLogger;
import io.pravega.shared.metrics.StatsLogger;
import java.time.Duration;

/**
 * Metrics for BookKeeper.
 */
final class Metrics {
    private static final StatsLogger STATS_LOGGER = MetricsProvider.createStatsLogger("bookkeeper");
    private static final DynamicLogger DYNAMIC_LOGGER = MetricsProvider.getDynamicLogger();

    /**
     * BookKeeperLog-specific (i.e. per Container) Metrics.
     */
    final static class BookKeeperLog {
        private final String writeQueueSize;
        private final String writeQueueFillRate;
        private final String ledgerCount;
        private final OpStatsLogger writeLatency;
        private final OpStatsLogger totalWriteLatency;
        private final OpStatsLogger writeBytes;

        BookKeeperLog(int containerId) {
            this.ledgerCount = MetricsNames.nameFromContainer(MetricsNames.BK_LEDGER_COUNT, containerId);
            this.writeQueueSize = MetricsNames.nameFromContainer(MetricsNames.BK_WRITE_QUEUE_SIZE, containerId);
            this.writeQueueFillRate = MetricsNames.nameFromContainer(MetricsNames.BK_WRITE_QUEUE_FILL_RATE, containerId);
            this.writeLatency = STATS_LOGGER.createStats(MetricsNames.nameFromContainer(MetricsNames.BK_WRITE_LATENCY, containerId));
            this.totalWriteLatency = STATS_LOGGER.createStats(MetricsNames.nameFromContainer(MetricsNames.BK_TOTAL_WRITE_LATENCY, containerId));
            this.writeBytes = STATS_LOGGER.createStats(MetricsNames.nameFromContainer(MetricsNames.BK_WRITE_BYTES, containerId));
        }

        void ledgerCount(int count) {
            DYNAMIC_LOGGER.reportGaugeValue(this.ledgerCount, count);
        }

        void queueStats(QueueStats qs) {
            int fillRate = (int) (qs.getAverageItemFillRate() * 100);
            DYNAMIC_LOGGER.reportGaugeValue(this.writeQueueSize, qs.getSize());
            DYNAMIC_LOGGER.reportGaugeValue(this.writeQueueFillRate, fillRate);
        }

        void writeCompleted(Duration elapsed) {
            this.totalWriteLatency.reportSuccessEvent(elapsed);
        }

        void bookKeeperWriteCompleted(int length, Duration elapsed) {
            this.writeLatency.reportSuccessEvent(elapsed);
            this.writeBytes.reportSuccessValue(length);
        }
    }
}
