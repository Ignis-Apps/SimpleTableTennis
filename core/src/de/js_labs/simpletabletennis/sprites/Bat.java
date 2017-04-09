package de.js_labs.simpletabletennis.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.screens.PlayScreen;

/**
 * Created by Janik on 16.05.2016.
 */
public class Bat extends Sprite {
    public static final int MAX_DOWN_ANGLE = -10;
    public static final int ROTATION_VELOCITY_ON_HIT = 5;

    private PlayScreen screen;
    private World world;
    private Body body;
    private Texture batTexture;

    private float rotation;
    private float rotationVelocity;
    private Vector2 position;

    public Bat(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        batTexture = new Texture(Gdx.files.internal("images/in_game/bat.png"));

        rotation = MAX_DOWN_ANGLE;
        rotationVelocity = 0;

        setBounds(0, 0, 100 / SimpleTableTennis.PPM, 8 / SimpleTableTennis.PPM);
        setOrigin(10 / SimpleTableTennis.PPM, 4 / SimpleTableTennis.PPM);
        setRotation(rotation);
        setRegion(new TextureRegion(batTexture));

        position = new Vector2(1, 1);
        defineBat(rotation);
    }

    private void defineBat(float rotation){
        if (body != null){
            world.destroyBody(body);
            body = null;
        }
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x, position.y);
        bodyDef.angle = (float) (rotation * SimpleTableTennis.DEGREES_TO_RADIANS);
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(90, 4).scl(1 / SimpleTableTennis.PPM);
        vertice[1] = new Vector2(-10, 4).scl(1 / SimpleTableTennis.PPM);
        vertice[2] = new Vector2(80, -104).scl(1 / SimpleTableTennis.PPM);
        vertice[3] = new Vector2(0, -104).scl(1 / SimpleTableTennis.PPM);
        shape.set(vertice);

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = SimpleTableTennis.BAT_BIT;
        body.createFixture(fixtureDef).setUserData(screen);
    }

    public void update(float dt){

        //Gdx.app.log("Test", "Bat Updating ...");
        setPosition(position.x - getWidth() / 10, position.y - getHeight() / 2);
        rotationVelocity -= dt*20;
        if(rotation > MAX_DOWN_ANGLE || (rotation == MAX_DOWN_ANGLE) && rotationVelocity > 0){
            rotation += rotationVelocity;
        }else {
            rotation = MAX_DOWN_ANGLE;
            rotationVelocity = 0;
        }
        body.setTransform(new Vector2(position.x, position.y), (float) (rotation * SimpleTableTennis.DEGREES_TO_RADIANS));
        setRotation(rotation);

        //Gdx.app.log("Test", "Bat Updated");
    }

    public void setPosition(Vector2 position){
        this.position = position;
    }

    public void hit(){
        if(rotation <= 0)
            rotationVelocity = ROTATION_VELOCITY_ON_HIT;
    }
}
