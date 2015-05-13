package com.datang.miou.views.dialogs;

import java.io.File;

import com.datang.miou.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoPickerDialogFragment extends DialogFragment {

	protected static final int REQUEST_TAKE_PHOTO = 0;
	private static final int REQUEST_PHOTO_ZOOM = 1;
	protected static final int REQUEST_PICK_PHOTO = 2;
	
	private static final String IMAGE_FILE_LOACTION = Environment.getExternalStorageDirectory() + "/temp.jpg";
	public static final String EXTRA_PHOTO_URI = "extra_photo_uri";
	

	public static PhotoPickerDialogFragment newInstance(Context context) {
		new Bundle();
		PhotoPickerDialogFragment fragment = new PhotoPickerDialogFragment();
		return fragment;
	}

	private Uri imageUri;
	private ImageView imageView;
	private Button takeButton;
	private Button pickButton;
	
	private PhotoPickerDialogFragment mDialog;
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo_picker, null);
		
		mDialog = this;
		
		imageUri = Uri.fromFile(new File(IMAGE_FILE_LOACTION));
		takeButton = (Button) view.findViewById(R.id.take_button);
		takeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
				startActivityForResult(intent, REQUEST_TAKE_PHOTO);
			}
		});
		
		pickButton = (Button) view.findViewById(R.id.pick_button);
		pickButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, REQUEST_PICK_PHOTO);
			}
		});
		
		imageView = (ImageView) view.findViewById(R.id.test_imageView);
		
		return new AlertDialog.Builder(getActivity())
					.setView(view)
					.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_TAKE_PHOTO) {
				Uri uri = imageUri;
				mDialog.dismiss();
				sendResult(uri, Activity.RESULT_OK);
			}
			if (requestCode == REQUEST_PHOTO_ZOOM) {
				Bundle extras = data.getExtras();
				Bitmap photo = null;
				if (extras != null) {
					photo = extras.getParcelable("data");
				}
				if (photo != null) {
					imageView.setImageBitmap(photo);
				}
			}
			if (requestCode == REQUEST_PICK_PHOTO) {
				Uri uri = data.getData();
				mDialog.dismiss();
				sendResult(uri, Activity.RESULT_OK);
			}
		}
	}

	private void sendResult(Uri uri, int resultCode) {
		// TODO 自动生成的方法存根
		if (getTargetFragment() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_PHOTO_URI, uri);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}
}
