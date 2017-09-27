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

package org.apache.ignite.ml.encog.evolution.operators;

import java.util.Random;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.genetic.MLMethodGenome;
import org.encog.neural.networks.BasicNetwork;

/**
 * TODO: add description.
 * TODO: use distribution from ctx.
 */
public class WeightMutation extends IgniteEvolutionaryOperator {
    public WeightMutation(double prob){
        super(prob);
    }

    @Override public int offspringProduced() {
        return 1;
    }

    @Override public int parentsNeeded() {
        return 1;
    }

    @Override public void performOperation(Random rnd, Genome[] parents, int parentIndex, Genome[] offspring,
        int offspringIndex) {
        BasicNetwork parent = (BasicNetwork)((MLMethodGenome)parents[parentIndex]).getPhenotype();
        BasicNetwork child = (BasicNetwork)parent.clone();

        int count = parent.getLayerCount();

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < parent.getLayerNeuronCount(i); j++) {

                for (int k = 0; k < parent.getLayerNeuronCount(i + 1); k++){
                    double parentWeight = parent.getWeight(i, j, k);
                    double shift = rnd.nextDouble() - 0.5d;

                    child.setWeight(i, j, k, parentWeight + rnd.nextDouble() - 0.5d);
                }
            }
        }

        offspring[offspringIndex] = new MLMethodGenome(child);
    }
}
