package com.minecolonies.coremod.entity.ai.statemachine.basestatemachine;

import com.minecolonies.coremod.entity.ai.statemachine.states.IAIEventType;
import com.minecolonies.coremod.entity.ai.statemachine.states.IAIState;
import com.minecolonies.coremod.entity.ai.statemachine.transitions.IStateMachineEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Basic event for statemachines, consists of a condition and a statesupplier to transition the statemachine into.
 * Events are always executed before any state transitions happen.
 */
public class BasicEvent extends BasicTransition implements IStateMachineEvent
{
    /**
     * The event type of this event
     */
    private final IAIEventType eventType;

    public BasicEvent(
      @NotNull final IAIEventType eventType,
      @NotNull final BooleanSupplier condition,
      @NotNull final Supplier<IAIState> nextState)
    {
        super(condition, nextState);
        this.eventType = eventType;
    }

    /**
     * Get the Eventtype
     *
     * @return IAIEventType
     */
    public IAIEventType getEventType()
    {
        return eventType;
    }

    /**
     * Events do not have a state
     */
    public final IAIState getState()
    {
        return null;
    }
}
