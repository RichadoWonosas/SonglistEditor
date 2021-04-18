package ap.yadora.richadowonosas.fmdslst.container;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ap.yadora.richadowonosas.fmdslst.R;

final class DifficultyContainer {
    private final View root;
    private final Button expandButton;
    private final TextView title;
    private final SingleContainer chartDesigner, jacketDesigner, rating, jacketNight, bg;
    private final CheckContainer enabled, ratingPlus, jacketOverride, hiddenUntilUnlocked, worldUnlock;
    private boolean isExpanded, isAvailable, isVisible;
    private int checkHeight, partsHeight, textHeight, rarePartsHeight, rareTextHeight;
    private final int HEIGHT_PARTS;

    public DifficultyContainer(View view, int rootId) {
        root = view.findViewById(rootId);
        HEIGHT_PARTS = (int) root.getResources().getDimension(R.dimen.height_parts);
        checkHeight = partsHeight = textHeight = 0;

        title = root.findViewById(R.id.editTitleDifficulty);
        expandButton = root.findViewById(R.id.editButtonExpandDifficulty);
        enabled = new CheckContainer(root, R.id.editAvailable);

        chartDesigner = new SingleContainer(root, R.id.editChartDesigner, SingleContainer.TYPE_MULTILINE_TEXT);
        jacketDesigner = new SingleContainer(root, R.id.editJacketDesigner, SingleContainer.TYPE_MULTILINE_TEXT);
        rating = new SingleContainer(root, R.id.editRating, SingleContainer.TYPE_INTEGER);
        ratingPlus = new CheckContainer(root, R.id.editRatingPlus);
        jacketNight = new SingleContainer(root, R.id.editJacketNight, SingleContainer.TYPE_PLAIN_TEXT);
        jacketOverride = new CheckContainer(root, R.id.editJacketOverride);
        hiddenUntilUnlocked = new CheckContainer(root, R.id.editHiddenUntilUnlocked);
        bg = new SingleContainer(root, R.id.editBackground, SingleContainer.TYPE_PLAIN_TEXT);
        worldUnlock = new CheckContainer(root, R.id.editWorldUnlock);

        isExpanded = isAvailable = false;
        expandButton.setText(R.string.selection_expand);
        enabled.getContent().setChecked(false);
        enabled.getTitle().setText(R.string.info_difficulty_enable);
        chartDesigner.getContent().setEnabled(false);
        chartDesigner.getTitle().setText(R.string.info_difficulty_chart_designer);
        jacketDesigner.getContent().setEnabled(false);
        jacketDesigner.getTitle().setText(R.string.info_difficulty_jacket_designer);
        rating.getContent().setEnabled(false);
        rating.getTitle().setText(R.string.info_difficulty_rating);
        ratingPlus.getContent().setClickable(false);
        ratingPlus.getTitle().setText(R.string.info_difficulty_rating_plus);
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

        enabled.getContent().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

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
         textHeight = partsHeight = rareTextHeight = rarePartsHeight = 0;

        if (isExpanded) {
            expandButton.setText(R.string.selection_collapse);
            checkHeight = HEIGHT_PARTS;
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
            checkHeight = 0;
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

        setHeight(enabled.getRoot(), checkHeight);
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
