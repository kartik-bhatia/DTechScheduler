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

package org.optaplanner.core.impl.domain.variable.inverserelation;

import java.io.Serializable;

import org.optaplanner.core.impl.domain.variable.descriptor.VariableDescriptor;
import org.optaplanner.core.impl.domain.variable.supply.Demand;
import org.optaplanner.core.impl.score.director.InnerScoreDirector;

public class SingletonInverseVariableDemand implements Demand<SingletonInverseVariableSupply>, Serializable {

    private static final int CLASS_NAME_HASH_CODE = SingletonInverseVariableDemand.class.getName().hashCode() * 37;

    protected final VariableDescriptor sourceVariableDescriptor;

    public SingletonInverseVariableDemand(VariableDescriptor sourceVariableDescriptor) {
        this.sourceVariableDescriptor = sourceVariableDescriptor;
    }

    public VariableDescriptor getSourceVariableDescriptor() {
        return sourceVariableDescriptor;
    }

    // ************************************************************************
    // Creation method
    // ************************************************************************

    public SingletonInverseVariableSupply createExternalizedSupply(InnerScoreDirector scoreDirector) {
        return new ExternalizedSingletonInverseVariableSupply(sourceVariableDescriptor);
    }

    // ************************************************************************
    // Equals/hashCode method
    // ************************************************************************

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingletonInverseVariableDemand)) {
            return false;
        }
        SingletonInverseVariableDemand other = (SingletonInverseVariableDemand) o;
        if (!sourceVariableDescriptor.equals(other.sourceVariableDescriptor)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return CLASS_NAME_HASH_CODE + sourceVariableDescriptor.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + sourceVariableDescriptor.getSimpleEntityAndVariableName() + ")";
    }

}
