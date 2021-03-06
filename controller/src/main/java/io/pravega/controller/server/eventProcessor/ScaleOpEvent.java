/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.controller.server.eventProcessor;

import io.pravega.shared.controller.event.ControllerEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.AbstractMap;
import java.util.List;

@Data
@AllArgsConstructor
public class ScaleOpEvent implements ControllerEvent {
    private final String scope;
    private final String stream;
    private final List<Integer> segmentsToSeal;
    private final List<AbstractMap.SimpleEntry<Double, Double>> newRanges;
    private final boolean runOnlyIfStarted;
    private final long scaleTime;

    @Override
    public String getKey() {
        return String.format("%s/%s", scope, stream);
    }
}
