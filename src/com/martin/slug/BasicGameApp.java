package com.martin.slug;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.app.DSLKt.loopBGM;
import static com.almasb.fxgl.app.DSLKt.run;

public class BasicGameApp extends GameApplication {

    // This block sets up the basic settings for the game (height, width, version and the menu)
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Cosby Hunter");
        settings.setVersion("0.1");
        settings.setMenuEnabled(true);
        settings.setMenuKey(KeyCode.ESCAPE);
    }

    public enum EntityType {
        BULLET, ENEMY
    }


    public Entity player;
    public PlayerControl playerControl;


    // Here the parameters for the game are set, the enemy spawn timer is set and the music loop is started
    @Override
    protected void initGame(){

        initPlayer();
        run(() -> spawnEnemy(), Duration.seconds(2));

        loopBGM("fortunateson.wav");

    }

    // Here a few variables are set to 1 or 0 to initialize the game
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
        vars.put("score", 0);
        vars.put("level", 0);
        vars.put("lives", 1);
        vars.put("enemiesKilled", 0);
    }



    // Here the player entity is built and given parameters
    public void initPlayer() {
        player = new Entity();
        player.getPositionComponent().setValue(getWidth() / 20, getHeight() / 3);
        player.setViewFromTexture("gamesprite.png");

        WeaponComponent weapon = new WeaponComponent();
        weapon.setDamage(2);
        weapon.setFireRate(1.0);
        weapon.setMaxAmmo(10);

        player.addComponent(weapon);

        playerControl = new PlayerControl();
        player.addComponent(playerControl);

        getGameWorld().addEntity(player);
    }

    // Here the input methods for the game are defined
    @Override
    protected void initInput(){
        Input input = getInput();


        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction(){
                player.translateX(5);
                getGameState().increment("pixelsMoved", +5);
            }

        }, KeyCode.D);

        // This method allows the user to use the mouse to aim while shooting
        input.addAction(new UserAction("Shoot Space") {
            @Override
            protected void onAction(){
                Point2D p = getInput().getMousePositionWorld();
                playerControl.shoot(p);
            }

        }, KeyCode.SPACE);


        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.translateX(-5); // move left 5 pixels
                getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                player.translateY(-5); // move up 5 pixels
                getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                player.translateY(5); // move down 5 pixels
                getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.S);


    }

    // This block defines the physics of the game, adds audio to hits and increments the score of the player after a hit
    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity enemy) {
                BulletComponent bulletData = bullet.getComponent(BulletComponent.class);

                bulletData.setHp(bulletData.getHp() - 1);

                HPComponent hp = enemy.getComponent(HPComponent.class);
                hp.decrement(bulletData.getDamage() + player.getComponent(WeaponComponent.class).getDamage());
                if (hp.getValue() <= 0)
                    enemy.removeFromWorld();
                    getAudioPlayer().playSound("oof.wav");
                    getGameState().increment("score", +1);



                if (bulletData.getHp() <= 0)
                    bullet.removeFromWorld();
            }
        });
    }

    // This block makes the enemy move towards the player
    @Override
    protected void onUpdate(double tpf) {
        getGameWorld().getEntitiesByType(EntityType.ENEMY).forEach(enemy -> enemy.translateX(-100 * tpf));


    }

    @OnUserAction(name = "Shoot", type = ActionType.ON_ACTION_BEGIN)
    public void shoot() {
        playerControl.shoot(getInput().getVectorToMouse(player.getPositionComponent().getValue()));
    }

    // This block defines the parameters for the enemy
    private void spawnEnemy() {
        Entity enemy = new Entity();
        enemy.getTypeComponent().setValue(EntityType.ENEMY);
        enemy.getPositionComponent().setValue(550,FXGLMath.random(25,450));
        enemy.getViewComponent().setTexture("cosbyboss.png", true);

        enemy.addComponent(new CollidableComponent(true));
        enemy.addComponent(new HPComponent(5));
        enemy.addComponent(new EnemyControl(new Point2D(getWidth() / 2, getHeight() / 2)));

        getGameWorld().addEntity(enemy);
    }


    // This block sets up the UI for the game, background and score
    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(25); // x = 50
        textPixels.setTranslateY(25); // y = 100
        getGameScene().setBackgroundRepeat("background.png");
        Text textScore = getUIFactory().newText("", Color.BLACK, 22);

        textScore.setTranslateX(10);
        textScore.setTranslateY(50);

        textScore.textProperty().bind(getGameState().intProperty("score").asString());

        textPixels.textProperty().bind(getGameState().intProperty("pixelsMoved").asString());

        getGameScene().addUINode(textScore);
    }


    // Launcher for the main class of the game
    public static void main(String[] args) {
        launch(args);
    }
}
