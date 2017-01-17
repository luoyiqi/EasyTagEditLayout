package com.yxr.easytagedittext.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxr.easykindedittext.R;
import com.yxr.easytagedittext.helper.SoftKeyboardHelper;
import com.yxr.easytagedittext.helper.SoftKeyboardHelper.OnSoftKeyboardChangeListener;

public class EasyKindEditText extends HorizontalScrollView {
	private boolean visible;
	private LinearLayout mLayout;
	private EditText mEditText;
	private String DEFALUT_SPLIT = " ";
	private int DEFALUT_ITEM_VIEW = R.layout.item_kind;
	private int DEFALUT_TEXT_SIZE = 12;
	private int DEFALUT_BACKGROUND = R.drawable.search_input;
	private int DEFALUT_VISIBILITY = View.VISIBLE;
	private InputMethodManager mIm;

	public EasyKindEditText(Context context) {
		super(context);
		init();
	}

	public EasyKindEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EasyKindEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 设置背景
	 * @param res
	 */
	public void setBackgroundRes(int res) {
		this.DEFALUT_BACKGROUND = res;
		setBackgroundResource(DEFALUT_BACKGROUND);
	}

	/**
	 * 设置标签的布局样式
	 * 其中的控件必须包含id名为 ：
	 * item_content : 用于显示标签内容
	 * item_delete : 用于设置删除点击事件
	 * @param layoutId ： 布局文件Id
	 */
	public void setKindView(int layoutId){
		this.DEFALUT_ITEM_VIEW = layoutId;
	}

	/**
	 * 设置分隔符号,默认是一个空格
	 * @param split
	 */
	public void setSplit(String split) {
		this.DEFALUT_SPLIT = split;
	}
	
	/**
	 * 设置输入框的文字大小
	 * @param size
	 */
	public void setTextSize(int size){
		this.DEFALUT_TEXT_SIZE = size;
	}

	/**
	 * 添加标签
	 * 比如点击GridView某个ITEM，将它添加到输入框中
	 * @param content ： 标签内容
	 */
	public void addKind(String content){
		String text = "";
		if(visible){
			text = mEditText.getText().toString() + DEFALUT_SPLIT + content;
			mEditText.setText(text);
			mEditText.setSelection(mEditText.getText().toString().length());
			scroll();
		}else{
			text = getText();
			if(TextUtils.isEmpty(text)){
				text = content;
			}else{
				text = text + content;
			}
			mEditText.setText(text);
			addKind();
		}
	}
	
	/**
	 * 清除所有标签和文本框内容
	 */
	public void clearContent() {
		mEditText.setText("");
		mLayout.removeAllViews();
	}
	
	/**
	 * 设置输入框显示隐藏
	 * @param visibility
	 */
	public void setEditTextVisbility(int visibility){
		this.DEFALUT_VISIBILITY = visibility;
		if(visible && visibility == View.GONE)
			mIm.hideSoftInputFromWindow(getWindowToken(), 0); 
		if(mEditText != null)
			mEditText.setVisibility(visibility);
	}
	
	/**
	 * 获取输入框的显示隐藏情况
	 * @return
	 */
	public int getEditTextVisbility(){
		return mEditText == null ? View.GONE : mEditText.getVisibility();
	}
	
	/**
	 * 设置输入法监听事件
	 * 输入法显示时，只显示文本框以分隔符分开的内容
	 * 输入法隐藏时，显示标签和空文本框
	 * @param activity
	 */
	public void setSoftKeyBoardChangedListener(Activity activity){
		SoftKeyboardHelper.observeSoftKeyboard(activity, new OnSoftKeyboardChangeListener() {

			@Override
			public void onSoftKeyBoardChange(int keyboardHeight, boolean visible) {
				EasyKindEditText.this.visible = visible;
				Log.e("metrp", "visible : " + visible);
				if(visible){
					setText();
				}else{
					addKind();
				}
			}
		});
	}
	
	/**
	 * 获取所有标签，返回标签内容集合
	 * @return 
	 */
	public List<String> getKinds(){
		if(visible){
			String contents = mEditText.getText().toString();
			return getAllKinds(contents);
		}
		return getAllKinds(getText());
	}

