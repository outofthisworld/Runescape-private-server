package world.definitions.types;

public class Graphic {
    private int graphicId;
    private int height;


    public Graphic(int id, int height) {
        this.graphicId = id;
        this.height = height;
    }

    public Graphic(int id) {
        this(id, 0);
    }

    public int getGraphicId() {
        return graphicId;
    }

    public int getHeight() {
        return height;
    }
}
