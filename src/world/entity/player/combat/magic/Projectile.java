package world.entity.player.combat.magic;

public class Projectile {
    private int id;
    private int startHeight;
    private int endHeight;
    private int curve;
    private int speed;
    private int delay;

    public Projectile() {

    }

    public Projectile(int id, int startHeight, int endHeight, int curve, int speed, int delay) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartHeight() {
        return startHeight;
    }

    public int getEndHeight() {
        return endHeight;
    }

    public int getCurve() {
        return curve;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDelay() {
        return delay;
    }
}