	private List<String> getAllKinds(String contents) {
		List<String> kinds = null;
		if(TextUtils.isEmpty(contents))
			return kinds;
		
		String[] split = contents.split(DEFALUT_SPLIT);
		if(split == null || split.length <= 0)
			split = new String []{contents.trim()};
		
		kinds = new ArrayList<String>(); 
		
		for(String str : split){
			if(TextUtils.isEmpty(str))
				continue;
			kinds.add(str);
		}	
		return kinds;
	}

	/**
	 * dip转换成px
	 * @param dip值
	 * @return 像素值
	 */
	public int dip2px(int dip){
		float density = getResources().getDisplayMetrics().density;
		return (int) (dip*density+0.5);
	}

	private void init() {
		mIm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		setHorizontalScrollBarEnabled(false);

		LinearLayout mLinearLayoutEditText = new LinearLayout(getContext());
		HorizontalScrollView.LayoutParams lp = new HorizontalScrollView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		mLinearLayoutEditText.setPadding(dip2px(2), dip2px(2), dip2px(2), dip2px(2));
		mLinearLayoutEditText.setLayoutParams(lp);
		mLinearLayoutEditText.setGravity(Gravity.CENTER_VERTICAL);
		setBackgroundRes(DEFALUT_BACKGROUND);

		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		mLayout = new LinearLayout(getContext());
		mLayout.setLayoutParams(lp1);
		mLinearLayoutEditText.addView(mLayout);

		mEditText = new EditText(getContext());
		mEditText.setTextSize(DEFALUT_TEXT_SIZE);
		mEditText.setBackgroundColor(Color.TRANSPARENT);
		mEditText.setHint("输入内容默认用空格分隔");
		mEditText.setLayoutParams(lp1);
		mEditText.setVisibility(DEFALUT_VISIBILITY);
		mLinearLayoutEditText.addView(mEditText);
		
		addView(mLinearLayoutEditText);
	}

	private void addKind() {
		String content = mEditText.getText().toString();
		if(TextUtils.isEmpty(content)){
			setText();
		}else{
			mLayout.removeAllViews();
			mLayout.setVisibility(View.VISIBLE);

			String[] split = content.split(DEFALUT_SPLIT);
			if(split == null || split.length <= 0)
				split = new String [] {content};

			for(String str : split){
				if(TextUtils.isEmpty(str))
					continue;
				final View view = View.inflate(getContext(), DEFALUT_ITEM_VIEW , null);
				View itemContent = view.findViewById(R.id.item_content);
				if(itemContent != null && itemContent instanceof TextView){
					TextView textView = (TextView) itemContent;
					textView.setText(str);
				}

				View itemDelete = view.findViewById(R.id.item_delete);
				if(itemDelete != null)
					itemDelete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							int width = view.getMeasuredWidth();
							mLayout.removeView(view);
							scrollTo(mLayout.getRight() - mEditText.getMeasuredWidth() / 4 - width, 0);
						}
					});

				mLayout.addView(view);
			}

			mEditText.setText("");
			scroll();
		}
	}

	private void scroll() {
		scrollTo(mLayout.getRight() - mEditText.getMeasuredWidth() / 4, 0);
	}

	private void setText() {
		String text = getText();
		mEditText.setText(text);
		mLayout.setVisibility(View.GONE);
		mEditText.setSelection(mEditText.getText().toString().length());
		scroll();
	}

	private String getText() {
		String text = "";
		int childCount = mLayout.getChildCount();
		if(childCount > 0){
			for(int i = 0 ; i < childCount ; i++){
				View view = mLayout.getChildAt(i);
				View itemContent = view.findViewById(R.id.item_content);
				if(itemContent != null && itemContent instanceof TextView){
					TextView textView = (TextView) itemContent;
					text = text + textView.getText().toString() + DEFALUT_SPLIT;
				}
			}
		}
		return text;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mEditText.setMinimumWidth(w);
	}
	
	
}
