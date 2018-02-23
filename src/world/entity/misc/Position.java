package world.entity.misc;

/**
 * The type Position.
 */
public class Position {
    private static final int MAP_SIZE_TILES = 104;
    private static final int REGION_SIZE_TILES = 64;
    private static final int CHUNK_SIZE_TILES = 8;
    private Vector v;

    /**
     * Instantiates a new Position.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public Position(int x, int y, int z) {
        v = new Vector(x, y, z);
    }

    /**
     * Instantiates a new Position.
     *
     * @param v the v
     */
    public Position(Vector v) {
        this.v = v;
    }

    /**
     * Gets vector.
     *
     * @return the vector
     */
    public Vector getVector() {

        return v;
    }

    /**
     * Sets v.
     *
     * @param v the v
     */
    public void setVector(Vector v) {
        this.v = v;
    }

    /**
     * Pythagorean distance to int.
     *
     * @param other the other
     * @return the int
     */
    public int pythagoreanDistanceTo(Position other) {
        return v.copy().subtract(other.getVector()).length();
    }

    /**
     * Manhattan distance to int.
     *
     * @param other the other
     * @return the int
     */
    public int manhattanDistanceTo(Position other) {
        Vector deltaVec = v.copy().subtract(other.getVector());
        return Math.abs(deltaVec.getX() + deltaVec.getY() + deltaVec.getZ());
    }

    /**
     * Gets map x.
     *
     * @return the map x
     */
    public int getMapX() {
        return getVector().getX() / Position.MAP_SIZE_TILES;
    }

    /**
     * Gets map y.
     *
     * @return the map y
     */
    public int getMapY() {
        return getVector().getY() / Position.MAP_SIZE_TILES;
    }

    /**
     * Gets chunk x.
     *
     * @return the chunk x
     */
    public int getChunkX() {
        return getVector().getX() >> 3;
    }

    /**
     * Gets chunk y.
     *
     * @return the chunk y
     */
    public int getChunkY() {
        return getVector().getY() >> 3;
    }

    /**
     * Gets chunk x centered.
     *
     * @return the chunk x centered
     */
    public int getChunkXCentered() {
        return getChunkX() - 6;
    }

    /**
     * Gets chunk y centered.
     *
     * @return the chunk y centered
     */
    public int getChunkYCentered() {
        return getChunkY() - 6;
    }

    /**
     * Gets map offset x.
     *
     * @return the map offset x
     */
    public int getMapOffsetX() {
        return getVector().getX() % 104;
    }

    /**
     * Gets map offset y.
     *
     * @return the map offset y
     */
    public int getMapOffsetY() {
        return getVector().getY() % 104;
    }

    /**
     * Gets chunk x centered offset.
     *
     * @return the chunk x centered offset
     */
    public int getChunkXCenteredOffset() {
        return getVector().getX() - (getChunkXCentered() << 3);
    }

    /**
     * Gets chunk y centered offset.
     *
     * @return the chunk y centered offset
     */
    public int getChunkYCenteredOffset() {
        return getVector().getY() - (getChunkYCentered() << 3);
    }

    /**
     * Gets chunk x offset.
     *
     * @return the chunk x offset
     */
    public int getChunkXOffset() {
        return getVector().getX() - (getChunkX() << 3);
    }

    /**
     * Gets chunk y offset.
     *
     * @return the chunk y offset
     */
    public int getChunkYOffset() {
        return getVector().getY() - (getChunkY() << 3);
    }

    /**
     * Gets local x.
     *
     * @return the local x
     */
    //Figure out what local x and y are
    //figure out why map is centered with magic number 6
    public final int getLocalX() {
        return getVector().getX() - 8 * getChunkXCentered();
    }

    /**
     * Gets local y.
     *
     * @return the local y
     */
    public final int getLocalY() {
        return getVector().getY() - 8 * getChunkYCentered();
    }


    /**
     * Gets region x.
     *
     * @return the region x
     */
    public int getRegionX() {
        return getChunkX() >> 3;
    }

    /**
     * Gets region y.
     *
     * @return the region y
     */
    public int getRegionY() {
        return getChunkY() >> 3;
    }

