package com.datang.miou.views.inner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Building;
import com.datang.miou.datastructure.InnerMap;
import com.datang.miou.datastructure.InnerOptions;
import com.datang.miou.views.dialogs.BuildingPickerDialogFragment;
import com.datang.miou.views.dialogs.InnerMapPickerDialogFragment;
import com.datang.miou.views.dialogs.PhotoPickerDialogFragment;
import com.datang.miou.views.inner.Gallery;

/**
 * 地图
 * 
 * @author suntongwei
 */
@AutoView(R.layout.inner_option)
public class InnerOptionsFragment extends FragmentSupport{

	private static final String TAG = "InnerOptionsFragment";
	
	protected static final int REQUEST_PHOTO = 0;
	protected static final int REQUEST_BUILDING = 1;
	protected static final int REQUEST_MAP = 2;

	protected static final String DIALOG_PHOTO_PICKER = "dialog_photo_picker";
	protected static final String DIALOG_BUILDING_PICKER = "dialog_building_picker";	
	protected static final String DIALOG_MAP_PICKER = "dialog_map_picker";
	private static final int REQUEST_MAP_IMAGE = 3;
	
	@AutoView(R.id.building_button)
	private Button mBuildingButton;
	@AutoView(R.id.floor_textView)
	private TextView mFloorTextView;
	@AutoView(R.id.increase_floor_button)
	private Button mIncreaseFloorButton;
	@AutoView(R.id.decrease_floor_button)
	private Button mDecreaseFloorButton;
	@AutoView(R.id.inner_map_button)
	private Button mInnerMapButton;
	@AutoView(R.id.map_gallery)
	private Gallery mMapGallery;
	@AutoView(R.id.photo_gallery)
	private Gallery mPhotoGallery;
	
	private BuildingPickerDialogFragment mBuildingPickerDialog;
	
	private ArrayList<Uri> photoUriList;
	private ArrayList<Uri> mapUriList;
	
	private InnerOptions mInnerOptions;
	protected InnerMapPickerDialogFragment mInnerMapPickerDialog;
	
	@AfterView
	public void init() {
			
		mFloorTextView.setText(mInnerOptions.getFloor() + "楼");
		
		mIncreaseFloorButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				int floor = mInnerOptions.getFloor() + 1;
				mInnerOptions.setFloor(floor);
				mFloorTextView.setText(mInnerOptions.getFloor() + "楼");
			}
		});
		
		mDecreaseFloorButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				int floor = mInnerOptions.getFloor() - 1;
				if (floor < 1) {
					floor = 1;
				}
				mInnerOptions.setFloor(floor);
				mFloorTextView.setText(mInnerOptions.getFloor() + "楼");
			}
		});
		
		mBuildingButton.setText(mInnerOptions.getBuilding().getName());
		mBuildingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				FragmentManager fm = getActivity().getSupportFragmentManager();
				mBuildingPickerDialog = BuildingPickerDialogFragment.newInstance(getActivity());
				mBuildingPickerDialog.setTargetFragment(InnerOptionsFragment.this, REQUEST_BUILDING);
				mBuildingPickerDialog.show(fm, DIALOG_BUILDING_PICKER);
			}
		});
		
		mInnerMapButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				FragmentManager fm = getActivity().getSupportFragmentManager();
				mInnerMapPickerDialog = InnerMapPickerDialogFragment.newInstance(getActivity());
				mInnerMapPickerDialog.setTargetFragment(InnerOptionsFragment.this, REQUEST_MAP);
				mInnerMapPickerDialog.show(fm, DIALOG_MAP_PICKER);
			}
		});
		
		photoUriList = mInnerOptions.getImages();
		mapUriList = new ArrayList<Uri>();

		mMapGallery.setFragmentManager(getActivity().getSupportFragmentManager());
		mMapGallery.setParentFragment(InnerOptionsFragment.this);
		mMapGallery.setHasAddButton(true);
		mMapGallery.setUriList(mapUriList);
		mMapGallery.setRequestCode(REQUEST_MAP_IMAGE);
		
		mPhotoGallery.setFragmentManager(getActivity().getSupportFragmentManager());
		mPhotoGallery.setParentFragment(InnerOptionsFragment.this);
		mPhotoGallery.setHasAddButton(true);
		mPhotoGallery.setUriList(photoUriList);
		mPhotoGallery.setRequestCode(REQUEST_PHOTO);
		
		TextView saveButton = (TextView) getActivity().findViewById(R.id.app_title_right_txt);
		//TextView saveButton = InnerActivity.mScriptButton;
		saveButton.setText("保存");
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				saveInnerOptions();
			}
		});
	}

	private void saveInnerOptions() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		mInnerOptions = InnerOptions.newInstance();
	}


	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_PHOTO) {
				Uri uri = data.getExtras().getParcelable(PhotoPickerDialogFragment.EXTRA_PHOTO_URI);
				mPhotoGallery.AddUri(uri);
				mPhotoGallery.updateImageViews();
			}
			if (requestCode == REQUEST_BUILDING) {
				Building building = (Building) data.getExtras().getSerializable(BuildingPickerDialogFragment.EXTRA_BUILDING);
				mInnerOptions.setBuilding(building);
				mBuildingButton.setText(building.getName());		
				mBuildingPickerDialog.dismiss();
			}
			if (requestCode == REQUEST_MAP) {
				ArrayList<InnerMap> maps = (ArrayList<InnerMap>) data.getExtras().getSerializable(BuildingPickerDialogFragment.EXTRA_BUILDING);
				mInnerOptions.setInnerMaps(maps);
				for (InnerMap map : mInnerOptions.getInnerMaps()) {
					Log.i(TAG, map.getName());
				}
				mInnerMapPickerDialog.dismiss();
			}
			if (requestCode == REQUEST_MAP_IMAGE) {
				Uri uri = data.getExtras().getParcelable(PhotoPickerDialogFragment.EXTRA_PHOTO_URI);
				mMapGallery.AddUri(uri);
				mMapGallery.updateImageViews();
			}
		}
	}
}
