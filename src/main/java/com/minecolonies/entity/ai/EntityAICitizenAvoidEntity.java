package com.minecolonies.entity.ai;

import com.minecolonies.entity.EntityCitizen;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class EntityAICitizenAvoidEntity extends EntityAIBase
{
    public final IEntitySelector entitySelector = new IEntitySelector()
    {
        /**
         * Return whether the specified entity is applicable to this filter.
         * TODO: what is this filter filtering for??? improve docs
         */
        @Override
        public boolean isEntityApplicable(Entity target)
        {
            return target.isEntityAlive() && EntityAICitizenAvoidEntity.this.theEntity.getEntitySenses().canSee(target);
        }
    };

    /** The entity we are attached to */
    private EntityCitizen theEntity;
    private double        farSpeed;
    private double        nearSpeed;
    private Entity        closestLivingEntity;
    private float         distanceFromEntity;
    private Class         targetEntityClass;

    public EntityAICitizenAvoidEntity(EntityCitizen entity, Class targetEntityClass, float distanceFromEntity, double farSpeed, double nearSpeed)
    {
        this.theEntity = entity;
        this.targetEntityClass = targetEntityClass;
        this.distanceFromEntity = distanceFromEntity;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        closestLivingEntity = getClosestToAvoid();
        return closestLivingEntity != null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !theEntity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        performMoveAway();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        closestLivingEntity = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        Entity newClosest = getClosestToAvoid();
        if (newClosest != null && newClosest != closestLivingEntity)
        {
            closestLivingEntity = newClosest;
            performMoveAway();
            return;
        }

        if (theEntity.getDistanceSqToEntity(closestLivingEntity) < 49.0D)
        {
            theEntity.getNavigator().setSpeed(nearSpeed);
        }
        else
        {
            theEntity.getNavigator().setSpeed(farSpeed);
        }
    }

    protected Entity getClosestToAvoid()
    {
        if (targetEntityClass == EntityPlayer.class)
        {
            return theEntity.worldObj.getClosestPlayerToEntity(theEntity, (double) distanceFromEntity);
        }
        else
        {
            List list = theEntity.worldObj.selectEntitiesWithinAABB(targetEntityClass, theEntity.boundingBox.expand((double)distanceFromEntity, 3.0D, (double)distanceFromEntity), entitySelector);

            if (list.isEmpty())
            {
                return null;
            }

            return (Entity)list.get(0);
        }
    }

    protected void performMoveAway()
    {
        theEntity.getNavigator().moveAwayFromEntityLiving(closestLivingEntity, distanceFromEntity * 2, nearSpeed);
    }
}