package ch.ethz.inf.vs.a1.fesslerl.antitheft;

public class SpikeMovementDetector extends AbstractMovementDetector {

    private boolean isTriggered = false;

    public SpikeMovementDetector(AlarmCallback callback, int sensitivity) {
        super(callback, sensitivity);
    }

    @Override
    public boolean doAlarmLogic(float[] values) {
        int sum = 0;
        for (float value : values)
            sum += Math.abs(value);

        if (isTriggered)
            return false;

        isTriggered = sum >= sensitivity;
        return isTriggered;
    }
}
