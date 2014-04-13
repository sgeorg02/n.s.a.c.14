package com.example.spaceapps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

	public class SplashScreen extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_splash_screen);

			Thread timer = new Thread() {
				public void run() {
					try {
						sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Intent i = new Intent(SplashScreen.this, MainActivity.class);
					startActivity(i);
					finish();
				}
			};
			timer.start();
		}

}
