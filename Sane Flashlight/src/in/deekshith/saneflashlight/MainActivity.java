/*
Copyright (C) 2014  Deekshith Allamaneni

This file is part of Sane Flashlight.

    Sane Flashlight is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Sane Flashlight is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Sane Flashlight.  If not, see <http://www.gnu.org/licenses/>.
*/

package in.deekshith.saneflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
//import android.graphics.Camera;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {
	private Camera camera;
    Button flashlightSwitchImg;
    private boolean isFlashlightOn;
    Parameters params;
    private LinearLayout flashBackground;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// flashlight on off Image
        flashlightSwitchImg = (Button) findViewById(R.id.toggle_flash_btn);
        flashBackground = (LinearLayout) findViewById(R.id.LinearLayout1);
        
        // Hide Action bar
        getSupportActionBar().hide();
        try{
        	boolean isCameraFlash = getApplicationContext().getPackageManager()
        			.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        	
        	if (!isCameraFlash) {
        		showNoCameraAlert();
        	} else {
        		getCamera();
        	}
        	
        	// 	Turn ON the Flashlight initially
        	setFlashlightOn();
        	
        	flashlightSwitchImg.setOnClickListener(new View.OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			if (isFlashlightOn) {
        				setFlashlightOff();
        			} else {
        				setFlashlightOn();
        			}
        		}
        	});
        }catch(Exception e){
        	flashlightSwitchImg.setBackgroundColor(0XFFFF0000);
            flashlightSwitchImg.setText("Sorry, Unknown Error or Device not supported!");
            flashBackground.setBackgroundColor(0XFFF7F7F7);
        }
    }
	
	private void getCamera(){
		if(camera==null){
			try{
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e){
				Log.e("Camera Error", e.getMessage());
			}
		}
	}
    private void showNoCameraAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Error: No Camera Flash!")
                .setMessage("Camera flashlight not available in this Android device!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // close the Android app
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return;
    }
    private void setFlashlightOn() {
       params = camera.getParameters();
       params.setFlashMode(Parameters.FLASH_MODE_TORCH);
       camera.setParameters(params);
       camera.startPreview();
       isFlashlightOn = true;
       flashlightSwitchImg.setBackgroundColor(0XFF1F1F21);
       flashlightSwitchImg.setText("Click to turn OFF");
       flashBackground.setBackgroundColor(0XFFF7F7F7);
    }

    private void setFlashlightOff() {
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isFlashlightOn = false;
        //flashlightSwitchImg.setImageResource(R.drawable.light_off);
        flashlightSwitchImg.setBackgroundColor(0XFF1F1F21);
        flashlightSwitchImg.setText("Click to turn ON");
        flashBackground.setBackgroundColor(0XFF000000);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
         
        // on starting the app get the camera params
        getCamera();
    }
    

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    
    protected void onResume() {
        super.onResume();
        // on resume turn on the flash
        setFlashlightOn();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
     
    @Override
    protected void onPause() {
        super.onPause();
         
        // on pause turn off the flash
        setFlashlightOff();
    }
     
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    
}
