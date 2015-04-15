package com.datang.miou.views.gen.params;

import android.content.res.Resources;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.RealData;

@AutoView(R.layout.fragment_gen_params)
public class GenParamsGsmFragment extends FragmentSupport {
	
	@AutoView(R.id.cell_info_textView)
	TextView mTitle;
	
	private Table mServingCellTable;
	private Table mNeighborCellTable;
	
	@AfterView
	void init() {
		
		Resources r = getActivity().getResources();
		mTitle.setText(r.getString(R.string.cell_info_hint) + r.getString(R.string.lte));
		
		LinearLayout servingCellInfoTable = (LinearLayout) mView.findViewById(R.id.serving_cell_info_table);
		
		mServingCellTable = TableManager.createTable(getActivity(), servingCellInfoTable, Globals.TABLE_SERVICE_CELL_GSM);
		
		LinearLayout neighborCellInfoTable = (LinearLayout) mView.findViewById(R.id.neighbor_cell_info_table);
		
		mNeighborCellTable = TableManager.createTable(getActivity(), neighborCellInfoTable, Globals.TABLE_NEIGHBOR_CELL_GSM);
		
	}

	@Override
	protected void updateUI(RealData data) {
		// TODO 自动生成的方法存根
		super.updateUI(data);
		if (mServingCellTable != null) {
			Log.i(TAG, "GSM service updateTable");
			TableManager.updateTable(mServingCellTable);
		}
		if (mNeighborCellTable != null) {
			Log.i(TAG, "GSM service updateTable");
			TableManager.updateTable(mNeighborCellTable);
		}
	}
}
