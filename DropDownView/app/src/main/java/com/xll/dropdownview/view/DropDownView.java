package com.xll.dropdownview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xll.dropdownview.Constant;
import com.xll.dropdownview.R;

/**
 * 功能：下拉框控件
 * 作者：xll
 * Created on 2017/6/26.
 */

public class DropDownView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private TextView tv_one, tv_two, tv_three, tv_four;
    private Drawable pullDown, pullUp;
    private PopupWindow popupWindow;

    //四个顶层标签的最终选定结果，默认是不限
    private String[] lastList;

    private MyAdapter adapter;
    private boolean isClick0, isClick1, isClick2, isClick3;//记录点击状态 默认全未点击
    private int clickDropDownNum;//被点击的顶层标签

    private String[] a, b, c, d;

    public void setLastList(String[] lastList) {
        this.lastList = lastList;
    }

    public DropDownView(Context context) {
        super(context);
    }

    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public DropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.drop_down, this);

        tv_one = (TextView) findViewById(R.id.tv1);
        tv_two = (TextView) findViewById(R.id.tv2);
        tv_three = (TextView) findViewById(R.id.tv3);
        tv_four = (TextView) findViewById(R.id.tv4);

        pullDown = ResourcesCompat.getDrawable(getResources(), R.mipmap.pull_down, null);
        if (pullDown != null) {
            pullDown.setBounds(0, 0, pullDown.getMinimumWidth(), pullDown.getMinimumHeight());
        }
        pullUp = ResourcesCompat.getDrawable(getResources(), R.mipmap.pull_up, null);
        if (pullUp != null) {
            pullUp.setBounds(0, 0, pullUp.getMinimumWidth(), pullUp.getMinimumHeight());
        }
        adapter = new MyAdapter();
        tv_one.setOnClickListener(this);
        tv_two.setOnClickListener(this);
        tv_three.setOnClickListener(this);
        tv_four.setOnClickListener(this);
    }

    private void showPopupWindow(View view, final String[] strs) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_window, null);
        popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        LinearLayout llPopw = (LinearLayout) contentView.findViewById(R.id.ll_popw);
        llPopw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        RecyclerView rcv = (RecyclerView) contentView.findViewById(R.id.rcv_pop);
        rcv.setLayoutManager(new LinearLayoutManager(mContext));
        rcv.setAdapter(adapter);
        adapter.setStrs(strs);

        popupWindow.setAnimationStyle(R.style.dropDownAnim);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, 0, 2);
        //监听popupwindow消失状态
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextviewGray();
            }
        });
    }

    //全部变灰
    private void setTextviewGray() {
        tv_one.setCompoundDrawables(null, null, pullDown, null);
        tv_one.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black_9, null));
        tv_two.setCompoundDrawables(null, null, pullDown, null);
        tv_two.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black_9, null));
        tv_three.setCompoundDrawables(null, null, pullDown, null);
        tv_three.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black_9, null));
        tv_four.setCompoundDrawables(null, null, pullDown, null);
        tv_four.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black_9, null));
    }

    /**
     * 设置控件的标题
     * @param title
     * @param clickDropDownNum
     */
    public void setTitle(String title, int clickDropDownNum) {
        switch (clickDropDownNum) {
            case 0:
                tv_one.setText(title);
                break;
            case 1:
                tv_two.setText(title);
                break;
            case 2:
                tv_three.setText(title);
                break;
            case 3:
                tv_four.setText(title);
                break;
            default:
                break;
        }
    }

    /**
     * 设置pop展示的列表
     * @param strs
     * @param clickDropDownNum
     */
    public void setStrs(String[] strs, int clickDropDownNum) {
        switch (clickDropDownNum) {
            case 0:
                a = strs;
                break;
            case 1:
                b = strs;
                break;
            case 2:
                c = strs;
                break;
            case 3:
                d = strs;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        setTextviewGray();
        switch (v.getId()) {
            case R.id.tv1:
                tv_one.setCompoundDrawables(null, null, pullUp, null);

                tv_one.setTextColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
                //pop不存在则show
                if (popupWindow == null || !popupWindow.isShowing()) {
                    dropDownClickListener.onDropDownClick(0);
                    showPopupWindow(tv_one, a);
                } else {
                    //存在则刷新
                    if (isClick0) {
                        //当前已展示 再次点击让它消失
                        popupWindow.dismiss();
                    } else {
                        dropDownClickListener.onDropDownClick(0);
                        adapter.setStrs(a);
                    }
                }
                clickDropDownNum = 0;
                isClick0 = !isClick0;
                isClick1 = false;
                isClick2 = false;
                isClick3 = false;
                break;
            case R.id.tv2:
                tv_two.setCompoundDrawables(null, null, pullUp, null);
                tv_two.setTextColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
                //pop不存在则show
                if (popupWindow == null || !popupWindow.isShowing()) {
                    dropDownClickListener.onDropDownClick(1);
                    showPopupWindow(tv_two, b);
                } else {
                    if (isClick1) {
                        //当前已展示 再次点击让它消失
                        popupWindow.dismiss();
                    } else {
                        dropDownClickListener.onDropDownClick(1);
                        //存在则刷新
                        adapter.setStrs(b);
                    }
                }
                clickDropDownNum = 1;
                isClick1 = !isClick1;
                isClick0 = false;
                isClick2 = false;
                isClick3 = false;
                break;
            case R.id.tv3:
                tv_three.setCompoundDrawables(null, null, pullUp, null);
                tv_three.setTextColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
                //pop不存在则show
                if (popupWindow == null || !popupWindow.isShowing()) {
                    dropDownClickListener.onDropDownClick(2);
                    showPopupWindow(tv_three, c);
                } else {
                    if (isClick2) {
                        //当前已展示 再次点击让它消失
                        popupWindow.dismiss();
                    } else {
                        dropDownClickListener.onDropDownClick(2);
                        //存在则刷新
                        adapter.setStrs(c);
                    }
                }
                clickDropDownNum = 2;
                isClick2 = !isClick2;
                isClick0 = false;
                isClick1 = false;
                isClick3 = false;
                break;
            case R.id.tv4:

                tv_four.setCompoundDrawables(null, null, pullUp, null);
                tv_four.setTextColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
                //pop不存在则show
                if (popupWindow == null || !popupWindow.isShowing()) {
                    dropDownClickListener.onDropDownClick(3);
                    showPopupWindow(tv_four, d);
                } else {
                    if (isClick3) {
                        //当前已展示 再次点击让它消失
                        popupWindow.dismiss();
                    } else {
                        dropDownClickListener.onDropDownClick(3);
                        //存在则刷新
                        adapter.setStrs(d);
                    }
                }
                clickDropDownNum = 3;
                isClick3 = !isClick3;
                isClick0 = false;
                isClick1 = false;
                isClick2 = false;
                break;
            default:
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        // 每一个标签它自己下面列表的填充数据
        private String[] strs;

        public MyAdapter() {

        }

        public void setStrs(String[] strs) {
            this.strs = strs;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.pop_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(strs[position]);
            if (lastList[clickDropDownNum].equals(strs[position])) {
                holder.Img.setVisibility(VISIBLE);
                holder.tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
            } else {
                holder.Img.setVisibility(GONE);
                holder.tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black_9, null));
            }
            if (position == strs.length - 1) {
                holder.viewLine.setVisibility(GONE);
            } else {
                holder.viewLine.setVisibility(VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return strs == null ? 0 : strs.length;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private ImageView Img;
        private View viewLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_pop);
            Img = (ImageView) itemView.findViewById(R.id.img);
            viewLine = itemView.findViewById(R.id.view_line);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    if (clickDropDownNum == 0) {
                        dropDownItemClickListener.onDropDownItemClick(Constant.A[getLayoutPosition()], clickDropDownNum);
                        lastList[0] = Constant.A[getLayoutPosition()];
                    }
                    if (clickDropDownNum == 1) {
                        dropDownItemClickListener.onDropDownItemClick(Constant.B[getLayoutPosition()], clickDropDownNum);
                        lastList[1] = Constant.B[getLayoutPosition()];
                    }
                    if (clickDropDownNum == 2) {
                        dropDownItemClickListener.onDropDownItemClick(Constant.C[getLayoutPosition()], clickDropDownNum);
                        lastList[2] = Constant.C[getLayoutPosition()];
                    }
                    if (clickDropDownNum == 3) {
                        dropDownItemClickListener.onDropDownItemClick(Constant.D[getLayoutPosition()], clickDropDownNum);
                        lastList[3] = Constant.D[getLayoutPosition()];
                    }
                }
            });
        }
    }

    private OnDropDownItemClickListener dropDownItemClickListener;

    //下拉列表的点击事件
    public interface OnDropDownItemClickListener {
        void onDropDownItemClick(String selectedDropDownChildItem, int clickDropDownNum);
    }

    public void setOnDropDownItemClickListener(OnDropDownItemClickListener dropDownItemClickListener) {
        this.dropDownItemClickListener = dropDownItemClickListener;
    }

    private OnDropDownClickListener dropDownClickListener;

    //点击四个下拉框的点击事件
    public interface OnDropDownClickListener {
        void onDropDownClick(int clickDropDownNum);
    }

    public void setOnDropDownClickListener(OnDropDownClickListener dropDownClickListener) {
        this.dropDownClickListener = dropDownClickListener;
    }
}
