package mvs.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				// Start your app main activity
				Intent i = new Intent(WelcomeActivity.this, MainMeuActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, 3000);

	}

}
