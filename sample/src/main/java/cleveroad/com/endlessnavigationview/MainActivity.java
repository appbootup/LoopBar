package cleveroad.com.endlessnavigationview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cleveroad.com.lib.adapter.ICategoryItem;
import cleveroad.com.lib.adapter.SimpleCategoriesAdapter;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.widget.EndlessNavigationView;
import cleveroad.com.lib.widget.OnItemClickListener;
import cleveroad.com.lib.widget.Orientation;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private static final String EXTRA_ORIENTATION = "orientation";

    private EndlessNavigationView endlessNavigationView;

    private SimpleCategoriesAdapter categoriesAdapter;
    private SimpleFragmentStatePagerAdapter pagerAdapter;

    private ViewPager viewPager;

    //args
    @Orientation
    private int orientation;
    @EndlessNavigationView.GravityAttr
    private int endlessGravity = EndlessNavigationView.SELECTION_GRAVITY_START;

    @Nullable
    private Toast toast;

    public static void start(Context context, int orientation) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(EXTRA_ORIENTATION, orientation);
        context.startActivity(starter);
    }

    public void onBtnOrientationClicked(View btn) {
        int nextOrientation = orientation == Orientation.ORIENTATION_VERTICAL ? Orientation.ORIENTATION_HORIZONTAL : Orientation.ORIENTATION_VERTICAL;
        start(this, nextOrientation);
        finish();
    }

    public void onBtnGravityClicked(View btn) {
        int nextGravity = endlessGravity == EndlessNavigationView.SELECTION_GRAVITY_START ?
                EndlessNavigationView.SELECTION_GRAVITY_END : EndlessNavigationView.SELECTION_GRAVITY_START;
        endlessGravity = nextGravity;
        endlessNavigationView.setGravity(nextGravity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @Orientation
        int orientation = getIntent().getIntExtra(EXTRA_ORIENTATION, Orientation.ORIENTATION_HORIZONTAL);
        this.orientation = orientation;

        setContentView(orientation == Orientation.ORIENTATION_VERTICAL ? R.layout.activity_main_vertical : R.layout.activity_main_horizontal);

        endlessNavigationView = (EndlessNavigationView) findViewById(R.id.endlessView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        categoriesAdapter = new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItemsUniq());
        endlessNavigationView.setCategoriesAdapter(categoriesAdapter);
        endlessNavigationView.addOnItemClickListener(this);

        List<Fragment> list = new ArrayList<>(8);
        list.add(ColorFragment.newInstance(android.R.color.holo_red_dark));
        list.add(ColorFragment.newInstance(android.R.color.black));
        list.add(ColorFragment.newInstance(android.R.color.holo_green_dark));
        list.add(ColorFragment.newInstance(android.R.color.holo_orange_dark));
        list.add(ColorFragment.newInstance(android.R.color.holo_blue_light));
        list.add(ColorFragment.newInstance(android.R.color.holo_green_light));
        list.add(ColorFragment.newInstance(android.R.color.holo_purple));
        list.add(ColorFragment.newInstance(android.R.color.white));

        pagerAdapter = new SimpleFragmentStatePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new AbstractPageChangedListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                endlessNavigationView.invalidate();
                Log.d("tag", "on page scrolled");
            }
        });

    }

    @Override
    public void onItemClicked(int position) {
        if (toast != null) {
            toast.cancel();
        }

        ICategoryItem categoryItem = categoriesAdapter.getItem(position);
        viewPager.setCurrentItem(position);

        toast = Toast.makeText(this, getString(R.string.mask_on_item_clicked, position, categoryItem.getCategoryName()), Toast.LENGTH_SHORT);
        toast.show();
    }
}
