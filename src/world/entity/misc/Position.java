package world.entity.misc;

/**
 * The type Position.
 */
public class Position {
    private static final int MAP_SIZE_TILES = 104;
    private static final int REGION_SIZE_TILES = 64;
    private static final int CHUNK_SIZE_TILES = 64;
    private final Vector v;

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

    public int getMapX() {
        return getVector().getX() / Position.MAP_SIZE_TILES;
    }

    public int getMapY() {
        return getVector().getY() / Position.MAP_SIZE_TILES;
    }

    public int getMapOffsetX() {
        return getVector().getX() % Position.MAP_SIZE_TILES;
    }

    public int getMapOffsetY() {
        return getVector().getY() % Position.MAP_SIZE_TILES;
    }

    public int getMapSizeY() {
        return getVector().getY() % Position.MAP_SIZE_TILES;
    }

    public int getChunkX() {
        return getVector().getX() >> 3;
    }

    public int getChunkY() {
        return getVector().getY() >> 3;
    }

    public int getChunkOffsetX() {
        return getVector().getX() % Position.CHUNK_SIZE_TILES;
    }

    public int getChunkOffsetY() {
        return getVector().getY() % Position.CHUNK_SIZE_TILES;
    }

    public int getChunkXCentered() {
        return getChunkX() - 6;
    }

    public int getChunkYCentered() {
        return getChunkY() - 6;
    }

    public int getRegionX() {
        return getChunkX() >> 3;
    }

    public int getRegionY() {
        return getChunkY() >> 3;
    }

    public int getRegionOffsetX() {
        return getVector().getX() & (Position.REGION_SIZE_TILES - 1);
    }

    public int getRegionOffsetY() {
        return getVector().getY() & (Position.REGION_SIZE_TILES - 1);
    }

    public int getChunkXOffsetFromRegion() {
        return getRegionOffsetX() >> 3;
    }

    public int getChunkYOffsetFromRegion() {
        return getRegionOffsetY() >> 3;
    }

    public int getChunkXOffsetFromMap() {
        return getMapOffsetX() >> 3;
    }

    public int getChunkYOffsetFromMap() {
        return getMapOffsetY() >> 3;
    }

    public boolean isInCenterOfMap() {
        return getChunkXOffsetFromMap() == 7 && getChunkYOffsetFromMap() == 7;
    }

    /*
       Checks if both positions are in the same map.
       A region is 64x64 tiles.
    */
    public boolean isInSameRegion(Position other) {
        return getRegionX() == other.getRegionX() && getRegionY() == other.getRegionY();
    }

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
     * @param numChunks
     * @param other
     * @return
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

    public boolean isWithinX(Position other, int x) {
        return Math.abs(distanceBetweenX(other)) <= x;
    }

    public boolean isWithinY(Position other, int y) {
        return Math.abs(distanceBetweenX(other)) <= y;
    }

    public boolean isWithinZ(Position other, int z) {
        return Math.abs(distanceBetweenZ(other)) <= z;
    }

    public boolean isWithinXY(Position other, int check) {
        return isWithinX(other, check) && isWithinY(other, check);
    }

    public int distanceBetweenX(Position other) {
        return v.getX() - other.getVector().getX();
    }

    public int distanceBetweenY(Position other) {
        return v.getY() - other.getVector().getY();
    }

    public int distanceBetweenZ(Position other) {
        return v.getY() - other.getVector().getY();
    }
}
