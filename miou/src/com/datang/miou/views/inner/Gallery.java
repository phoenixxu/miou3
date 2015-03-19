package com.datang.miou.views.inner;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.datang.miou.R;
import com.datang.miou.views.dialogs.PhotoPickerDialogFragment;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Gallery extends LinearLayout {

	protected static final String DIALOG_PHOTO_PICKER = "dialog_photo_picker";
	private static final String TAG = "Gallery";
	private int mMaxPhotoNum = 4;
	private boolean hasAddButton = true;
	
	private Button mAddButton;
	private ImageView[] mImageViews;
	private ArrayList<Uri> mUriList;
	private Context mContext;
	private int mImageHeight = 400;
	private int mImageWidth = 400;
	private PhotoPickerDialogFragment mPhotoPickerDialog;
	private Fragment mParentFragment;
	private FragmentManager mFragmentManager;
	private int mRequestCode;
	private LinearLayout mLayout;
	
	public Gallery(Context context, AttributeSet ats) {
		super(context, ats);
		// TODO 自动生成的构造函数存根
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.gallery, this, true);
		
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context, ats);
		//params.height = mImageHeight;
		mLayout = (LinearLayout) view.findViewById(R.id.image_layout);
		mLayout.setGravity(Gravity.CENTER_VERTICAL);
		//mLayout.setLayoutParams(params);
		
		
		if (hasAddButton) {
			mAddButton = new Button(context);
			mAddButton.setWidth(mImageWidth / 2);
			mAddButton.setHeight(mImageHeight / 2);
			mAddButton.setGravity(Gravity.CENTER);
			mAddButton.setText("+");
			mAddButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					FragmentManager fm = mFragmentManager;
					mPhotoPickerDialog = PhotoPickerDialogFragment.newInstance(mContext);
					mPhotoPickerDialog.setTargetFragment(mParentFragment, mRequestCode);
					mPhotoPickerDialog.show(fm, DIALOG_PHOTO_PICKER);
				}
			});	
			mLayout.addView(mAddButton);
		}
		
		mImageViews = new ImageView[mMaxPhotoNum];
		for (int i = 0; i < mMaxPhotoNum; i++) {
			mImageViews[i] = new ImageView(context);
			mLayout.addView(mImageViews[i]);
		}
		
		OnLongClickListener listener = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {
				// TODO 自动生成的方法存根
				ImageView imageView = (ImageView) view;
				imageView.setImageBitmap(null);
				for (int i = 0; i < mUriList.size(); i++) {
					if (mImageViews[i].equals(imageView)) {
						mUriList.remove(i);
					}
				}
				updateImageViews();
				return true;
			}
			
		};
		for (ImageView imageView : mImageViews) {
			imageView.setOnLongClickListener(listener);
		}
	}

	public void updateImageViews() {
		clearImageViews();
		//int max = mUriList.size() > mMaxPhotoNum ? mMaxPhotoNum : mUriList.size();
		for (int i = 0; i < mUriList.size(); i++) {
			ContentResolver cr = mContext.getContentResolver();
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(mUriList.get(i)));
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			Bitmap smallBitmap = cutBitmap(bitmap, mImageHeight, mImageWidth);
			mImageViews[i].setImageBitmap(smallBitmap);
			mImageViews[i].setVisibility(View.VISIBLE);
		}
	}

	private void clearImageViews() {
		for (ImageView view : mImageViews) {
			view.setImageBitmap(null);
		}
	}
	
	private Bitmap cutBitmap(Bitmap bitmap, int targetHeight, int targetWidth) {
		// TODO 自动生成的方法存根
		Matrix matrix = new Matrix();
		float scale;
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		
		if (height > width) {
			scale = targetHeight / (float) height;
		} else {
			scale = targetWidth / (float) width;
		}
		matrix.postScale(scale, scale);
		Bitmap resize = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resize;
	}
	
	public int getMaxPhotoNum() {
		return mMaxPhotoNum;
	}

	public void setMaxPhotoNum(int mMaxPhotoNum) {
		this.mMaxPhotoNum = mMaxPhotoNum;
	}

	public void setImageHeight(int mImageHeight) {
		this.mImageHeight = mImageHeight;
	}

	public void setImageWidth(int mImageWidth) {
		this.mImageWidth = mImageWidth;
	}

	public void setFragmentManager(FragmentManager mFragmentManager) {
		this.mFragmentManager = mFragmentManager;
		Log.i(TAG, "FragmentManager = " + this.mFragmentManager);
	}

	public void setParentFragment(Fragment mParentFragment) {
		this.mParentFragment = mParentFragment;
	}

	public void AddUri(Uri uri) {
		if (mUriList.size() == mMaxPhotoNum) {
			return;
		} else {
			mUriList.add(uri);
		}
	}
	
	public void setUriList(ArrayList<Uri> mUriList) {
		this.mUriList = mUriList;
	}

	public int getRequestCode() {
		return mRequestCode;
	}

	public void setRequestCode(int mRequestCode) {
		this.mRequestCode = mRequestCode;
	}

	public void setHasAddButton(boolean hasAddButton) {
		this.hasAddButton = hasAddButton;
		if (!hasAddButton) {
			mLayout.removeView(mAddButton);
		}
	}
	
	
}
