package mvs.android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import mvs.android.CommonData.LongOperation;

public class ImageSearchActivity extends Activity {
	Bitmap bitmap;

	
	private static int RESULT_LOAD_IMAGE = 1;
	Button btnLoadImage, btnFind;
	ImageView inputImage;
	Bitmap bmp2;
	CommonData.LongOperation lo;
	Bitmap resized;
	Handler h;
	Runnable r;
	ViewFlipper viewFlipper;
	public Uri fileUri; // file url to store image/video
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	float lastX, currentX;
	private Context mContext;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 20;
	private final GestureDetector detector = new GestureDetector(
			new SwipeGestureDetector());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);
		inputImage = (ImageView) findViewById(R.id.imageViewInputImage);
		btnLoadImage = (Button) findViewById(R.id.buttonUploadImage);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlippersearchImages);
		btnFind = (Button) findViewById(R.id.buttonFind);

		CommonData.progress = new ProgressDialog(this);
		mContext = this;

		btnFind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonData.progress.setMessage("Searching!!!");
				CommonData.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				CommonData.progress.setIndeterminate(true);
				CommonData.progress.show();

				CommonData.METHOD_NAME = "receiveimagerequest";

				lo = new LongOperation();
				lo.execute("");

			}
		});

		viewFlipper.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				detector.onTouchEvent(event);
				return true;
			}
		});
		h = new Handler(Looper.getMainLooper());
		h.post(r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// System.out.println("In");
				if (CommonData.responseFromServer) {
					// System.out.println("In true");

					viewFlipper.destroyDrawingCache();
					viewFlipper.removeAllViews();
					CommonData.responseFromServer = false;
					if (CommonData.return_images.size() > 0) {
						for (int i = 0; i < CommonData.return_images.size(); i++) {
							ImageView iv = new ImageView(
									ImageSearchActivity.this);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);
							iv.setLayoutParams(params);
							Bitmap img = Bitmap.createBitmap(
									CommonData.return_images.elementAt(i).img,
									640, 480, Config.ARGB_8888);
							iv.setImageBitmap(img);
							viewFlipper.addView(iv, i);
							System.out.println("adding view...");
							// ImageSearchActivity.this.addContentView(iv,
							// params);
						}
						CommonData.progress.dismiss();
						Toast.makeText(getApplicationContext(),
								"Scroll Down To View Content",
								Toast.LENGTH_SHORT).show();
						CommonData.progress.cancel();
					} else {
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

		btnLoadImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("Inside usdsjd");
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_search, menu);

		return true;
	}

	public void resizeImage(Bitmap orgbmp)

	{

		resized = Bitmap.createScaledBitmap(orgbmp, 640, 480, true);
		inputImage.setImageBitmap(resized);
		// resized.getPixels(CommonData.si.img, 0, 640, 0, 0, 640, 480);

		for (int i = 0; i < 5; i++) {
			System.out
					.println("Value of img  after resize at android:@@@@@@@@@!!!!!"
							+ CommonData.si.img[i]);
		}

		System.out.println("Re Height" + resized.getHeight());
		System.out.println("re Width" + resized.getWidth());
		// resized.getPixels(CommonData.si.img, 0, 640, 0, 0, 640, 480);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		String selectedImagePath;
		System.out.println("REQ CODE :" + requestCode);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			// ImageView imageView = (ImageView) findViewById(R.id.imageshare2);
			// inputImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

			bmp2 = BitmapFactory.decodeFile(picturePath);
			// System.out.println("BMP Height" + bmp2.getHeight());
			// System.out.println("BMP Width" + bmp2.getWidth());

			int ww = bmp2.getWidth();
			int hh = bmp2.getHeight();
			CommonData.si.width = ww;
			CommonData.si.height = hh;
			CommonData.si.img = new int[ww * hh];
			// System.out.println("Height in load@@@@@@@@@"+hh);
			// System.out.println("Width in load@@@@@@@@@"+ww);
			// System.out.println("Image in Size@@@@@@@@@@@@@@"+(ww*hh));
			bmp2.getPixels(CommonData.si.img, 0, ww, 0, 0, ww, hh);

			resizeImage(bmp2);
			// inputImage.setImageBitmap(bmp2);
			// ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// bmp2.compress(Bitmap.CompressFormat.PNG, 100, stream);
			// share2 = stream.toByteArray();

		}

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
					System.out.println("In if");
					viewFlipper.showNext();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.in_from_left));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.out_to_right));
					System.out.println("In else");
					viewFlipper.showPrevious();
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}
}
