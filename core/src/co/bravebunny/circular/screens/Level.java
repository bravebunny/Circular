package co.bravebunny.circular.screens;

import co.bravebunny.circular.Circular;
import co.bravebunny.circular.Circular.State;
import co.bravebunny.circular.Circular.CurrentScreen;
import co.bravebunny.circular.managers.Assets;
import co.bravebunny.circular.managers.GameInput;
import co.bravebunny.circular.managers.Particles;
import co.bravebunny.circular.objects.multiple.Enemy;
import co.bravebunny.circular.objects.single.Circle;
import co.bravebunny.circular.objects.single.HUD;
import co.bravebunny.circular.objects.single.Score;
import co.bravebunny.circular.objects.single.Ship;
import co.bravebunny.circular.objects.single.Ship.ShipState;
import co.bravebunny.circular.objects.Solid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Level extends Common implements Screen {
	//objects
	private static Music music;
	
	//values
	private static float bpm = 107;
	private static float time = 0;
	public static int score = 0;
	
	//groups (object layers)
	public static Group layerGame = new Group();
	public static Group layerShip = new Group();
	public static Group layerObjects = new Group();
	public static Group layerOverlay = new Group();
	public static Group layerHUD = new Group();
	
	//other objects
	public static Ship ship;
	public static Circle circle;
	public static Array<Enemy> enemies = new Array<Enemy>();
	
    public static float getBPM() {
    	return bpm;
    }
	
    @Override
    public void render(float delta) {
    	super.render(delta);
    }
    
    @Override
    public void resize(int width, int height) {    
    	super.resize(width, height);
    }

    @Override
    public void show() {
    	screen = CurrentScreen.LEVEL;
    	
    	bgRed = 0;
    	bgGreen = 89;
    	bgBlue = 118;
    	stage = new Stage();
    	//time = TimeUtils.millis();
    	
    	super.show();
    	
    	circle = new Circle();
    	circle.setLayer(layerGame);
    	ship = new Ship();
    	
        Score.show();
        Particles.show();
        HUD.show();
        
        getStage().addActor(layerGame);
        getStage().addActor(layerShip);
        getStage().addActor(layerObjects);
        getStage().addActor(layerOverlay);
        getStage().addActor(layerHUD);
        
    	//initialize input
    	GameInput input = new GameInput(this);
    	Gdx.input.setInputProcessor(input);
    	
    	//start music
    	music = Gdx.audio.newMusic(Gdx.files.internal("media/music/music1.ogg"));
    	//music = Assets.getMusic("music1");
    	//I SHOULD BE USING THE ASSET MANAGER HERE
    	//I'M NOT, THOUGH
    	//WHY ARE WE YELLING
    	music.play();
    	
    	ship.state = ShipState.ALIVE;


    }

    @Override
    public void hide() {
    	
    }

    @Override
    public void pause() {
    	music.pause();
    	super.pause();
    }

    @Override
    public void resume() {
    	music.play();
    	super.resume();
    }

    @Override
    public void dispose() {
    	super.dispose();
    	Particles.dispose();
    }
    
    public void renderRun(float delta) {
    	if (music.isPlaying()) {
	    	Particles.render(delta);
	    	ship.render(delta);
	    	circle.render(delta);
	    	Score.render(delta);
	    	HUD.render(delta);
	    	
	    	for (int i = 0; i < enemies.size; i++) {
	    		if (enemies.get(i).isDead()) {
	    			enemies.removeIndex(i);
	    		} else {
	    			enemies.get(i).render(delta);
	    			if (ship.collidesWith(enemies.get(i))) {
		    			enemies.removeIndex(i);
		    			enemies.get(i).explode();
		    			ship.destroy();
		    			circle.grow();
		    		}
	    		}
	    	}
	        
	    	time += delta;
	        if (time >= 60/bpm) {
	        	if (ship.state == ShipState.ALIVE) {
	        		//call all the rhythm related stuff
	            	rhythm();
	        	}
	            time -= 60/bpm;
	        }
        
        }
    }
    
    public void renderPause(float delta) {
    }
    
    public static void restart() {
    	if (HUD.restart.getScaleX() >= 1) {
        	circle.shrink();
        	HUD.restartHide();
        	score = 0;
        	
        	Timer.schedule(new Task(){
        	    @Override
        	    public void run() {
        	    	ship.reset();
        	    	ship.moveUp();
        	    }
        	}, 60/Level.getBPM());
    	}
    }
    
    
    //events that happen every beat
    public void rhythm() {
		Enemy enemy = new Enemy();
		enemy.setRotation(ship.getRotation() + 180);
		enemies.add(enemy);
		circle.beat();
		Score.inc();
		//enemy.beat();

    }

	@Override
	public void touchDown(int screenX, int screenY) {
		if (ship.state == Ship.ShipState.ALIVE) {
			ship.moveDown();
		} else {
			restart();
		}
		
	}

	@Override
	public void touchUp(int screenX, int screenY) {
		switch (ship.state)
        {
        case ALIVE:
        	ship.moveUp();
            break;
        case DEAD:
        	
            break;
        }
		
	}

	@Override
	public void backKey() {
		// TODO Auto-generated method stub
		
	}

}
