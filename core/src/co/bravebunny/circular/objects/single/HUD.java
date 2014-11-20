package co.bravebunny.circular.objects.single;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;
import co.bravebunny.circular.managers.ActorAccessor;
import co.bravebunny.circular.managers.Assets;
import co.bravebunny.circular.managers.Positions;
import co.bravebunny.circular.screens.Common;
import co.bravebunny.circular.screens.Level;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class HUD {
	public static Image restart = Assets.getImage("level/hud_restart");
	
	public static void show() {
		Positions.setPolarPosition(restart);
	    Level.layerHUD.addActor(restart);
	    restart.setScale(0, 0);
	    restart.setVisible(false);
	}
	
	public static void render(float delta) {
		restart.rotateBy(delta*30);
	}
	
	//Grows the restart symbol into sight
	public static void restartShow() {
		restart.setVisible(true);
		Tween.to(restart, ActorAccessor.SCALE, 60/Level.getBPM())
        .target(1.0f).ease(Back.OUT).delay(60/Level.getBPM())
        .start(Common.getTweenManager());
		
	}
	
	//Shrinks the restart symbol back 
	public static void restartHide() {
		Tween.to(restart, ActorAccessor.SCALE, 60/Level.getBPM())
        .target(0.0f).ease(Back.IN)
        .start(Common.getTweenManager());
		
		Timer.schedule(new Task(){
    	    @Override
    	    public void run() {
    	    	restart.setVisible(false);
    	    }
    	}, 60/Level.getBPM());
		
		
	}
	
}



