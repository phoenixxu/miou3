package com.datang.miou.views.gen.params;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.datang.miou.FragmentSupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AfterView;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.datastructure.Globals;
import com.datang.miou.datastructure.RealData;
import com.datang.miou.views.gen.GenParamsFragment;

@AutoView(R.layout.fragment_gen_params_lte)
public class GenParamsLteFragment extends FragmentSupport {
	
	private static final String TAG = "GenParamsLteFragment";
	
	private class ServingCellInfo {
		//这个TextView的数组用来存放所有的参数值对应的列表域
		//从XML中获得的参数类型用来指定数组下标，参数描述用来指定对应的描述表格
		private TextView[] paramTextViews;
		
		public ServingCellInfo() {
			this.paramTextViews = new TextView[Globals.MAX_PARAMS];
		}
	}
	
	private class NeighborCellInfo {
		private TextView[] paramTextViews;
		public NeighborCellInfo() {
			this.paramTextViews = new TextView[Globals.MAX_PARAMS];
		}
	}
	//假设总共有128个参数
	
	private static boolean tableRowStyleFlag;
	private TableLayout servingCellInfoTable;
	private TableLayout neighborCellInfoTable;
	private SaxItemParser parser;
	
	//用于存放表项，包括表项参数类型和表项参数描述
	private List<ParamTableItem> items;
	private NeighborCellInfo[] mNeighborCellInfo;
	private ServingCellInfo mServingCellInfo;

	@AfterView
	void init() {
		mServingCellInfo = new ServingCellInfo();
		mNeighborCellInfo = new NeighborCellInfo[Globals.NEIGHBOR_CELL_SIGNAL_MOST_STRONG];
		for (int i = 0; i < Globals.NEIGHBOR_CELL_SIGNAL_MOST_STRONG; i++) {
			mNeighborCellInfo[i] = new NeighborCellInfo();
		}
		
		TextView title = (TextView) getActivity().findViewById(R.id.cell_info_textView);
		title.setText("服务小区与邻小区信息-LTE");
		
		getParamsfromXml();
		
		AddServingCellInfoTable();	
		
		GenParamsFragment.tableRowStyleFlag = true;
		
		AddNeighborCellInfoTable();
	}
	
	private void getParamsfromXml() {
		// TODO 自动生成的方法存根
		//Test
		try {
			InputStream is = getActivity().getAssets().open("params_items_lte.xml");
			parser = new SaxItemParser();
			items = parser.parse(is);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	private void AddServingCellInfoTable() {
		// TODO 自动生成的方法存根
		servingCellInfoTable = (TableLayout) mView.findViewById(R.id.serving_cell_info_table);
		
		for (ParamTableItem item : items) {
			if (item.getType() < 100) {
				mServingCellInfo.paramTextViews[item.getType()] = GenParamsFragment.addTwoColumnsForTable(getActivity(), servingCellInfoTable, item.getDescription());
			}
		}	
	}
	
	private void AddNeighborCellInfoTable() {
		// TODO 自动生成的方法存根
		neighborCellInfoTable = (TableLayout) mView.findViewById(R.id.neighbor_cell_info_table);
		
		for (ParamTableItem item : items) {
			if (item.getType() >= 100) {
				TextView[] views = GenParamsFragment.addSevenColumnsForTable(getActivity(), neighborCellInfoTable, item.getDescription());
				for (int i = 0; i < Globals.NEIGHBOR_CELL_SIGNAL_MOST_STRONG; i++) {
					mNeighborCellInfo[i].paramTextViews[item.getType()] = views[i];
				}
			}
		}
	}

	@Override
	protected void updateUI(RealData data) {
		// TODO 自动生成的方法存根
		super.updateUI(data);
		for (int i = 0; i < Globals.MAX_PARAMS; i++) {
			TextView view = mServingCellInfo.paramTextViews[i];
			if (view != null) {
				view.setText(reserveTwoBit(data.params[i]));
			}
		}
	}
	
	private String reserveTwoBit(double data) {
		DecimalFormat format = new DecimalFormat("#.00");
		return format.format(data);
	}
}
