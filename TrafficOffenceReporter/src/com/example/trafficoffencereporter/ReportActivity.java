package com.example.trafficoffencereporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ReportActivity extends Activity {

	String vehicleType = "";
	String offence = "";
	Uri fileUrl;
	String fileUriStr;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		// Show the Up button in the action bar.
		setupActionBar();
		Bundle i = this.getIntent().getExtras();
		fileUriStr = i.getString("imageURL");
		fileUrl = Uri.parse(i.getString("imageURL"));
		offence =  i.getString("OffenceType");
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report, menu);
		return true;
	}

	public void onRadioButtonClicked(View v){
		RadioButton r = (RadioButton) v;
		switch(v.getId()){
			case R.id.radiominibus:
				vehicleType = "Minibus";
				break;
			case R.id.radiobakkie:
				vehicleType = "Bakkie";
				break;
			case R.id.radiobus:
				vehicleType = "Bus";
				break;
			case R.id.radiomotorcar:
				vehicleType = "Motorcar";
				break;
			case R.id.radioother:
				vehicleType = "Other";
				break;
			case R.id.radiotaxi:
				vehicleType = "Minibus taxi";
				break;
			case R.id.radiotruck:
				vehicleType = "Truck";
				break;
			case R.id.radiomotorcycle:
				vehicleType = "Motorcycle";
				break;
		}
		
		Toast.makeText(getBaseContext(),vehicleType,Toast.LENGTH_SHORT).show();
	}
	
	public void submitIncident(View v){
		EditText registration = (EditText)findViewById(R.id.veh_registration);
		EditText description = (EditText)findViewById(R.id.veh_desc);
		Toast.makeText(getBaseContext(),offence,Toast.LENGTH_SHORT).show();
		
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

	    try {
	        // Add your data
	    	
	    	
	    	BitmapFactory.Options options = new BitmapFactory.Options();
	    	options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    	Bitmap bitmap = BitmapFactory.decodeFile(fileUriStr, options);
	    	selected_photo.setImageBitmap(bitmap);

	        ByteArrayOutputStream bao = new ByteArrayOutputStream();

	        String upload_url = prepare_upload_url();
	        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);

	        byte[] data = bao.toByteArray();

	        HttpClient httpClient = new DefaultHttpClient();
	        HttpPost postRequest = new HttpPost(upload_url);
	        MultipartEntity entity = new MultipartEntity(
	                HttpMultipartMode.BROWSER_COMPATIBLE);

	        //Set Data and Content-type header for the image
	        entity.addPart("file",
	                new ByteArrayBody(data, "image/jpeg", "file"));
	        postRequest.setEntity(entity);
	        try {

	            HttpResponse response = httpClient.execute(postRequest);
	        //Read the response
	            String jsonString = EntityUtils.toString(response.getEntity());
	            Log.v(ProgramConstants.TAG, "after uploading file "
	                    + jsonString);

	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    	
	    	
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("offense_type", offence));
	        nameValuePairs.add(new BasicNameValuePair("vehicle_type", vehicleType));
	        nameValuePairs.add(new BasicNameValuePair("vehicle_registration", (registration.getText().toString())));
	        nameValuePairs.add(new BasicNameValuePair("vehicle_description", (description.getText().toString())));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
