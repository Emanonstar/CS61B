package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int state;
    private int period;
    private double acceleratingFactor;

    public AcceleratingSawToothGenerator(int period, double acceleratingFactor) {
        state = 0;
        this.period = period;
        this.acceleratingFactor = acceleratingFactor;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        if (state == 0) {
            period = (int) (period * acceleratingFactor);
        }
        return -1.0 + state * 2.0 / period;
    }
}
