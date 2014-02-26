package com.me.medievaltd;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by user1 on 2/26/14.
 */
public class Drop implements ApplicationListener {
    Texture dropImage;
    Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    SpriteBatch batch;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
    @Override
    public void create() {
        raindrops = new Array<Rectangle>();
        spawnRaindrop();
        dropImage = new Texture(Gdx.files.internal("Images/droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("Images/bucket.png"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("Sound/waterdropSound.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/rainMusic.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480,800);
        batch = new SpriteBatch();
        bucket = new Rectangle();
        bucket.x = 800/2 - 64/20;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
    }

    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void render() {
Gdx.gl.glClearColor(0,0,0.6f,1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for(Rectangle raindrop: raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.draw(bucketImage, bucket.x, bucket.y);
        batch.end();
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;

        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
        if(bucket.x < 0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;
        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y + 64 < 0) iter.remove();
            if(raindrop.overlaps(bucket)) {
                dropSound.play();
                iter.remove();
            }
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

            dropImage.dispose();
            bucketImage.dispose();
            dropSound.dispose();
            rainMusic.dispose();
            batch.dispose();

    }
}
