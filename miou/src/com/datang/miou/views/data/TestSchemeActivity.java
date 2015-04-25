package com.datang.miou.views.data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.datang.miou.ActivitySupport;
import com.datang.miou.R;
import com.datang.miou.annotation.AutoView;
import com.datang.miou.testplan.TestSchemeId;

/**
 * TestScheme 详细内容显示模版
 */
@AutoView(R.layout.testscheme_activity)
public class TestSchemeActivity extends ActivitySupport {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView mBackButton = (ImageView) findViewById(R.id.app_title_left);
        mBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (NavUtils.getParentActivityName((Activity) getApplicationContext()) != null) {
                        NavUtils.navigateUpFromSameTask((Activity) getApplicationContext());
                    }

                    TestSchemeCache.write(null);


                } catch (Exception e) {
                    finish();
                }
            }
        });
        Intent intent = getIntent();
        if (intent == null) return;

        TextView mTitleTextView = (TextView) findViewById(R.id.app_title_value);
        ITestScheme scheme = TestSchemeCache.getScheme(intent.getStringExtra(TestSchemeId.ID));
        if(scheme==null) {
            mTitleTextView.setText("没有放入SchemeCache");
            return;
        }
        mTitleTextView.setText(scheme.name());

        TableLayout tl = (TableLayout) this.findViewById(R.id.tl_test_scheme);
        if (scheme.id().length() == 0) return;
        for (String name : scheme.keys().keySet()) {
            TableRow row = new TableRow(mContext);
            TextView key = new TextView(mContext);
            key.setText(scheme.keys().get(name));
            EditText value = new EditText(mContext);
            value.setText(scheme.values().get(name));
            row.addView(key);
            row.addView(value);
            tl.addView(row);
        }


    }
}
