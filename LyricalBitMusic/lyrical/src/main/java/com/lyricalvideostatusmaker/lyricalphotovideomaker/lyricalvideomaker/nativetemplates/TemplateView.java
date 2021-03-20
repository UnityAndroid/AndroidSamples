// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.nativetemplates;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;

/**
 * Base class for a template view. *
 */
public class TemplateView extends FrameLayout {

    private int templateType;
    private NativeTemplateStyle styles;
    private UnifiedNativeAd nativeAd;
    private UnifiedNativeAdView nativeAdView;

    private TextView primaryView;
    private TextView secondaryView;
    private TextView ad_price, ad_store, ad_advertiser;
    private AppCompatRatingBar ratingBar;
    private ImageView iconView;
    private MediaView mediaView;
    private AppCompatTextView callToActionView;
    private LinearLayout background;

    private static final String MEDIUM_TEMPLATE = "medium_template";
    private static final String SMALL_TEMPLATE = "small_template";

    public TemplateView(Context context) {
        super(context);
    }

    public TemplateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TemplateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public TemplateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void setStyles(NativeTemplateStyle styles) {
        this.styles = styles;
        this.applyStyles();
    }

    public UnifiedNativeAdView getNativeAdView() {
        return nativeAdView;
    }

    private void applyStyles() {

        Drawable mainBackground = styles.getMainBackgroundColor();
        if (mainBackground != null) {
            background.setBackground(mainBackground);
            if (primaryView != null) {
                primaryView.setBackground(mainBackground);
            }
            if (secondaryView != null) {
                secondaryView.setBackground(mainBackground);
            }
        }

        Typeface primary = styles.getPrimaryTextTypeface();
        if (primary != null && primaryView != null) {
            primaryView.setTypeface(primary);
        }

        Typeface secondary = styles.getSecondaryTextTypeface();
        if (secondary != null && secondaryView != null) {
            secondaryView.setTypeface(secondary);
        }


        Typeface ctaTypeface = styles.getCallToActionTextTypeface();
        if (ctaTypeface != null && callToActionView != null) {
            callToActionView.setTypeface(ctaTypeface);
        }

        int primaryTypefaceColor = styles.getPrimaryTextTypefaceColor();
        if (primaryTypefaceColor > 0 && primaryView != null) {
            primaryView.setTextColor(primaryTypefaceColor);
        }

        int secondaryTypefaceColor = styles.getSecondaryTextTypefaceColor();
        if (secondaryTypefaceColor > 0 && secondaryView != null) {
            secondaryView.setTextColor(secondaryTypefaceColor);
        }

        int ctaTypefaceColor = styles.getCallToActionTypefaceColor();
        if (ctaTypefaceColor > 0 && callToActionView != null) {
            callToActionView.setTextColor(ctaTypefaceColor);
        }

        float ctaTextSize = styles.getCallToActionTextSize();
        if (ctaTextSize > 0 && callToActionView != null) {
            callToActionView.setTextSize(ctaTextSize);
        }

        float primaryTextSize = styles.getPrimaryTextSize();
        if (primaryTextSize > 0 && primaryView != null) {
            primaryView.setTextSize(primaryTextSize);
        }

        float secondaryTextSize = styles.getSecondaryTextSize();
        if (secondaryTextSize > 0 && secondaryView != null) {
            secondaryView.setTextSize(secondaryTextSize);
        }

        Drawable ctaBackground = styles.getCallToActionBackgroundColor();
        if (ctaBackground != null && callToActionView != null) {
            callToActionView.setBackground(ctaBackground);
        }

        Drawable primaryBackground = styles.getPrimaryTextBackgroundColor();
        if (primaryBackground != null && primaryView != null) {
            primaryView.setBackground(primaryBackground);
        }

        Drawable secondaryBackground = styles.getSecondaryTextBackgroundColor();
        if (secondaryBackground != null && secondaryView != null) {
            secondaryView.setBackground(secondaryBackground);
        }

        invalidate();
        requestLayout();
    }

