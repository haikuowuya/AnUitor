package com.scurab.android.anuitorsample;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.scurab.android.anuitorsample.widget.SlidingPaneLayout;

/**
 * Created by jbruchanov on 15.5.14.
 */
public class MainActivity extends FragmentActivity {

    private SlidingPaneLayout mPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mPaneLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        Resources res = getResources();
        mPaneLayout.setParallaxDistance((int) res.getDimension(R.dimen.left_menu_parallax_distance));
        mPaneLayout.setSliderFadeColor(Color.TRANSPARENT);

        View v = findViewById(R.id.txt_sample);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        v.setAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sample_menu, menu);
        return true;
    }

    public void showToast(Throwable t) {
        showToast(t.getMessage());
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void openFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, f, f.getClass().getSimpleName())
                .commit();
        mPaneLayout.closePane();
    }
}