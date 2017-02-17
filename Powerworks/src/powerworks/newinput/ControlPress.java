package powerworks.newinput;

import powerworks.newinput.ControlOption;

public abstract class ControlPress {
    ControlPressType type;
    ControlOption option;
    
    protected ControlPress(ControlPressType type, ControlOption option) {
	this.type = type;
	this.option = option;
    }
    
    public ControlPressType getType() {
	return type;
    }
    
    public ControlOption getOption() {
	return option;
    }
}