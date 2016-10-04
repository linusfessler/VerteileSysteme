package antitheft.nethz.a1.vs.inf.ethz.ch.vs_fesslerl_antitheft;

import ch.ethz.inf.vs.a1.nethz.antitheft.AlarmCallback;

public class SpikeMovementDetector extends AbstractMovementDetector {

    public SpikeMovementDetector(AlarmCallback callback, int sensitivity) {
        super(callback, sensitivity);
    }

    @Override
    public boolean doAlarmLogic(float[] values) {
		// TODO, insert your logic here
        return false;
    }
}
