package world.region;

import util.integrity.Preconditions;
import world.entity.Entity;
import world.area.Position;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RegionDivision<T extends Entity> {
    /**
     * Divides players up by region, to make processing of certain things easier. E.G
     * AOE spells.
     */
    private final Map<Position, Set<T>> entitiesByRegion = new HashMap<>();

    /**
     * Gets players by region.
     * The set returned is unmodifiable.
     */
    public Optional<Set<T>> getEntitiesByRegion(Position regionPosition) {
        Preconditions.notNull(regionPosition);
        if (!entitiesByRegion.containsKey(regionPosition)) {
            return Optional.empty();
        }

        return Optional.of(Collections.unmodifiableSet(entitiesByRegion.get(regionPosition)));
    }


    public Set<T> getEntitiesByQuadRegion(Position regionPosition) {
        Preconditions.notNull(regionPosition);

        Set<T> playersInQuadRegion = new HashSet<>();

        Consumer<Set<T>> addPlayersToQuadRegion = (players) -> playersInQuadRegion.addAll(players);

        getEntitiesByRegion(regionPosition).ifPresent(addPlayersToQuadRegion);

        Position rightRegion = regionPosition.copy();
        rightRegion.getVector().addX(1);
        getEntitiesByRegion(rightRegion).ifPresent(addPlayersToQuadRegion);

        Position leftRegion = regionPosition.copy();
        leftRegion.getVector().subtractX(1);
        getEntitiesByRegion(leftRegion).ifPresent(addPlayersToQuadRegion);

        Position topRegion = regionPosition.copy();
        topRegion.getVector().addY(1);
        getEntitiesByRegion(topRegion).ifPresent(addPlayersToQuadRegion);

        Position bottomRegion = regionPosition.copy();
        topRegion.getVector().subtractY(1);
        getEntitiesByRegion(bottomRegion).ifPresent(addPlayersToQuadRegion);

        return playersInQuadRegion;
    }

    /**
     * Returns set of players in quad region surrounding player filtered by the given predicate.
     * @param regionPosition
     * @param predicate
     * @return
     */
    public Set<T> getEntitiesByQuadRegion(Position regionPosition, Predicate<T> predicate){
        return getEntitiesByQuadRegion(regionPosition).stream().filter(predicate).collect(Collectors.toSet());
    }

    private void addEntityToRegion(T p) {
        Preconditions.notNull(p,p.getPosition());

        Position regionPosition = p.getPosition().getRegionPosition();

        if (!entitiesByRegion.containsKey(regionPosition)) {
            Set<T> set = new HashSet<>();
            set.add(p);
            entitiesByRegion.put(regionPosition, set);
        } else {
            entitiesByRegion.get(regionPosition).add(p);
        }

        p.setLastRegionPosition(regionPosition);
    }

    public void removeEntityFromRegion(Entity e){
        Set<T> entitiesInRegion = entitiesByRegion.get(e.getLastRegionPosition());
        Preconditions.notNull(entitiesInRegion);
        Preconditions.areEqual(entitiesInRegion.contains(e),true);
        entitiesInRegion.remove(e);
    }

    /**
     * Update player region.
     * Should be called any time a players region is updated
     * which will in turn update the entitiesByRegion map.
     *
     * @param p the p
     */
    public void updateEntityRegion(T p) {
        Preconditions.notNull(p);

        if(p.getPosition().getRegionPosition().equals(p.getLastRegionPosition())){
            return;
        }

        Position lastRegionPosition = p.getLastRegionPosition();


        if(lastRegionPosition == null){
            addEntityToRegion(p);
        }else {
            removeEntityFromRegion(p);
            addEntityToRegion(p);
        }
    }
}
