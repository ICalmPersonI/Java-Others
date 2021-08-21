package converter;

public class Base {
    private final int source;
    private final int target;

    Base(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }
}
