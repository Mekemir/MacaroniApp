package com.game.macaroniapp.bindings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.game.macaroniapp.R;

public final class ViewBindings {

    private  ViewBindings() {

    }

    @BindingAdapter({"adapter"})
    public static void setAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setAdapter(adapter);
    }

    @BindingAdapter({"layoutManager"})
    public  static void setLayoutManager(RecyclerView view, LayoutManagers layoutManager) {
        switch (layoutManager) {
            case LINEAR_LAYOUT_MANAGER:
                view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                break;
            case HORIZONTAL_LAYOUT_MANAGER:
                view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
                PagerSnapHelper pagerSnapHelper  = new PagerSnapHelper();
                pagerSnapHelper.attachToRecyclerView(view);
                break;
            case GRID_LAYOUT_MANAGER:
                view.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
                break;
        }
    }


    @BindingAdapter({"backgroundImg"})
    public static void setProgressValue(View view, int backgroundImg) {
        view.setBackgroundResource(backgroundImg);
    }

    @BindingAdapter({"app:srcCompat"})
    public static void setImageViewResource(ImageView imageView, Drawable resource) {
        imageView.setImageDrawable(resource);
    }

    @BindingAdapter({"cardColor"})
    public static void setCardColor(CardView progressBar, int color) {
        progressBar.setCardBackgroundColor(color);
    }

    @BindingAdapter({"textColors"})
    public static void setCardColor(TextView textView, int color) {
        textView.setTextColor(color);
    }

    @BindingAdapter({"shouldMarkCorrect"})
    public static void setShouldMarkCorrect(CardView view, boolean shouldMarkCorrect) {
        if (shouldMarkCorrect) {
            view.setCardBackgroundColor(view.getResources().getColor(R.color.teal_700));
        }
    }

    @BindingAdapter({"setVisibility"})
    public static void setSetVisibility(View view, int setVisibility) {
        view.setVisibility(setVisibility);
    }

    @BindingAdapter({"animateView"})
    public static void setAnimateView(View view, boolean shouldAnimate) {
        Context context = view.getContext();
        if (context == null) {
            return;
        }
        if (!shouldAnimate) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(500);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Context context = view.getContext();
                if (context == null) {
                    return;
                }
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Context context = view.getContext();
                if (context == null) {
                    return;
                }
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(animation);
    }
    public static enum LayoutManagers {
        LINEAR_LAYOUT_MANAGER,
        HORIZONTAL_LAYOUT_MANAGER,
        GRID_LAYOUT_MANAGER
    }

}