    public Position getRegionPosition() {
        return new Position(new Vector(getRegionX(), getRegionY(), 0));
    }

    /**
     * Gets region offset x.
     *
     * @return the region offset x
     */
    public int getRegionOffsetX() {
        return getVector().getX() & (Position.REGION_SIZE_TILES - 1);
    }

    /**
     * Gets region offset y.
     *
     * @return the region offset y
     */
    public int getRegionOffsetY() {
        return getVector().getY() & (Position.REGION_SIZE_TILES - 1);
    }


    /**
     * Is in same region boolean.
     *
     * @param other the other
     * @return the boolean
     */
/*
       Checks if both positions are in the same map.
       A region is 64x64 tiles.
    */
    public boolean isInSameRegion(Position other) {
        return getRegionX() == other.getRegionX() && getRegionY() == other.getRegionY();
    }

    /**
     * Is in same chunk boolean.
     *
     * @param other the other
     * @return the boolean
     */
/*
         Checks if both positions are in the same map.
         A map is 8x8 tiles
    */
    public boolean isInSameChunk(Position other) {
        int thisChunkX = getChunkX();
        int thisChunkY = getChunkY();
        int otherChunkX = other.getChunkX();
        int otherChunkY = other.getChunkY();

        return thisChunkX == otherChunkX && thisChunkY == otherChunkY;
    }

    /**
     * Checks if the specified position is within numChunks number of chunks.
     *
     * @param numChunks the num chunks
     * @param other     the other
     * @return boolean
     */
    public boolean isWithinChunks(int numChunks, Position other) {
        int chunkX = getChunkX();
        int chunkY = getChunkY();
        int otherChunkX = other.getChunkX();
        int otherChunkY = other.getChunkY();

        return Math.abs(chunkX - otherChunkX) <= numChunks && Math.abs(chunkY - otherChunkY) <= numChunks;
    }

    @Override
    public int hashCode() {
        return getVector().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }

        Position p = (Position) obj;

        return getVector().equals(p.getVector());
    }

    /**
     * Is in same map boolean.
     *
     * @param other the other
     * @return the boolean
     */
/*
            Checks if both positions are in the same map.
            A map is 104x104 tiles.
        */
    public boolean isInSameMap(Position other) {
        int thisMapX = getMapX();
        int thisMapY = getMapY();
        int otherMapX = other.getMapX();
        int otherMapY = other.getMapY();

        return thisMapX == otherMapX && thisMapY == otherMapY;
    }

    /**
     * Is within x boolean.
     *
     * @param other the other
     * @param x     the x
     * @return the boolean
     */
    public boolean isWithinX(Position other, int x) {
        return Math.abs(distanceBetweenX(other)) <= x;
    }

    /**
     * Is within y boolean.
     *
     * @param other the other
     * @param y     the y
     * @return the boolean
     */
    public boolean isWithinY(Position other, int y) {
        return Math.abs(distanceBetweenY(other)) <= y;
    }

    /**
     * Is within z boolean.
     *
     * @param other the other
     * @param z     the z
     * @return the boolean
     */
    public boolean isWithinZ(Position other, int z) {
        return Math.abs(distanceBetweenZ(other)) <= z;
    }

    /**
     * Is within xy boolean.
     *
     * @param other the other
     * @param check the check
     * @return the boolean
     */
    public boolean isWithinXY(Position other, int check) {
        return isWithinX(other, check) && isWithinY(other, check);
    }

    /**
     * Distance between x int.
     *
     * @param other the other
     * @return the int
     */
    public int distanceBetweenX(Position other) {
        return v.getX() - other.getVector().getX();
    }

    /**
     * Distance between y int.
     *
     * @param other the other
     * @return the int
     */
    public int distanceBetweenY(Position other) {
        return v.getY() - other.getVector().getY();
    }

    /**
     * Distance between z int.
     *
     * @param other the other
     * @return the int
     */
    public int distanceBetweenZ(Position other) {
        return v.getZ() - other.getVector().getZ();
    }

    public boolean isInViewingDistance(Position other) {
        return isWithinXY(other, 15) && isWithinZ(other, 0);
    }

    public Position copy() {
        return new Position(v.copy());
    }
}
