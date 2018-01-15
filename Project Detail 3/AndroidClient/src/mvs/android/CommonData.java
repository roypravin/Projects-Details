package mvs.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import LibPack.SingleImage;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class CommonData {

	private static final String NAMESPACE = "http://MyServerPack/";
	private static final String URL = "http://192.168.1.40"
			+ ":8080/MVSServer/MSVService?wsdl";;
	private static final String SOAP_ACTION = "MSVService";
	public static String METHOD_NAME = " ";
	// public static int imgarray[]=new int[640*480];
	public static SingleImage si = new SingleImage();
	public static Vector<SingleImage> return_images = new Vector<SingleImage>();
	public static boolean responseFromServer = false;
	public static String input_Image_Tag;
	public static ProgressDialog progress;

	public static class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// METHOD_NAME = "videoStreamForAndroid";
			// response = false;

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			PropertyInfo pi = new PropertyInfo();
			pi.setName("fromClient");
			pi.setValue(objectToString(si));
			System.out.println("Size of image aerray in long" + si.img.length);
			pi.setType(String.class);
			request.addProperty(pi);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
				String resp = resultsRequestSOAP
						.getPrimitivePropertyAsString("return");

				Thread.sleep(100);

				return_images = (Vector<SingleImage>) stringToObject(resp);
				System.out.println("Size of return_Images#############"
						+ return_images.size());
				responseFromServer = true;

			} catch (Exception e) {
				System.out.println("Error in reading device details data");
				e.printStackTrace();
			}
			// if (resp != null) {
			// System.out.println("Returning response for " + METHOD_NAME);
			// //response = true;
			// }
			return "";

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (CommonData.return_images.size() > 0) {
				progress.dismiss();
				progress.cancel();
			} else {
				progress.dismiss();
				progress.cancel();
			}
		}

	}

	public static class LongOperation1 extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// METHOD_NAME = "videoStreamForAndroid";
			// response = false;

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			PropertyInfo pi = new PropertyInfo();
			pi.setName("fromClient");
			pi.setValue((input_Image_Tag));
			// System.out.println("Size of image aerray in long"+si.img.length);
			pi.setType(String.class);
			request.addProperty(pi);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
				String resp = resultsRequestSOAP
						.getPrimitivePropertyAsString("return");

				Thread.sleep(100);

				return_images = (Vector<SingleImage>) stringToObject(resp);
				System.out.println("Size of return_Images#############"
						+ return_images.size());
				responseFromServer = true;

			} catch (Exception e) {
				System.out.println("Error in reading device details data");
				e.printStackTrace();
			}
			// if (resp != null) {
			// System.out.println("Returning response for " + METHOD_NAME);
			// //response = true;
			// }
			return "";

		}
		
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (CommonData.return_images.size() > 0) {
				progress.dismiss();
				progress.cancel();
			} else {
				progress.dismiss();
				progress.cancel();
			}
		}

	}

	public static class LongOperation2 extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// METHOD_NAME = "videoStreamForAndroid";
			// response = false;

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			PropertyInfo pi = new PropertyInfo();
			pi.setName("fromClient");
			pi.setValue((input_Image_Tag));
			// System.out.println("Size of image aerray in long"+si.img.length);
			pi.setType(String.class);
			request.addProperty(pi);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
				String resp = resultsRequestSOAP
						.getPrimitivePropertyAsString("return");

				Thread.sleep(100);

				return_images = (Vector<SingleImage>) stringToObject(resp);
				System.out.println("Size of return_Images#############"
						+ return_images.size());
				responseFromServer = true;

			} catch (Exception e) {
				System.out.println("Error in Processing Image Search");
				e.printStackTrace();
			}
			// if (resp != null) {
			// System.out.println("Returning response for " + METHOD_NAME);
			// //response = true;
			// }
			return "";

		}
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (CommonData.return_images.size() > 0) {
				progress.dismiss();
				progress.cancel();
			} else {
				progress.dismiss();
				progress.cancel();
			}
		}

	}

	public static String objectToString(Object obj) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			b = bos.toByteArray();
		} catch (Exception e) {
			System.out.println("NOT SERIALIZABLE: " + e);
		}
		return Base64.encode(b);
	}

	public static Object stringToObject(String inp) {
		byte b[] = Base64.decode(inp);
		Object ret = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(b);
			ObjectInput in = new ObjectInputStream(bis);
			ret = (Object) in.readObject();
			bis.close();
			in.close();
		} catch (Exception e) {
			System.out.println("NOT DE-SERIALIZABLE: " + e);
		}
		return ret;
	}

}
