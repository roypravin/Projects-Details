package mvs.android;

import mvs.android.CommonData.LongOperation1;
import mvs.android.VoiceSearchActivity.SwipeGestureDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.text.Editable;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import mvs.android.CommonData.LongOperation2;

public class TextSearchActivity extends Activity implements SensorEventListener {

	EditText txtTextInput;
	Button btnSearch;
	ViewFlipper viewFlipper;
	
	CommonData.LongOperation2 lo;
	Handler h;
	Runnable r;
	String input_text = "";
	private Context mContext;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 20;
	private final GestureDetector detector = new GestureDetector(
			new SwipeGestureDetector());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_search);
		txtTextInput = (EditText) findViewById(R.id.editTextSerachInput);
		mContext = this;
		btnSearch = (Button) findViewById(R.id.buttonTextFind);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlippersearchtext);
		CommonData.progress = new ProgressDialog(this);

		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CommonData.progress.setMessage("Searching!!!");
				CommonData.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				CommonData.progress.setIndeterminate(true);
				CommonData.progress.show();

				CommonData.METHOD_NAME = "textsearch";
				CommonData.input_Image_Tag = txtTextInput.getText().toString();
				System.out.println("Input Text at !!!!!:"
						+ CommonData.input_Image_Tag);
				lo = new LongOperation2();
				lo.execute("");

			}
		});

		h = new Handler(Looper.getMainLooper());
		h.post(r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// System.out.println("In");
				if (CommonData.responseFromServer) {
					System.out.println("In true");
					viewFlipper.destroyDrawingCache();
					viewFlipper.removeAllViews();
					CommonData.responseFromServer = false;
					if (CommonData.return_images.size() > 0) {
						for (int i = 0; i < CommonData.return_images.size(); i++) {
							ImageView iv = new ImageView(
									TextSearchActivity.this);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);
							iv.setLayoutParams(params);
							Bitmap img = Bitmap.createBitmap(
									CommonData.return_images.elementAt(i).img,
									640, 480, Config.ARGB_8888);
							iv.setImageBitmap(img);
							viewFlipper.addView(iv, i);
							// System.out.println("adding view...");
							// ImageSearchActivity.this.addContentView(iv,
							// params);
						}
						CommonData.progress.dismiss();
						Toast.makeText(getApplicationContext(),
								"Scroll Down To View Content",
								Toast.LENGTH_SHORT).show();
						CommonData.progress.dismiss();
						CommonData.progress.cancel();
					}else {
						CommonData.progress.dismiss();
						CommonData.progress.cancel();
						Toast.makeText(getApplicationContext(),
								"No Results Found For Input Query",
								Toast.LENGTH_SHORT).show();
					}

				}
				h.postAtTime(r, SystemClock.uptimeMillis() + 500);

			}

		});

		viewFlipper.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				detector.onTouchEvent(event);
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_search, menu);
		return true;
	}

	class SwipeGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.in_from_right));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.out_to_left));
					// System.out.println("In if");
					viewFlipper.showNext();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.in_from_left));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.out_to_right));
					// System.out.println("In else");
					viewFlipper.showPrevious();
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

	}

}
