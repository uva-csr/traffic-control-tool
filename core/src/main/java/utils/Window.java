package utils;

public class Window<T> {

    private final long windowLengthInMs;

    private long windowStart;

    private T value;


    public Window(long windowLengthInMs, long windowStart, T value) {
        this.windowLengthInMs = windowLengthInMs;
        this.windowStart = windowStart;
        this.value = value;
    }

    public long windowLength() {
        return windowLengthInMs;
    }

    public long windowStart() {
        return windowStart;
    }

    public T value() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Window<T> resetTo(long startTime) {
        this.windowStart = startTime;
        return this;
    }

    public boolean isTimeInWindow(long timeMillis) {
        return windowStart <= timeMillis && timeMillis < windowStart + windowLengthInMs;
    }

    @Override
    public String toString() {
        return "WindowWrap{" +
                "windowLengthInMs=" + windowLengthInMs +
                ", windowStart=" + windowStart +
                ", value=" + value +
                '}';
    }
}
