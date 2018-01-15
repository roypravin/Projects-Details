package mvs.android;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMeuActivity extends Activity {
	Button btnImageSearch, btnVoiceSearch, btnTextSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_meu);
		btnImageSearch = (Button) findViewById(R.id.btnImageSearch);
		btnVoiceSearch = (Button) findViewById(R.id.btnVoiceSearch);
		btnTextSearch = (Button) findViewById(R.id.btnTextSearch);
		
		btnImageSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ii = new Intent(getApplicationContext(), ImageSearchActivity.class);
				startActivity(ii);
			}
		});

		btnVoiceSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ii = new Intent(getApplicationContext(), VoiceSearchActivity.class);
				startActivity(ii);
			}
		});
		btnTextSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent ii = new Intent(getApplicationContext(), TextSearchActivity.class);
				startActivity(ii);
			}
		});

	}

}
