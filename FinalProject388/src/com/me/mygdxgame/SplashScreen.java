package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen,ApplicationListener
{
        private SpriteBatch spriteBatch;
        private Texture splsh;
        private MyGdxGame myGame;
        
        /**
         * Constructor for the splash screen
         * @param myGdxGame Game which called this splash screen.
         */
        public SplashScreen(MyGdxGame myGdxGame)
        {
                myGame = myGdxGame;
        }


		@Override
        public void render(float delta)
        {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                spriteBatch.begin();
                spriteBatch.draw(splsh, 0, 0);
                spriteBatch.end();
                
                if(Gdx.input.justTouched()){
                	 myGame.setScreen(myGame.getGame());
                }
                       
        }
        
        @Override
        public void show()
        {
                spriteBatch = new SpriteBatch();
                splsh = new Texture(Gdx.files.internal("data/splash2048.png"));
                
        }

		@Override
		public void resize(int width, int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resume() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void create() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void render() {
			// TODO Auto-generated method stub
			
		}
}