    private boolean adHasOnlyStore(UnifiedNativeAd nativeAd) {
        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser);
    }

    public void setNativeAd(Activity context, UnifiedNativeAd nativeAd) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/merriweather.ttf");
        callToActionView.setTypeface(typeface, Typeface.BOLD);

        this.nativeAd = nativeAd;

        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        String headline = nativeAd.getHeadline();
        String body = nativeAd.getBody();
        String cta = nativeAd.getCallToAction();
        Double starRating = nativeAd.getStarRating();
        Image icon = nativeAd.getIcon();

        String secondaryText;

        nativeAdView.setCallToActionView(callToActionView);
        nativeAdView.setHeadlineView(primaryView);
        nativeAdView.setMediaView(mediaView);

        /*if (adHasOnlyStore(nativeAd)) {
            nativeAdView.setStoreView(secondaryView);
            secondaryText = store;
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView.setAdvertiserView(secondaryView);
            secondaryText = advertiser;
        } else {
            secondaryText = "";
        }*/

        if (nativeAd.getBody() == null) {
            secondaryView.setVisibility(View.INVISIBLE);
        } else {
            secondaryView.setVisibility(View.VISIBLE);
            secondaryView.setText(nativeAd.getBody());
        }

        if (nativeAd.getPrice() == null) {
            ad_price.setVisibility(View.INVISIBLE);
        } else {
            ad_price.setVisibility(View.VISIBLE);
            ad_price.setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            ad_store.setVisibility(View.INVISIBLE);
        } else {
            ad_store.setVisibility(View.VISIBLE);
            ad_store.setText(nativeAd.getStore());
        }

        if (nativeAd.getAdvertiser() == null) {
            ad_advertiser.setVisibility(View.INVISIBLE);
        } else {
            ad_advertiser.setText(nativeAd.getAdvertiser());
            ad_advertiser.setVisibility(View.VISIBLE);
        }

        primaryView.setText(headline);
        callToActionView.setText(cta);

        /*//  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            //secondaryView.setVisibility(GONE);
            ratingBar.setVisibility(VISIBLE);
            ratingBar.setMax(5);
            nativeAdView.setStarRatingView(ratingBar);
        } else {
            //secondaryView.setText(secondaryText);
            secondaryView.setVisibility(VISIBLE);
            ratingBar.setVisibility(GONE);
        }*/

        if (nativeAd.getStarRating() == null) {
            ratingBar.setVisibility(View.INVISIBLE);
        } else {
            ratingBar.setRating(nativeAd.getStarRating().floatValue());
            ratingBar.setVisibility(View.VISIBLE);
        }

        if (icon != null) {
            iconView.setVisibility(VISIBLE);
            iconView.setImageDrawable(icon.getDrawable());
        } else {
            iconView.setVisibility(GONE);
        }


        nativeAdView.setNativeAd(nativeAd);
    }

    /**
     * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
     * method does not destroy the template view.
     * https://developers.google.com/admob/android/native-unified#destroy_ad
     */
    public void destroyNativeAd() {
        nativeAd.destroy();
    }


    private void initView(Context context, AttributeSet attributeSet) {

        TypedArray attributes =
                context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0);

        try {
            templateType = attributes.getResourceId(
                    R.styleable.TemplateView_gnt_template_type, R.layout.gnt_medium_template_view);
        } finally {
            attributes.recycle();
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(templateType, this);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        nativeAdView = (UnifiedNativeAdView) findViewById(R.id.native_ad_view);
        primaryView = (TextView) findViewById(R.id.primary);
        secondaryView = (TextView) findViewById(R.id.secondary);
        ad_price = (TextView) findViewById(R.id.ad_price);
        ad_store = (TextView) findViewById(R.id.ad_store);
        ad_advertiser = (TextView) findViewById(R.id.ad_advertiser);

        ratingBar = (AppCompatRatingBar) findViewById(R.id.rating_bar);
        ratingBar.setEnabled(false);

        callToActionView = (AppCompatTextView) findViewById(R.id.cta);
        iconView = (ImageView) findViewById(R.id.icon);
        mediaView = (MediaView) findViewById(R.id.media_view);
        background = (LinearLayout) findViewById(R.id.background);

    }
}
