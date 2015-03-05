package com.PinBoard.Notifications;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.Ccet.Messenger.CommonTextInfoDisplay;
import org.Ccet.Messenger.Pingbook.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PostNoticeFaculty extends Activity {

	private static final int SELECT_PICTURE = 1;
    private String selectedImagePath="";
	EditText titleEditText;
	EditText contentEditText;
	ImageView showSelectedImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_notice_faculty);
		titleEditText = (EditText) findViewById(R.id.titleEditText);
		contentEditText=(EditText) findViewById(R.id.contentEditText);
		showSelectedImage=(ImageView) findViewById(R.id.imageView1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.post_notice_menubar, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        case R.id.postImageMenuBar:
        	if(Build.VERSION.SDK_INT <19)
        	{
	        	Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent, ""),SELECT_PICTURE);
        	}else{
	            
	            Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
	            intent2.addCategory(Intent.CATEGORY_OPENABLE);
	            intent2.setType("image/jpeg");
	            startActivityForResult(intent2, SELECT_PICTURE);
        	}
            return true;
         default:   
        	return super.onOptionsItemSelected(item);
		}
	}

	public void selectImage(View view){

		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""),SELECT_PICTURE);
	}
	
	
	public void postNotice(View view) {
		String title=titleEditText.getText().toString();
		String content=contentEditText.getText().toString();
		if(checkNetwork() && title.length()>0 && content.length()>0){
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Posting..", "Loading");
			mydialog.showDialog();
			ParseObject notice= new ParseObject("Notices");
			notice.put("heading", title);
			notice.put("content", content+"\n\nPosted By: "+ParseUser.getCurrentUser().getString("username"));
			notice.put("author", ParseUser.getCurrentUser().getString("username"));
			String college = getResources().getString(R.string.college);
			notice.put("college", college);
			
			
			//*************************
			if(selectedImagePath.length()>0){
				
				Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG,22, stream);
				byte[] image = stream.toByteArray();
				
				ParseFile file = new ParseFile("noticeImage.jpg", image);
				file.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						
					}
				});
			
				notice.put("ImageName", "Notice Image");			
				notice.put("ImageFile", file);
				
			}
			notice.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					Toast.makeText(PostNoticeFaculty.this, "Notice Posted Successsfully",
							Toast.LENGTH_SHORT).show();
					mydialog.dismissDialog();
				}
			});
			
			//**************************
			
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                File imgFile= new File(selectedImagePath);
                if(imgFile.exists()){
                	Bitmap imgBitmap= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                	showSelectedImage.setVisibility(0);
                	showSelectedImage.setImageBitmap(imgBitmap);
                }
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    @SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
            if( uri == null ) {
                return null;
            }
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            return uri.getPath();
    }
	
	boolean checkNetwork(){
		if (!CheckNetworkStatus.getInstance(this).isOnline()) {
			CheckNetworkStatus.getInstance(this).showerror(this,
					this, "No network available.");
		return false;
		}
		return true;
	}

}
