package com.kasao.qintai.dialoge;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kasao.qintai.R;
import com.kasao.qintai.adapter.CarSystemAdapter;
import com.kasao.qintai.api.ApiInterface;
import com.kasao.qintai.api.ApiManager;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.model.domain.CarListdomain;
import com.kasao.qintai.widget.FloatingItemDecoration;
import com.kasao.qintaiframework.http.HttpRespnse;
import com.kasao.qintaiframework.until.GsonUtil;
import com.kasao.qintaiframework.until.LogUtil;
import com.kasao.qintaiframework.until.ScreenUtil;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2017/12/11.
 */

public class DialogeCarChose implements View.OnClickListener {
    private Dialog mDialoge;
    private SoftReference<Activity> soft = null;
    private View viewBack;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private CarSystemAdapter adapter;
    private FloatingItemDecoration floatingItemDecoration;
    private String carTitle;
    private String branid;


    public DialogeCarChose(Activity context) {
        soft = new SoftReference<Activity>(context);
        if (soft.get() == null) {
            return;
        }

        if (null == mDialoge) {
            mDialoge = new Dialog(soft.get(), R.style.dialog);
            initView();
        }
    }

    public DialogeCarChose(Activity context, String title, String id) {
        soft = new SoftReference<Activity>(context);
        if (soft.get() == null) {
            return;
        }
        this.carTitle = title;
        this.branid = id;
        mDialoge = new Dialog(soft.get(), R.style.dialog);
        initView();

    }


    private void initView() {
        View view = LayoutInflater.from(soft.get()).inflate(R.layout.dialoge_car_system, null);
        mDialoge.setContentView(view);
        viewBack = view.findViewById(R.id.ivback);
        tvTitle = view.findViewById(R.id.tvTitle);
        recyclerView = view.findViewById(R.id.recycleview);
        FrameLayout layout = view.findViewById(R.id.viewReturn);
        viewBack.setOnClickListener(this);
        layout.setOnClickListener(this);
        Window window = mDialoge.getWindow();// 获取Window对象
        WindowManager.LayoutParams lp = window.getAttributes();
        ScreenUtil.initScreen(soft.get());
        lp.width = (int) (0.75 * ScreenUtil.getScreenW());
        lp.height = ScreenUtil.getScreenH();
        window.setGravity(Gravity.RIGHT);
        mDialoge.getWindow().setAttributes(lp);
        mDialoge.setCanceledOnTouchOutside(true);
        mDialoge.getWindow().setWindowAnimations(R.style.carsystem);
        adapter = new CarSystemAdapter();
        LinearLayoutManager ll = new LinearLayoutManager(soft.get());
        //   manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setmICarSeries(new CarSystemAdapter.ICarSeries() {
            @Override
            public void onCarSerise(String seriesId) {
                if (null != mIChoseSeries) {
                    mIChoseSeries.onChoseSerise(seriesId);

                }
            }
        });
        floatingItemDecoration = new FloatingItemDecoration(soft.get(), Color.BLUE, 1, 0);
        if (!TextUtils.isEmpty(carTitle)) {
            tvTitle.setText(carTitle);
        }

        getData(branid);
    }

    @Override
    public void onClick(View view) {
        hide();
    }

    public void hide() {
        if (mDialoge != null) {
            mDialoge.dismiss();
        }
    }

    public void showDialoge(IChoseSeries mIChoseSeries) {
        if (mDialoge != null) {
            this.mIChoseSeries = mIChoseSeries;
            mDialoge.show();

        }
    }

    Map<Integer, String> carmap = new HashMap<>();
    List<CarDetailEntity> listEntity;
    String target = "";

    private void getData(String brandid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("brand_id", brandid);
        ApiManager.Companion.getGetInstance().loadDataByParmars(ApiInterface.Companion.getCarSystemById(), map, new HttpRespnse() {
            @Override
            public void _onComplete() {

            }

            @Override
            public void _onNext(@NotNull ResponseBody t) {
                try {
                    CarListdomain domain = GsonUtil.Companion.GsonToBean(t.string(), CarListdomain.class);
                    List<CarDetailEntity> list = domain.data;
                    int size = list.size();
                    listEntity = new ArrayList<>();

                    Collections.sort(list, new Comparator<CarDetailEntity>() {
                        @Override
                        public int compare(CarDetailEntity o1, CarDetailEntity o2) {
                            return o1.purpose.compareTo(o2.purpose);
                        }
                    });
                    for (int i = 0; i < size; i++) {
                        String purse = list.get(i).purpose;
                        if (!purse.equals(target)) {
                            target = list.get(i).purpose.trim();
                            if (carmap.containsValue(target)) {
                                listEntity.add(list.get(i));
                            } else {
                                carmap.put(i, target);
                                listEntity.add(list.get(i));
                            }
                        } else {
                            listEntity.add(list.get(i));
                        }
                    }
                    adapter.setList(listEntity);
                    floatingItemDecoration.setKeys(carmap);
                    floatingItemDecoration.setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, soft.get().getResources().getDisplayMetrics()));
                    recyclerView.addItemDecoration(floatingItemDecoration);
                } catch (Throwable e) {
                    LogUtil.e(e);
                }

            }


            @Override
            public void _onError(@NotNull Throwable e) {

            }
        });
    }

    private IChoseSeries mIChoseSeries;

    public interface IChoseSeries {
        void onChoseSerise(String id);
    }
}
