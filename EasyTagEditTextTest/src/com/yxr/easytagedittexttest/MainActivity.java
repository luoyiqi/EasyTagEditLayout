package com.yxr.easytagedittexttest;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxr.easykindedittexttest.R;
import com.yxr.easytagedittext.widget.EasyKindEditText;

public class MainActivity extends Activity {
	private EasyKindEditText mEketSearch;
	private GridView mGvKind;
	private String [] mObjects = new String[]{"语文","数学","英语","物理","化学","生物","历史","地理","政治"
			,"手工","社会","科学","劳动","体育","音乐","美术"};
	private Button btnSearch;
	private TextView tvClear;
	private Button btnChanged;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		initView();
		initListener();
		initData();
	}

	private void initView() {
		mEketSearch = (EasyKindEditText) findViewById(R.id.eket_search);
		mGvKind = (GridView) findViewById(R.id.gv_kind);
		btnSearch = (Button) findViewById(R.id.btn_search);
		tvClear = (TextView) findViewById(R.id.tv_clear);
		btnChanged = (Button) findViewById(R.id.btn_changed);
	}

	private void initListener() {
		mEketSearch.setSoftKeyBoardChangedListener(this);
		mGvKind.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				String content = mObjects[position];
				mEketSearch.addKind(content);
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				List<String> kinds = mEketSearch.getKinds();
				if(kinds != null)
					Toast.makeText(getApplicationContext(), kinds.toString(), Toast.LENGTH_SHORT).show();
				mEketSearch.clearContent();
			}
		});
		tvClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mEketSearch.clearContent();
			}
		});
		btnChanged.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mEketSearch.setEditTextVisbility(mEketSearch.getEditTextVisbility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			}
		});
	}

	private void initData() {
		mEketSearch.setBackgroundRes(R.drawable.search_input);
		mEketSearch.setKindView(R.layout.item_kind);
		mEketSearch.setSplit(" ");
		mEketSearch.setTextSize(12);
		
		mGvKind.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mObjects));
	}
}
