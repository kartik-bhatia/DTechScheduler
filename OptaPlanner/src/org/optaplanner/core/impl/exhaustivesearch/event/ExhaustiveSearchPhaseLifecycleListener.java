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

package org.optaplanner.core.impl.exhaustivesearch.event;

import org.optaplanner.core.impl.exhaustivesearch.scope.ExhaustiveSearchPhaseScope;
import org.optaplanner.core.impl.exhaustivesearch.scope.ExhaustiveSearchStepScope;
import org.optaplanner.core.impl.solver.event.SolverLifecycleListener;

public interface ExhaustiveSearchPhaseLifecycleListener extends SolverLifecycleListener {

    void phaseStarted(ExhaustiveSearchPhaseScope phaseScope);

    void stepStarted(ExhaustiveSearchStepScope stepScope);

    void stepEnded(ExhaustiveSearchStepScope stepScope);

    void phaseEnded(ExhaustiveSearchPhaseScope phaseScope);

}
