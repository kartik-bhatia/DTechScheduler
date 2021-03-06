/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.config.heuristic.selector.move.generic;

import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.core.config.heuristic.selector.entity.pillar.PillarSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.MoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.value.ValueSelectorConfig;
import org.optaplanner.core.impl.heuristic.selector.entity.pillar.PillarSelector;
import org.optaplanner.core.impl.heuristic.selector.move.MoveSelector;
import org.optaplanner.core.impl.heuristic.selector.move.generic.PillarChangeMoveSelector;
import org.optaplanner.core.impl.heuristic.selector.value.ValueSelector;

@XStreamAlias("pillarChangeMoveSelector")
public class PillarChangeMoveSelectorConfig extends MoveSelectorConfig {

    @XStreamAlias("pillarSelector")
    private PillarSelectorConfig pillarSelectorConfig = null;
    @XStreamAlias("valueSelector")
    private ValueSelectorConfig valueSelectorConfig = null;

    public PillarSelectorConfig getPillarSelectorConfig() {
        return pillarSelectorConfig;
    }

    public void setPillarSelectorConfig(PillarSelectorConfig pillarSelectorConfig) {
        this.pillarSelectorConfig = pillarSelectorConfig;
    }

    public ValueSelectorConfig getValueSelectorConfig() {
        return valueSelectorConfig;
    }

    public void setValueSelectorConfig(ValueSelectorConfig valueSelectorConfig) {
        this.valueSelectorConfig = valueSelectorConfig;
    }

    // ************************************************************************
    // Builder methods
    // ************************************************************************

    public MoveSelector buildBaseMoveSelector(HeuristicConfigPolicy configPolicy,
            SelectionCacheType minimumCacheType, boolean randomSelection) {
        PillarSelectorConfig pillarSelectorConfig_ = pillarSelectorConfig == null ? new PillarSelectorConfig()
                : pillarSelectorConfig;
        List<String> variableNameIncludeList = valueSelectorConfig == null ? null
                : valueSelectorConfig.getVariableName() == null ? null
                : Collections.singletonList(valueSelectorConfig.getVariableName());
        PillarSelector pillarSelector = pillarSelectorConfig_.buildPillarSelector(configPolicy,
                minimumCacheType, SelectionOrder.fromRandomSelectionBoolean(randomSelection), variableNameIncludeList);
        ValueSelectorConfig valueSelectorConfig_ = valueSelectorConfig == null ? new ValueSelectorConfig()
                : valueSelectorConfig;
        ValueSelector valueSelector = valueSelectorConfig_.buildValueSelector(configPolicy,
                pillarSelector.getEntityDescriptor(),
                minimumCacheType, SelectionOrder.fromRandomSelectionBoolean(randomSelection));
        return new PillarChangeMoveSelector(pillarSelector, valueSelector, randomSelection);
    }

    public void inherit(PillarChangeMoveSelectorConfig inheritedConfig) {
        super.inherit(inheritedConfig);
        if (pillarSelectorConfig == null) {
            pillarSelectorConfig = inheritedConfig.getPillarSelectorConfig();
        } else if (inheritedConfig.getPillarSelectorConfig() != null) {
            pillarSelectorConfig.inherit(inheritedConfig.getPillarSelectorConfig());
        }
        if (valueSelectorConfig == null) {
            valueSelectorConfig = inheritedConfig.getValueSelectorConfig();
        } else if (inheritedConfig.getValueSelectorConfig() != null) {
            valueSelectorConfig.inherit(inheritedConfig.getValueSelectorConfig());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + pillarSelectorConfig + ", " + valueSelectorConfig + ")";
    }

}
