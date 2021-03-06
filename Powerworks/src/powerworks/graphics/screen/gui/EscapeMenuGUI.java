package powerworks.graphics.screen.gui;

import java.util.Arrays;
import powerworks.graphics.Image;
import powerworks.main.Game;
import powerworks.main.State;
import powerworks.task.Task;
import powerworks.main.Setting;

public class EscapeMenuGUI extends GUI {

    private GUITexturePane background;
    private GUIButton mainMenu, exit, options, resume;
    private GUIGroup buttons;

    public EscapeMenuGUI() {
	super(0, 0, 2);
	int w = Game.getScreenWidth();
	int h = Game.getScreenHeight();
	
	background = new GUITexturePane(this, 0, 0, w, h, 3, Image.ESCAPE_MENU_BACKGROUND);
	mainMenu = new GUIButton(this, 0, 0, 4, "Back to Main Menu", true, new Task() {

	    @Override
	    public void run() {
	    }
	}, new Task() {

	    @Override
	    public void run() {
		close();
		State.setState(State.MAIN_MENU);
	    }
	});
	options = new GUIButton(this, 0, 0, 4, "Options", true, new Task() {

	    @Override
	    public void run() {
	    }
	}, new Task() {

	    @Override
	    public void run() {
		close();
		Game.getEscapeOptionsMenuGUI().open();
	    }
	});
	exit = new GUIButton(this, 0, 0, 4, "Exit", true, new Task() {

	    @Override
	    public void run() {
	    }
	}, new Task() {

	    @Override
	    public void run() {
		Game.exit();
	    }
	});
	resume = new GUIButton(this, 0, 0, 4, "Resume", true, new Task() {

	    @Override
	    public void run() {
		close();
		if(Setting.PAUSE_IN_ESCAPE_MENU.getValue()) {
		    Game.setPaused(false);
		}
	    }
	}, new Task() {

	    @Override
	    public void run() {
	    }
	});
	buttons = new AutoFormatGUIGroup(this, 0, 0, 3, 0, 0, Arrays.asList(resume, mainMenu, options, exit));
	buttons.setRelXPixel((w - buttons.getWidthPixels()) / 2);
	buttons.setRelYPixel((h - buttons.getHeightPixels()) / 2);
    }
}
