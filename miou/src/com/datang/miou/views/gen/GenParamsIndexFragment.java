package com.datang.miou.views.gen;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.views.gen.params.Table;
import com.datang.miou.views.gen.params.TableManager;

@AutoView(R.layout.fragment_gen_params_index)
public class GenParamsIndexFragment extends FragmentSupport {
	@AutoView(R.id.cell_info_textView)
	TextView mTitle;
	

	private String mTableId;

	private Table mTable;
	
	public GenParamsIndexFragment(String id) {
		mTableId = id;
	}
	
	@AfterView
	void init() {
		
		LinearLayout servingCellInfoTable = (LinearLayout) mView.findViewById(R.id.index_table);
		
		mTable = TableManager.createTable(getActivity(), servingCellInfoTable, mTableId);
		
	}

	@Override
	protected void updateUI(RealData data) {
		// TODO 自动生成的方法存根
		super.updateUI(data);
		if (mTable != null) {
			TableManager.updateTable(mTable);
		}
	}
}
