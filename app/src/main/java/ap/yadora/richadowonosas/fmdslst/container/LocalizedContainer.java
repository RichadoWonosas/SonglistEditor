package ap.yadora.richadowonosas.fmdslst.container;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ap.yadora.richadowonosas.fmdslst.R;

final class LocalizedContainer {
    private final View root;
    private final Button expandButton;
    private final TextView title;
    private final SingleContainer en, ja, zhHans, zhHant, ko;
    private boolean isExpanded;
    private int partsHeight, textHeight;
    private final int HEIGHT_PARTS;

    public LocalizedContainer(View view, int rootId) {
        root = view.findViewById(rootId);
        HEIGHT_PARTS = (int) root.getResources().getDimension(R.dimen.height_parts);
        partsHeight = 0;
        textHeight = 0;

        title = root.findViewById(R.id.editTitleLocalized);
        expandButton = root.findViewById(R.id.editButtonExpandLocalized);
        en = new SingleContainer(root, R.id.editEn, SingleContainer.TYPE_MULTILINE_TEXT);
        ja = new SingleContainer(root, R.id.editJa, SingleContainer.TYPE_MULTILINE_TEXT);
        zhHans = new SingleContainer(root, R.id.editZhHans, SingleContainer.TYPE_MULTILINE_TEXT);
        zhHant = new SingleContainer(root, R.id.editZhHant, SingleContainer.TYPE_MULTILINE_TEXT);
        ko = new SingleContainer(root, R.id.editKo, SingleContainer.TYPE_MULTILINE_TEXT);
        expandButton.setText(R.string.selection_expand);

        isExpanded = false;
        en.getTitle().setText(R.string.info_localized_en);
        ja.getTitle().setText(R.string.info_localized_ja);
        zhHans.getTitle().setText(R.string.info_localized_zh_hans);
        zhHant.getTitle().setText(R.string.info_localized_zh_hant);
        ko.getTitle().setText(R.string.info_localized_ko);

        setHeight(en.getRoot(), textHeight);
        setHeight(ja.getRoot(), textHeight);
        setHeight(zhHans.getRoot(), textHeight);
        setHeight(zhHant.getRoot(), textHeight);
        setHeight(ko.getRoot(), textHeight);

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                if (isExpanded) {
                    expandButton.setText(R.string.selection_collapse);
                    partsHeight = HEIGHT_PARTS;
                    textHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                } else {
                    expandButton.setText(R.string.selection_expand);
                    partsHeight = textHeight = 0;
                }

                setHeight(en.getRoot(), textHeight);
                setHeight(ja.getRoot(), textHeight);
                setHeight(zhHans.getRoot(), textHeight);
                setHeight(zhHant.getRoot(), textHeight);
                setHeight(ko.getRoot(), textHeight);
            }
        });
    }

    private void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public View getRoot() {
        return root;
    }

    public TextView getTitle() {
        return title;
    }

    public SingleContainer getEn() {
        return en;
    }

    public SingleContainer getJa() {
        return ja;
    }

    public SingleContainer getZhHans() {
        return zhHans;
    }

    public SingleContainer getZhHant() {
        return zhHant;
    }

    public SingleContainer getKo() {
        return ko;
    }
}
