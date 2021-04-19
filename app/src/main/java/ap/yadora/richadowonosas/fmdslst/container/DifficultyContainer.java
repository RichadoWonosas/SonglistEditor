package ap.yadora.richadowonosas.fmdslst.container;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigInteger;

import ap.yadora.richadowonosas.fmdslst.R;

final class DifficultyContainer {
    private final View root;
    private final Button expandButton;
    private final TextView title, contentDifficulty;
    private final SingleContainer chartDesigner, jacketDesigner, rating, jacketNight, bg;
    private final CheckContainer enabled, ratingPlus, jacketOverride, hiddenUntilUnlocked, worldUnlock;
    private boolean isExpanded, isAvailable, isVisible;
    private int partsHeight, textHeight, rarePartsHeight, rareTextHeight;
    private final int HEIGHT_PARTS;

    public DifficultyContainer(View view, int rootId) {
        root = view.findViewById(rootId);
        HEIGHT_PARTS = (int) root.getResources().getDimension(R.dimen.height_parts);
        partsHeight = textHeight = 0;

        title = root.findViewById(R.id.editTitleDifficulty);
        contentDifficulty = root.findViewById(R.id.contentDifficulty);
        enabled = new CheckContainer(root, R.id.editAvailable);
        expandButton = root.findViewById(R.id.editButtonExpandDifficulty);

        chartDesigner = new SingleContainer(root, R.id.editChartDesigner, SingleContainer.TYPE_MULTILINE_TEXT);
        jacketDesigner = new SingleContainer(root, R.id.editJacketDesigner, SingleContainer.TYPE_MULTILINE_TEXT);
        rating = new SingleContainer(root, R.id.editRating, SingleContainer.TYPE_SIGNED);
        ratingPlus = new CheckContainer(root, R.id.editRatingPlus);
        jacketNight = new SingleContainer(root, R.id.editJacketNight, SingleContainer.TYPE_PLAIN_TEXT);
        jacketOverride = new CheckContainer(root, R.id.editJacketOverride);
        hiddenUntilUnlocked = new CheckContainer(root, R.id.editHiddenUntilUnlocked);
        bg = new SingleContainer(root, R.id.editBackground, SingleContainer.TYPE_PLAIN_TEXT);
        worldUnlock = new CheckContainer(root, R.id.editWorldUnlock);

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                refresh();
            }
        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        };

        isExpanded = isAvailable = false;
        expandButton.setText(R.string.selection_expand);
        enabled.getContent().setChecked(false);
        enabled.getContent().setOnClickListener(onClickListener);
        enabled.getTitle().setText(R.string.info_difficulty_enable);
        chartDesigner.getContent().setEnabled(false);
        chartDesigner.getTitle().setText(R.string.info_difficulty_chart_designer);
        jacketDesigner.getContent().setEnabled(false);
        jacketDesigner.getTitle().setText(R.string.info_difficulty_jacket_designer);
        rating.getContent().setEnabled(false);
        rating.getContent().setOnFocusChangeListener(onFocusChangeListener);
        rating.getTitle().setText(R.string.info_difficulty_rating);
        ratingPlus.getContent().setClickable(false);
        ratingPlus.getTitle().setText(R.string.info_difficulty_rating_plus);
        ratingPlus.getContent().setOnClickListener(onClickListener);
        jacketNight.getContent().setEnabled(false);
        jacketNight.getTitle().setText(R.string.info_difficulty_jacket_night);
        jacketOverride.getContent().setClickable(false);
        jacketOverride.getTitle().setText(R.string.info_difficulty_jacket_override);
        hiddenUntilUnlocked.getContent().setClickable(false);
        hiddenUntilUnlocked.getTitle().setText(R.string.info_songlist_hidden);
        bg.getContent().setEnabled(false);
        bg.getTitle().setText(R.string.info_bg);
        worldUnlock.getContent().setClickable(false);
        worldUnlock.getTitle().setText(R.string.info_world_unlock);

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                refresh();
            }
        });

        refresh();
    }

    private void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public void refresh() {
        isAvailable = enabled.getContent().isChecked();
        isExpanded = isExpanded & isAvailable;
        textHeight = partsHeight = rareTextHeight = rarePartsHeight = 0;

        if (isExpanded) {
            expandButton.setText(R.string.selection_collapse);
            expandButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, root.getContext().getDrawable(R.drawable.icon_normal_expand), null);
            if (isAvailable) {
                textHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                partsHeight = HEIGHT_PARTS;
                if (isVisible) {
                    rareTextHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                    rarePartsHeight = HEIGHT_PARTS;
                }
            }
        } else {
            expandButton.setText(R.string.selection_expand);
            expandButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, root.getContext().getDrawable(R.drawable.icon_normal_collapse), null);
        }

        if (!isAvailable) {
            contentDifficulty.setText(R.string.default_song_difficulty);
        } else {
            if (!isExpanded) {
                if (rating.getContent().getText().length() == 0) {
                    rating.getContent().setText("0");
                }
            }
            int diff = new BigInteger(rating.getContent().getText().toString()).intValue();
            contentDifficulty.setText(((diff == 0) ? "?" : diff) + ((ratingPlus.getContent().isChecked()) ? "+" : ""));
        }

        chartDesigner.getContent().setEnabled(isAvailable);
        jacketDesigner.getContent().setEnabled(isAvailable);
        rating.getContent().setEnabled(isAvailable);
        ratingPlus.getContent().setClickable(isAvailable);
        jacketNight.getContent().setEnabled(isAvailable && isVisible);
        jacketOverride.getContent().setClickable(isAvailable);
        hiddenUntilUnlocked.getContent().setClickable(isAvailable);
        bg.getContent().setEnabled(isAvailable && isVisible);
        worldUnlock.getContent().setClickable(isAvailable && isVisible);

        setHeight(chartDesigner.getRoot(), textHeight);
        setHeight(jacketDesigner.getRoot(), textHeight);
        setHeight(rating.getRoot(), textHeight);
        setHeight(ratingPlus.getRoot(), partsHeight);
        setHeight(jacketNight.getRoot(), rareTextHeight);
        setHeight(jacketOverride.getRoot(), partsHeight);
        setHeight(hiddenUntilUnlocked.getRoot(), partsHeight);
        setHeight(bg.getRoot(), rareTextHeight);
        setHeight(worldUnlock.getRoot(), rarePartsHeight);
    }

    private void setWeight(View view, float weight) {
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(view.getLayoutParams());
        layoutParams.weight = weight;
        view.setLayoutParams(layoutParams);
    }

    public View getRoot() {
        return root;
    }

    public TextView getTitle() {
        return title;
    }

    public SingleContainer getChartDesigner() {
        return chartDesigner;
    }

    public SingleContainer getJacketDesigner() {
        return jacketDesigner;
    }

    public SingleContainer getRating() {
        return rating;
    }

    public SingleContainer getJacketNight() {
        return jacketNight;
    }

    public SingleContainer getBg() {
        return bg;
    }

    public CheckContainer getEnabled() {
        return enabled;
    }

    public CheckContainer getRatingPlus() {
        return ratingPlus;
    }

    public CheckContainer getJacketOverride() {
        return jacketOverride;
    }

    public CheckContainer getHiddenUntilUnlocked() {
        return hiddenUntilUnlocked;
    }

    public CheckContainer getWorldUnlock() {
        return worldUnlock;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
        refresh();
    }
}
