/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.ml.encog;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeLoadBalancer;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.apache.ignite.lang.IgniteBiTuple;
import org.apache.ignite.ml.encog.caches.TrainingContext;
import org.apache.ignite.ml.encog.caches.TrainingContextCache;
import org.apache.ignite.ml.math.Matrix;
import org.apache.ignite.ml.math.functions.IgniteFunction;
import org.apache.ignite.resources.LoadBalancerResource;
import org.encog.ml.genetic.MLMethodGenome;
import org.jetbrains.annotations.Nullable;

/**
 * TODO: add description.
 *
 * Train and choose leader.
 */
public class GroupTrainerTask<S, U extends Serializable> extends ComputeTaskAdapter<UUID, U> {
    private IgniteFunction<Collection<S>, U> statsAggregator;
    private U inputData;

    public GroupTrainerTask(IgniteFunction<Collection<S>, U> statsAggregator, U inputData) {
        this.statsAggregator = statsAggregator;
        this.inputData = inputData;
    }

    @Nullable @Override public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> subgrid,
        @Nullable UUID trainingUuid) throws IgniteException {
        Map<ComputeJob, ClusterNode> res = new HashMap<>();

        for (ClusterNode node : subgrid)
            res.put(new LocalTrainingTickJob<>(trainingUuid, inputData), node);

        return res;
    }

    @Nullable @Override
    public U reduce(List<ComputeJobResult> results) throws IgniteException {
        return statsAggregator.apply(results.stream().map(res -> ((S)res.getData())).collect(Collectors.toList()));
    }
}
