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

package org.apache.ignite.ml.encog.caches;

import org.encog.ml.MethodFactory;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.ea.genome.GenomeFactory;
import org.encog.ml.factory.MLMethodFactory;

public class TrainingContext {
    private final MethodFactory methodFactory;
    private GenomeFactory factory;
    private MLDataSet ds;

    public TrainingContext(GenomeFactory genomeFactory, MethodFactory mlMethodFactory, MLDataSet ds) {
        this.factory = genomeFactory;
        this.methodFactory = mlMethodFactory;
        this.ds = ds;
    }

    public GenomeFactory genomeFactory() {
        return factory;
    }

    public MethodFactory getMlMethodFactory() {
        return methodFactory;
    }

    public MLDataSet getDataset() {
        return ds;
    }
}
